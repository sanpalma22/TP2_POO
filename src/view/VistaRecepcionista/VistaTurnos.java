package view.VistaRecepcionista;

import controller.GestorClientes;
import controller.GestorTurnos;
import excepciones.SeniaRequeridaException;
import excepciones.TurnoSuperpuestoException;
import model.enums.TipoAtencion;
import model.mascotas.Mascota;
import model.personas.Duenio;
import model.personas.Veterinario;
import model.turnos.Turno;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Pantalla de turnos: asignar, reprogramar, cancelar, asistencia, urgencias y agenda.
 */
public class VistaTurnos {
    private final Scanner scanner;
    private final GestorTurnos gestor;
    private final GestorClientes gestorClientes;

    public VistaTurnos(Scanner scanner, GestorTurnos gestor, GestorClientes gestorClientes) {
        this.scanner = scanner;
        this.gestor = gestor;
        this.gestorClientes = gestorClientes;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== TURNOS ===");
            System.out.println("1. Asignar turno");
            System.out.println("2. Reprogramar turno");
            System.out.println("3. Cancelar turno");
            System.out.println("4. Registrar asistencia / ausencia");
            System.out.println("5. Urgencia");
            System.out.println("6. Ver agenda de un veterinario");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> asignar();
                    case 2 -> reprogramar();
                    case 3 -> cancelar();
                    case 4 -> asistencia();
                    case 5 -> urgencia();
                    case 6 -> verAgenda();
                    case 0 -> System.out.println("Volviendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (TurnoSuperpuestoException | SeniaRequeridaException e) {
                System.out.println("No se pudo dar el turno: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("··································");
            }
        }
    }

    // Opciones

    private void asignar() throws TurnoSuperpuestoException, SeniaRequeridaException {
        Mascota mascota = elegirMascota();
        if (mascota == null) return;
        Veterinario vet = elegirVeterinario();
        if (vet == null) return;
        TipoAtencion tipo = elegirTipo();
        LocalDate fecha = leerFecha("Fecha");
        LocalTime hora = leerHora("Hora");

        Turno t = gestor.asignarTurno(mascota, vet, fecha, hora, tipo);
        System.out.println("Turno #" + t.getIdTurno() + " asignado: " + describir(t));
    }

    private void reprogramar() throws TurnoSuperpuestoException {
        Turno t = elegirTurnoProgramado();
        if (t == null) return;
        LocalDate fecha = leerFecha("Nueva fecha");
        LocalTime hora = leerHora("Nueva hora");
        gestor.reprogramarTurno(t, fecha, hora);
        System.out.println("Turno reprogramado: " + describir(t));
    }

    private void cancelar() {
        Turno t = elegirTurnoProgramado();
        if (t == null) return;
        double cargo = gestor.cancelarTurno(t, LocalDateTime.now());
        if (cargo > 0) {
            System.out.printf("Turno cancelado con menos de 2 h de anticipación. Cargo: $%.2f%n", cargo);
        } else {
            System.out.println("Turno cancelado sin cargo.");
        }
    }

    private void asistencia() {
        Turno t = elegirTurnoProgramado();
        if (t == null) return;
        System.out.print("¿Asistió? (s/n): ");
        boolean asistio = scanner.nextLine().trim().equalsIgnoreCase("s");
        gestor.registrarAsistencia(t, asistio);
        Duenio d = t.getMascota().getDuenio();
        System.out.println(asistio ? "Asistencia registrada." : "Ausencia registrada. " + d.getNombre()
                + (d.debePagarSenia() ? " ya tiene 3 ausencias seguidas: deberá pagar seña." : ""));
    }

    private void urgencia() throws TurnoSuperpuestoException {
        Mascota mascota = elegirMascota();
        if (mascota == null) return;
        System.out.println("Veterinario de guardia:");
        Veterinario guardia = elegirVeterinario();
        if (guardia == null) return;
        TipoAtencion tipo = elegirTipo();

        List<String> avisos = gestor.asignarUrgencia(mascota, guardia, tipo, LocalDateTime.now());
        System.out.println("Urgencia asignada para ahora con " + guardia.getNombre() + ".");
        for (String aviso : avisos) System.out.println("  NOTIFICAR: " + aviso);
    }

    private void verAgenda() {
        Veterinario vet = elegirVeterinario();
        if (vet == null) return;
        List<Turno> agenda = gestor.turnosDeVeterinario(vet);
        System.out.println("\nAgenda de " + vet.getNombre() + " " + vet.getApellido() + ":");
        if (agenda.isEmpty()) System.out.println("Sin turnos.");
        for (Turno t : agenda) System.out.println("  #" + t.getIdTurno() + " " + describir(t));
    }

    // Selección

    private Mascota elegirMascota() {
        System.out.print("DNI del dueño: ");
        Duenio duenio = gestorClientes.buscarDuenio(scanner.nextLine().trim());
        if (duenio == null) {
            System.out.println("No existe un cliente con ese DNI.");
            return null;
        }
        List<Mascota> mascotas = gestorClientes.mascotasDe(duenio);
        if (mascotas.isEmpty()) {
            System.out.println(duenio.getNombre() + " no tiene mascotas registradas.");
            return null;
        }
        for (int i = 0; i < mascotas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + mascotas.get(i).getNombre() + " (" + mascotas.get(i).getEspecie() + ")");
        }
        int n = leerEntero("Elija una mascota: ");
        if (n < 1 || n > mascotas.size()) {
            System.out.println("Opción inválida.");
            return null;
        }
        return mascotas.get(n - 1);
    }

    private Veterinario elegirVeterinario() {
        for (Veterinario v : gestor.getVeterinarios()) {
            System.out.println("  [" + v.getDocumento() + "] " + v.getNombre() + " " + v.getApellido() + " - " + v.getEspecialidad());
        }
        System.out.print("Documento del veterinario: ");
        Veterinario v = gestor.buscarVeterinario(scanner.nextLine());
        if (v == null) System.out.println("No existe un veterinario con ese documento.");
        return v;
    }

    private Turno elegirTurnoProgramado() {
        Mascota mascota = elegirMascota();
        if (mascota == null) return null;
        List<Turno> programados = gestor.turnosProgramadosDe(mascota);
        if (programados.isEmpty()) {
            System.out.println(mascota.getNombre() + " no tiene turnos programados.");
            return null;
        }
        for (int i = 0; i < programados.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + describir(programados.get(i)));
        }
        int n = leerEntero("Elija un turno: ");
        if (n < 1 || n > programados.size()) {
            System.out.println("Opción inválida.");
            return null;
        }
        return programados.get(n - 1);
    }

    private TipoAtencion elegirTipo() {
        TipoAtencion[] tipos = TipoAtencion.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println("  " + (i + 1) + ". " + tipos[i] + " (" + tipos[i].getTiempo() + " min)");
        }
        while (true) {
            int n = leerEntero("Tipo de atención: ");
            if (n >= 1 && n <= tipos.length) return tipos[n - 1];
            System.out.println("Opción inválida.");
        }
    }

    // Auxiliares

    private String describir(Turno t) {
        return t.getFecha() + " " + t.getHora() + "-" + t.getHoraFin() + " | " + t.getMascota().getNombre()
                + " con " + t.getVeterinario().getNombre() + " | " + t.getTipoAtencion() + " | " + t.getEstado()
                + (t.isUrgente() ? " | URGENTE" : "");
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (AAAA-MM-DD): ");
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida.");
            }
        }
    }

    private LocalTime leerHora(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (HH:MM): ");
            try {
                return LocalTime.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Hora inválida.");
            }
        }
    }
}
