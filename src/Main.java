import controller.FacturacionController;
import controller.GestorAtencion;
import repositorios.HistoriaClinicaRepository;
import view.VistaRecepcionista.MenuRecepcionista;
import view.VistaVeterinario;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=========================================");
            System.out.println("   CLÍNICA VETERINARIA \"PATITAS\"");
            System.out.println("=========================================");
            System.out.println("1. Panel de Recepción");
            System.out.println("2. Panel de Atención Médica (Veterinario)");
            System.out.println("0. Salir del Sistema");
            System.out.println("=========================================");
            System.out.print("Seleccione un perfil/rol: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());

                switch (opcion) {
                    case 1 -> {
                        MenuRecepcionista menuRecep = new MenuRecepcionista();
                        menuRecep.mostrarMenuRecepcionista();
                    }
                    case 2 -> {
                        // 1. Creamos los dos argumentos que pide GestorAtencion
                        HistoriaClinicaRepository historiaRepo = new HistoriaClinicaRepository();
                        FacturacionController facturacionController = new FacturacionController();

                        // 2. Se los pasamos al crear el Gestor
                        GestorAtencion gestor = new GestorAtencion(historiaRepo, facturacionController);

                        // 3. Abrimos la vista
                        VistaVeterinario vistaVet = new VistaVeterinario(gestor);
                        vistaVet.mostrarMenu();
                    }
                    case 0 -> System.out.println("Cerrando el sistema. ¡Hasta luego!");
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}