package view.VistaRecepcionista;

import controller.FacturacionController;
import repositorios.DuenioRepository;
import repositorios.MascotaRepository;

import java.util.Scanner;

public class MenuRecepcionista {
    private final Scanner scanner;

    public MenuRecepcionista() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Menú principal con el switch de navegación
     */
    public void mostrarMenuRecepcionista() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("PANEL DE RECEPCIÓN");
            System.out.println("1. Generar Factura");
            System.out.println("2. Registrar Cliente (Dueño)");
            System.out.println("3. Registrar Mascota");
            System.out.println("0. Salir / Volver al Menú Principal");
            System.out.println("=========================================");
            System.out.print("Seleccione una opción: ");

            try {
                RegistroView registroView = new RegistroView(new DuenioRepository(), new MascotaRepository());
                opcion = Integer.parseInt(scanner.nextLine().trim());

                switch (opcion) {
                    case 1 -> new FacturacionView().generarFactura();
                    case 2 -> registroView.registrarCliente();
                    case 3 -> registroView.registrarMascota();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        }
    }
}

