package controller;

import excepciones.SeniaRequeridaException;
import excepciones.TurnoSuperpuestoException;
import model.enums.EstadoTurno;
import model.enums.TipoAtencion;
import model.mascotas.Mascota;
import model.personas.FranjaHoraria;
import model.personas.Veterinario;
import model.turnos.Turno;
import repositorios.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controlador de turnos: asignar y reprogramar (CU01), urgencias (CU02), asistencia (CU11),
 * turnos por veterinario (CU15), cancelación (CU18) y seña (CU19).
 * Antes de dar un turno valida el horario del veterinario, la superposición y la seña.
 */
public class GestorTurnos {
    private static final int PASO_MINUTOS = 15;   // grilla para buscar horarios libres
    private static final int DIAS_A_BUSCAR = 14;

    private final List<Turno> turnos;
    private final TurnoRepository turnoRepo;
    private final GestorClientes gestorClientes;
    private final List<Veterinario> veterinarios;

    public GestorTurnos(List<Turno> turnos, TurnoRepository turnoRepo,
                        GestorClientes gestorClientes, List<Veterinario> veterinarios) {
        this.turnos = turnos;
        this.turnoRepo = turnoRepo;
        this.gestorClientes = gestorClientes;
        this.veterinarios = veterinarios;
    }

    // Consultas

    public List<Veterinario> getVeterinarios() { return veterinarios; }

    public Veterinario buscarVeterinario(String documento) {
        for (Veterinario v : veterinarios) {
            if (v.getDocumento().equals(documento.trim())) return v;
        }
        return null;
    }

    public Turno buscarTurno(int idTurno) {
        for (Turno t : turnos) {
            if (t.getIdTurno() == idTurno) return t;
        }
        return null;
    }

    // Reporte: turnos de un veterinario, ordenados por fecha y hora (Comparable de Turno)
    public List<Turno> turnosDeVeterinario(Veterinario veterinario) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getVeterinario() == veterinario) resultado.add(t);
        }
        Collections.sort(resultado);
        return resultado;
    }

    public List<Turno> turnosProgramadosDe(Mascota mascota) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getMascota() == mascota && t.estaProgramado()) resultado.add(t);
        }
        Collections.sort(resultado);
        return resultado;
    }

    // Casos de uso

    // Asignar turno: valida seña, horario del veterinario y superposición
    public Turno asignarTurno(Mascota mascota, Veterinario veterinario, LocalDate fecha, LocalTime hora,
                              TipoAtencion tipo) throws SeniaRequeridaException, TurnoSuperpuestoException {
        if (mascota.getDuenio().debePagarSenia()) {
            throw new SeniaRequeridaException("El dueño tiene 3 ausencias seguidas: debe abonar una seña para pedir turno.");
        }
        validarDisponibilidad(veterinario, fecha, hora, tipo, null);

        Turno turno = new Turno(mascota, veterinario, fecha, hora, tipo, false);
        turnos.add(turno);
        turnoRepo.guardarTodos(turnos);
        return turno;
    }

    // Reprogramar: mismas validaciones, sin contar el propio turno que se mueve
    public void reprogramarTurno(Turno turno, LocalDate fecha, LocalTime hora) throws TurnoSuperpuestoException {
        if (!turno.estaProgramado()) {
            throw new IllegalStateException("Solo se puede reprogramar un turno PROGRAMADO.");
        }
        validarDisponibilidad(turno.getVeterinario(), fecha, hora, turno.getTipoAtencion(), turno);
        turno.reprogramar(fecha, hora);
        turnoRepo.guardarTodos(turnos);
    }

    // Cancelar: sin cargo hasta 2 h antes; después, 20 % de la atención. Devuelve el cargo.
    public double cancelarTurno(Turno turno, LocalDateTime ahora) {
        if (!turno.estaProgramado()) {
            throw new IllegalStateException("Solo se puede cancelar un turno PROGRAMADO.");
        }
        double cargo = turno.cancelar(ahora);
        turnoRepo.guardarTodos(turnos);
        return cargo;
    }

    // Asistencia: actualiza el turno y el contador de ausencias SEGUIDAS del dueño
    public void registrarAsistencia(Turno turno, boolean asistio) {
        if (!turno.estaProgramado()) {
            throw new IllegalStateException("El turno ya fue " + turno.getEstado() + ".");
        }
        if (asistio) {
            turno.marcarAtendido();
            turno.getMascota().getDuenio().registrarAsistencia();
        } else {
            turno.marcarAusente();
            turno.getMascota().getDuenio().registrarAusencia();
        }
        turnoRepo.guardarTodos(turnos);
        gestorClientes.guardarDuenios();
    }

    /*
     * Urgencia: toma el primer horario del veterinario de guardia (ahora).
     * Si hay turnos que se pisan, se desplazan al próximo horario libre y se avisa al dueño.
     * Devuelve los avisos generados.
     */
    public List<String> asignarUrgencia(Mascota mascota, Veterinario guardia, TipoAtencion tipo,
                                        LocalDateTime ahora) throws TurnoSuperpuestoException {
        LocalDate fecha = ahora.toLocalDate();
        LocalTime hora = ahora.toLocalTime().withSecond(0).withNano(0);
        if (!guardia.horarioDisponible(fecha, hora, tipo)) {
            throw new TurnoSuperpuestoException(guardia.getNombre() + " no está de guardia en este horario.");
        }

        Turno urgencia = new Turno(mascota, guardia, fecha, hora, tipo, true);

        List<Turno> desplazados = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.estaProgramado() && urgencia.seSuperponeCon(t)) desplazados.add(t);
        }
        turnos.add(urgencia);

        List<String> avisos = new ArrayList<>();
        for (Turno t : desplazados) {
            LocalDateTime nuevo = buscarProximoHorarioLibre(guardia, fecha, urgencia.getHoraFin(), t.getTipoAtencion(), t);
            if (nuevo == null) {
                t.cancelarSinCargo();   // la cancelación no es culpa del dueño
                avisos.add("Turno #" + t.getIdTurno() + " de " + t.getMascota().getNombre()
                        + " CANCELADO por urgencia (sin horario libre). Avisar a " + t.getMascota().getDuenio().getNombre() + ".");
            } else {
                t.reprogramar(nuevo.toLocalDate(), nuevo.toLocalTime());
                avisos.add("Turno #" + t.getIdTurno() + " de " + t.getMascota().getNombre() + " reprogramado para el "
                        + nuevo.toLocalDate() + " a las " + nuevo.toLocalTime()
                        + ". Avisar a " + t.getMascota().getDuenio().getNombre() + " (" + t.getMascota().getDuenio().getTelefono() + ").");
            }
        }
        turnoRepo.guardarTodos(turnos);
        return avisos;
    }

    // Auxiliares

    // Lanza la excepción si el veterinario no atiende en ese horario o ya tiene un turno que se pisa
    private void validarDisponibilidad(Veterinario veterinario, LocalDate fecha, LocalTime hora,
                                       TipoAtencion tipo, Turno excluido) throws TurnoSuperpuestoException {
        if (LocalDateTime.of(fecha, hora).isBefore(LocalDateTime.now())) {
            throw new TurnoSuperpuestoException("No se puede dar un turno en el pasado.");
        }
        if (!veterinario.horarioDisponible(fecha, hora, tipo)) {
            throw new TurnoSuperpuestoException(veterinario.getNombre() + " no atiende el " + fecha.getDayOfWeek()
                    + " a las " + hora + " (o la atención de " + tipo.getTiempo() + " min no entra en su horario).");
        }
        if (haySuperposicion(veterinario, fecha, hora, tipo, excluido)) {
            throw new TurnoSuperpuestoException(veterinario.getNombre() + " ya tiene un turno que se superpone con ese horario.");
        }
    }

    private boolean haySuperposicion(Veterinario veterinario, LocalDate fecha, LocalTime hora,
                                     TipoAtencion tipo, Turno excluido) {
        Turno candidato = new Turno(0, null, veterinario, fecha, hora, tipo, EstadoTurno.PROGRAMADO, false);
        for (Turno t : turnos) {
            if (t != excluido && candidato.seSuperponeCon(t)) return true;
        }
        return false;
    }

    // Recorre la agenda de a 15 minutos, desde 'desde', hasta encontrar un hueco (máximo 14 días)
    private LocalDateTime buscarProximoHorarioLibre(Veterinario veterinario, LocalDate fecha, LocalTime desde,
                                                    TipoAtencion tipo, Turno excluido) {
        for (int d = 0; d < DIAS_A_BUSCAR; d++) {
            LocalDate dia = fecha.plusDays(d);
            FranjaHoraria franja = veterinario.getHorarios().get(dia.getDayOfWeek());
            if (franja == null) continue;
            LocalTime hora = (d == 0 && desde.isAfter(franja.getHoraEntrada())) ? desde : franja.getHoraEntrada();
            while (hora.isBefore(franja.getHoraSalida())) {
                if (veterinario.horarioDisponible(dia, hora, tipo)
                        && !haySuperposicion(veterinario, dia, hora, tipo, excluido)) {
                    return LocalDateTime.of(dia, hora);
                }
                hora = hora.plusMinutes(PASO_MINUTOS);
            }
        }
        return null;
    }
}
