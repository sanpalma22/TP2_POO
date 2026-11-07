package view;

import controller.GestorAtencion;
import java.util.Scanner;
import model.atencion.Consulta;
import model.mascotas.Mascota;
import model.personas.Veterinario;
import model.stock.Insumo;

public class VistaVeterinario {
    private GestorAtencion gestor;
    private Scanner scanner;

    public VistaVeterinario(GestorAtencion gestor) {
        this.gestor = gestor;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcion = -1;

        Consulta consultaActual = null;
        Mascota mascotaActual = null;
        Veterinario vetActual = null;
        Insumo insumoActual = null;

        while (opcion != 0) {
            System.out.println("\n=== MENÚ ATENCIÓN VETERINARIA ===");
            System.out.println("1. Iniciar nueva consulta (Pesar mascota)");
            System.out.println("2. Registrar diagnóstico");
            System.out.println("3. Indicar tratamiento");
            System.out.println("4. Aplicar vacuna");
            System.out.println("5. Finalizar consulta (Descontar stock)");
            System.out.println("0. Volver");
            System.out.print("Elija una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    System.out.println("\n-- Iniciando consulta --");
                    System.out.print("Ingrese motivo de la consulta: ");
                    String motivo = scanner.nextLine();
                    System.out.print("Ingrese peso de la mascota (kg): ");
                    int peso = scanner.nextInt();
                    scanner.nextLine();

                    // CONEXIÓN CON EL CONTROLADOR
                    consultaActual = gestor.iniciarConsulta(null, motivo, peso, mascotaActual);
                    System.out.println("Consulta iniciada con éxito y conectada al gestor.");
                    break;

                case 2:
                    System.out.println("\n-- Registrando diagnóstico --");
                    System.out.print("Ingrese diagnóstico: ");
                    String diag = scanner.nextLine();
                    System.out.print("Ingrese observaciones: ");
                    String obs = scanner.nextLine();


                    gestor.registrarDiagnostico(consultaActual, vetActual, diag, obs);
                    break;

                case 3:
                    System.out.println("\n-- Indicando tratamiento --");
                    System.out.print("Ingrese dosis: ");
                    String dosis = scanner.nextLine();
                    System.out.print("Ingrese frecuencia: ");
                    String frecuencia = scanner.nextLine();
                    System.out.print("Ingrese duración: ");
                    String duracion = scanner.nextLine();


                    gestor.indicarTratamiento(consultaActual, insumoActual, dosis, frecuencia, duracion);
                    break;

                case 4:
                    System.out.println("\n-- Aplicando vacuna --");

                    gestor.registrarVacuna(consultaActual, null);
                    break;

                case 5:
                    System.out.println("\n-- Finalizando consulta --");

                    gestor.finalizarConsulta(consultaActual);
                    break;

                case 0:
                    System.out.println("Saliendo del menú...");
                    break;

                default:
                    System.out.println("Opción incorrecta.");
            }
        }
    }
}