package view;

import controller.GestorAtencion;
import excepciones.PesoNoRegistradoException;
import excepciones.StockInsuficienteException;
import excepciones.VacunaNoAplicableException;
import model.atencion.AplicacionVacuna;
import model.atencion.Consulta;
import model.atencion.Tratamiento;
import model.atencion.Vacuna;
import model.mascotas.Mascota;
import model.personas.Duenio;
import model.personas.Veterinario;
import model.stock.Insumo;

import java.util.List;
import java.util.Scanner;

/**
 * Pantalla del veterinario: atender una mascota (consulta, diagnóstico, tratamiento,
 * vacunas) y ver su historia clínica. Captura las excepciones de negocio y muestra el mensaje.
 */
public class VistaVeterinario {
    private final GestorAtencion gestor;
    private final Scanner scanner;

    public VistaVeterinario(GestorAtencion gestor, Scanner scanner) {
        this.gestor = gestor;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        Veterinario veterinario = identificarVeterinario();
        if (veterinario == null) return;

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== ATENCIÓN VETERINARIA - Dr/a. " + veterinario.getNombre() + " " + veterinario.getApellido() + " ===");
            System.out.println("1. Atender mascota (nueva consulta)");
            System.out.println("2. Ver historia clínica de una mascota");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1 -> atenderMascota(veterinario);
                    case 2 -> verHistoriaClinica();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            }
        }
    }

    // Pasos de la atención

    private Veterinario identificarVeterinario() {
        System.out.println("\nVeterinarios:");
        for (Veterinario v : gestor.getVeterinarios()) {
            System.out.println("  [" + v.getDocumento() + "] " + v.getNombre() + " " + v.getApellido()
                    + " - " + v.getEspecialidad());
        }
        System.out.print("Ingrese su documento: ");
        Veterinario v = gestor.buscarVeterinario(scanner.nextLine());
        if (v == null) System.out.println("No hay un veterinario con ese documento.");
        return v;
    }

    private Mascota elegirMascota() {
        System.out.print("DNI del dueño: ");
        Duenio duenio = gestor.buscarDuenio(scanner.nextLine());
        if (duenio == null) {
            System.out.println("No existe un cliente con ese DNI.");
            return null;
        }
        List<Mascota> mascotas = gestor.mascotasDe(duenio);
        if (mascotas.isEmpty()) {
            System.out.println(duenio.getNombre() + " no tiene mascotas registradas.");
            return null;
        }
        System.out.println("Mascotas de " + duenio.getNombre() + ":");
        for (int i = 0; i < mascotas.size(); i++) {
            Mascota m = mascotas.get(i);
            System.out.println("  " + (i + 1) + ". " + m.getNombre() + " (" + m.getEspecie() + ", " + m.getPeso() + " kg)");
        }
        int n = leerEntero("Elija una mascota: ");
        if (n < 1 || n > mascotas.size()) {
            System.out.println("Opción inválida.");
            return null;
        }
        return mascotas.get(n - 1);
    }

    private void atenderMascota(Veterinario veterinario) {
        Mascota mascota = elegirMascota();
        if (mascota == null) return;

        System.out.print("Motivo de la consulta: ");
        String motivo = scanner.nextLine().trim();
        int peso = leerEntero("Peso del día (kg): ");

        Consulta consulta;
        try {
            consulta = gestor.iniciarConsulta(mascota, veterinario, motivo, peso);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        System.out.println("Consulta #" + consulta.getIdConsulta() + " iniciada para " + mascota.getNombre() + ".");

        int opcion = -1;
        while (opcion != 0 && opcion != 5) {
            System.out.println("\n--- Consulta #" + consulta.getIdConsulta() + " - " + mascota.getNombre() + " ---");
            System.out.println("1. Registrar diagnóstico");
            System.out.println("2. Indicar tratamiento");
            System.out.println("3. Aplicar vacuna");
            System.out.println("4. Ver resumen");
            System.out.println("5. Finalizar consulta (descuenta stock y guarda)");
            System.out.println("0. Cancelar consulta (no se guarda)");
            opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrarDiagnostico(consulta, veterinario);
                    case 2 -> indicarTratamiento(consulta);
                    case 3 -> aplicarVacuna(consulta, mascota);
                    case 4 -> mostrarResumen(consulta);
                    case 5 -> {
                        gestor.finalizarConsulta(consulta, mascota);
                        System.out.println("Consulta finalizada y guardada en la historia clínica.");
                    }
                    case 0 -> {
                        gestor.descartarConsulta(consulta, mascota);
                        System.out.println("Consulta cancelada.");
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (PesoNoRegistradoException | VacunaNoAplicableException e) {
                System.out.println("No se pudo completar: " + e.getMessage());
            } catch (StockInsuficienteException e) {
                System.out.println("No se puede finalizar: " + e.getMessage());
                opcion = -1;   // vuelve al menú de la consulta para corregir
            } finally {
                System.out.println("··································");
            }
        }
    }

    private void registrarDiagnostico(Consulta consulta, Veterinario veterinario) {
        System.out.print("Diagnóstico: ");
        String diagnostico = scanner.nextLine().trim();
        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine().trim();
        gestor.registrarDiagnostico(consulta, veterinario, diagnostico, observaciones);
        System.out.println("Diagnóstico registrado.");
    }

    private void indicarTratamiento(Consulta consulta) throws PesoNoRegistradoException {
        System.out.println("Medicamentos disponibles:");
        for (Insumo i : gestor.getInsumos()) {
            System.out.println("  [" + i.getCodigo() + "] " + i.getNombre() + " - stock: " + i.getStockActual());
        }
        System.out.print("Código del medicamento: ");
        Insumo medicamento = gestor.buscarInsumo(scanner.nextLine());
        if (medicamento == null) {
            System.out.println("No existe un insumo con ese código.");
            return;
        }
        if (medicamento.getStockActual() == 0) {
            System.out.println("Atención: " + medicamento.getNombre() + " no tiene stock. No se podrá finalizar la consulta.");
        }
        System.out.print("Dosis: ");
        String dosis = scanner.nextLine().trim();
        System.out.print("Frecuencia: ");
        String frecuencia = scanner.nextLine().trim();
        System.out.print("Duración: ");
        String duracion = scanner.nextLine().trim();

        gestor.indicarTratamiento(consulta, medicamento, dosis, frecuencia, duracion);
        System.out.println("Tratamiento indicado (el stock se descuenta al finalizar).");
    }

    private void aplicarVacuna(Consulta consulta, Mascota mascota) throws VacunaNoAplicableException {
        List<Vacuna> vacunas = gestor.vacunasPara(mascota);
        if (vacunas.isEmpty()) {
            System.out.println("No hay vacunas en el catálogo para " + mascota.getEspecie() + ".");
            return;
        }
        System.out.println("Vacunas para " + mascota.getEspecie() + ":");
        for (int i = 0; i < vacunas.size(); i++) {
            Vacuna v = vacunas.get(i);
            System.out.println("  " + (i + 1) + ". " + v.getNombre() + " (edad mínima " + v.getEdadMinMeses() + " meses)");
        }
        int n = leerEntero("Elija una vacuna: ");
        if (n < 1 || n > vacunas.size()) {
            System.out.println("Opción inválida.");
            return;
        }
        System.out.print("Número de lote: ");
        String lote = scanner.nextLine().trim();

        AplicacionVacuna aplicacion = gestor.aplicarVacuna(consulta, mascota, vacunas.get(n - 1), lote);
        System.out.println("Vacuna aplicada. Próximo refuerzo: " + aplicacion.getFechaRefuerzo());
    }

    private void mostrarResumen(Consulta c) {
        System.out.println("Consulta #" + c.getIdConsulta() + " - " + c.getFecha());
        System.out.println("  Motivo: " + c.getMotivo() + " | Peso: " + c.getPesoDia() + " kg");
        System.out.println("  Diagnóstico: " + c.getDiagnostico());
        System.out.println("  Observaciones: " + c.getObservaciones());
        for (Tratamiento t : c.getTratamientos()) {
            System.out.println("  Tratamiento: " + t.getMedicamento().getNombre() + " - " + t.getDosis()
                    + ", " + t.getFrecuencia() + ", " + t.getDuracion());
        }
        for (AplicacionVacuna a : c.getVacunas()) {
            System.out.println("  Vacuna: " + a.getVacuna().getNombre() + " (lote " + a.getLote()
                    + ") - refuerzo: " + a.getFechaRefuerzo());
        }
    }

    private void verHistoriaClinica() {
        Mascota mascota = elegirMascota();
        if (mascota == null) return;
        List<Consulta> consultas = gestor.historiaClinica(mascota);
        System.out.println("\n=== HISTORIA CLÍNICA DE " + mascota.getNombre().toUpperCase() + " ===");
        if (consultas.isEmpty()) {
            System.out.println("Sin consultas registradas.");
        }
        for (Consulta c : consultas) {
            mostrarResumen(c);
            System.out.println("  .........................");
        }
    }

    // Auxiliares

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
}
