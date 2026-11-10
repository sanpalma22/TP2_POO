package view;

import controller.GestorAtencion;
import controller.GestorStock;
import controller.GestorTurnos;
import model.atencion.AplicacionVacuna;
import model.atencion.RecordatorioVacuna;
import model.atencion.Vacuna;
import model.enums.Especie;
import model.personas.Veterinario;
import model.stock.Insumo;
import model.stock.Lote;
import model.stock.Proveedor;
import model.turnos.Turno;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Pantalla del administrador: reportes (stock bajo, vacunas a reforzar,
 * turnos por veterinario) y gestión de stock y catálogo. Solo habla con los controladores.
 */
public class VistaAdministrador {
    private final Scanner scanner;
    private final GestorStock gestorStock;
    private final GestorTurnos gestorTurnos;
    private final GestorAtencion gestorAtencion;

    public VistaAdministrador(Scanner scanner, GestorStock gestorStock,
                              GestorTurnos gestorTurnos, GestorAtencion gestorAtencion) {
        this.scanner = scanner;
        this.gestorStock = gestorStock;
        this.gestorTurnos = gestorTurnos;
        this.gestorAtencion = gestorAtencion;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== ADMINISTRACIÓN ===");
            System.out.println("--- Reportes ---");
            System.out.println("1. Productos con stock bajo");
            System.out.println("2. Vacunas próximas a vencer / vencidas");
            System.out.println("3. Turnos de un veterinario");
            System.out.println("4. Stock completo (insumos y lotes)");
            System.out.println("--- Gestión ---");
            System.out.println("5. Registrar ingreso de lote");
            System.out.println("6. Registrar insumo");
            System.out.println("7. Registrar proveedor");
            System.out.println("8. Catálogo de vacunas (ver / agregar)");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> reporteStockBajo();
                    case 2 -> reporteVacunas();
                    case 3 -> reporteTurnos();
                    case 4 -> listarStock();
                    case 5 -> ingresarLote();
                    case 6 -> registrarInsumo();
                    case 7 -> registrarProveedor();
                    case 8 -> catalogoVacunas();
                    case 0 -> System.out.println("Volviendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("··································");
            }
        }
    }

    // Reportes

    private void reporteStockBajo() {
        List<Insumo> bajos = gestorStock.insumosConStockBajo();
        System.out.println("\nALERTA DE REPOSICIÓN (más críticos primero)");
        if (bajos.isEmpty()) System.out.println("Todos los productos están por encima del mínimo.");
        for (Insumo i : bajos) {
            System.out.println("  [" + i.getCodigo() + "] " + i.getNombre() + " - disponible: " + i.getStockActual()
                    + " / mínimo: " + i.getStockMinimo() + " - proveedor: " + i.getProveedor().getRazonSocial());
        }
    }

    private void reporteVacunas() {
        LocalDate hoy = LocalDate.now();
        List<RecordatorioVacuna> lista = gestorAtencion.vacunasPorVencer(hoy);
        System.out.println("\nVACUNAS A REFORZAR (al " + hoy + ")");
        if (lista.isEmpty()) System.out.println("No hay refuerzos próximos ni vencidos.");
        for (RecordatorioVacuna r : lista) {
            AplicacionVacuna a = r.getAplicacion();
            String estado = r.estaVencida(hoy) ? "VENCIDA" : "RECORDATORIO";
            System.out.println("  " + estado + " - " + r.getMascota().getNombre() + " (" + a.getVacuna().getNombre()
                    + ") refuerzo: " + a.getFechaRefuerzo() + " - dueño: " + r.getMascota().getDuenio().getNombre()
                    + " " + r.getMascota().getDuenio().getApellido() + ", tel. " + r.getMascota().getDuenio().getTelefono());
        }
    }

    private void reporteTurnos() {
        for (Veterinario v : gestorTurnos.getVeterinarios()) {
            System.out.println("  [" + v.getDocumento() + "] " + v.getNombre() + " " + v.getApellido());
        }
        System.out.print("Documento del veterinario: ");
        Veterinario vet = gestorTurnos.buscarVeterinario(scanner.nextLine());
        if (vet == null) {
            System.out.println("No existe un veterinario con ese documento.");
            return;
        }
        List<Turno> agenda = gestorTurnos.turnosDeVeterinario(vet);
        System.out.println("\nTurnos de " + vet.getNombre() + " " + vet.getApellido() + ":");
        if (agenda.isEmpty()) System.out.println("Sin turnos.");
        for (Turno t : agenda) {
            System.out.println("  #" + t.getIdTurno() + " " + t.getFecha() + " " + t.getHora() + " | "
                    + t.getMascota().getNombre() + " | " + t.getTipoAtencion() + " | " + t.getEstado()
                    + (t.isUrgente() ? " | URGENTE" : ""));
        }
    }

    private void listarStock() {
        for (Insumo i : gestorStock.getInsumos()) {
            System.out.println("[" + i.getCodigo() + "] " + i.getNombre() + " - disponible: " + i.getStockActual()
                    + (i.estaEnStockBajo() ? "  ⚠ STOCK BAJO" : ""));
            for (Lote l : i.getLotes()) {
                System.out.println("     lote " + l.getNumeroLote() + " vence " + l.getFechaVto() + " - stock "
                        + l.getStockLote() + (l.estaVencido() ? " (VENCIDO)" : ""));
            }
        }
    }

    // Gestión

    private void ingresarLote() {
        listarStock();
        System.out.print("Código del insumo: ");
        Insumo insumo = gestorStock.buscarInsumo(scanner.nextLine());
        if (insumo == null) {
            System.out.println("No existe un insumo con ese código.");
            return;
        }
        System.out.print("Número de lote: ");
        String numero = scanner.nextLine().trim();
        LocalDate vto = leerFecha("Fecha de vencimiento");
        int cantidad = leerEntero("Cantidad recibida: ");
        gestorStock.registrarIngresoLote(insumo, numero, vto, cantidad);
        System.out.println("Lote ingresado. Stock disponible de " + insumo.getNombre() + ": " + insumo.getStockActual());
    }

    private void registrarInsumo() {
        System.out.println("Proveedores:");
        for (Proveedor p : gestorStock.getProveedores()) {
            System.out.println("  [" + p.getCuit() + "] " + p.getRazonSocial());
        }
        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        int minimo = leerEntero("Stock mínimo: ");
        double costo = leerDecimal("Costo: ");
        double precio = leerDecimal("Precio de venta: ");
        System.out.print("CUIT del proveedor: ");
        String cuit = scanner.nextLine().trim();
        Insumo i = gestorStock.registrarInsumo(codigo, nombre, minimo, costo, precio, cuit);
        System.out.println("Insumo " + i.getCodigo() + " registrado (sin stock: ingrese un lote).");
    }

    private void registrarProveedor() {
        System.out.print("CUIT: ");
        String cuit = scanner.nextLine().trim();
        System.out.print("Razón social: ");
        String razon = scanner.nextLine().trim();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();
        gestorStock.registrarProveedor(cuit, razon, direccion, email, telefono);
        System.out.println("Proveedor registrado.");
    }

    private void catalogoVacunas() {
        System.out.println("Catálogo:");
        for (Vacuna v : gestorStock.getVacunas()) {
            System.out.println("  " + v.getNombre() + " (" + v.getLaboratorio() + ") - especies " + v.getEspeciesAplicables()
                    + " - edad mín. " + v.getEdadMinMeses() + " meses - refuerzo cada " + v.getIntervaloRefuerzoDias() + " días");
        }
        System.out.print("¿Agregar una vacuna? (s/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) return;

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Laboratorio: ");
        String lab = scanner.nextLine().trim();
        List<Especie> especies = new ArrayList<>();
        for (Especie e : Especie.values()) {
            System.out.print("¿Se aplica a " + e + "? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) especies.add(e);
        }
        int edad = leerEntero("Edad mínima (meses): ");
        int intervalo = leerEntero("Intervalo de refuerzo (días): ");
        double costo = leerDecimal("Costo: ");
        double precio = leerDecimal("Precio de venta: ");
        gestorStock.registrarVacuna(nombre, lab, especies, edad, intervalo, costo, precio);
        System.out.println("Vacuna agregada al catálogo.");
    }

    // Auxiliares

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
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
}
