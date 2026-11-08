package model.turnos;

import model.enums.EstadoTurno;
import model.enums.TipoAtencion;
import model.mascotas.Mascota;
import model.personas.Veterinario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Turno implements Comparable<Turno> {
    private static int contador = 0;
    private int idTurno;
    private Mascota mascota;
    private Veterinario veterinario;
    private LocalDate fecha;
    private LocalTime hora;
    private TipoAtencion tipoAtencion;
    private EstadoTurno estado;
    private boolean urgente;

    public Turno(Mascota mascota, Veterinario veterinario, LocalDate fecha, LocalTime hora, TipoAtencion tipoAtencion, boolean urgente) {
        this.idTurno = ++contador;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoAtencion = tipoAtencion;
        this.estado = EstadoTurno.PROGRAMADO;
        this.urgente = urgente;
    }


    public LocalTime getHoraFin() {
        return hora.plusMinutes(tipoAtencion.getTiempo());
    }


    public boolean seSuperponeCon(Turno otro) {
        if (this.estado == EstadoTurno.CANCELADO || otro.estado == EstadoTurno.CANCELADO) {
            return false;
        }
        if (this.veterinario.getDocumento().equals(otro.veterinario.getDocumento()) && this.fecha.equals(otro.fecha)) {
            return this.hora.isBefore(otro.getHoraFin()) && otro.hora.isBefore(this.getHoraFin());
        }
        return false;
    }


    public double cancelar(LocalDateTime ahora) {
        this.estado = EstadoTurno.CANCELADO;
        LocalDateTime fechaHoraTurno = LocalDateTime.of(fecha, hora);
        if (fechaHoraTurno.isBefore(ahora.plusHours(2))) {
            return tipoAtencion.getCosto() * 0.20;
        }
        return 0.0;
    }

    public void reprogramar(LocalDate nuevaFecha, LocalTime nuevaHora) {
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
    }

    public void marcarAtendido() { this.estado = EstadoTurno.ATENDIDO; }
    public void marcarAusente() { this.estado = EstadoTurno.AUSENTE; }

    @Override
    public int compareTo(Turno otro) {
        int comparacionFecha = this.fecha.compareTo(otro.fecha);
        if (comparacionFecha == 0) {
            return this.hora.compareTo(otro.hora);
        }
        return comparacionFecha;
    }


    public int getIdTurno() { return idTurno; }
    public Mascota getMascota() { return mascota; }
    public Veterinario getVeterinario() { return veterinario; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public TipoAtencion getTipoAtencion() { return tipoAtencion; }
    public EstadoTurno getEstado() { return estado; }
    public boolean isUrgente() { return urgente; }
}