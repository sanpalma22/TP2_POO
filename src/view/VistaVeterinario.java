package view;

import controller.GestorAtencion;
import java.util.Scanner;

public class VistaVeterinario {
    private GestorAtencion gestor;
    private Scanner scanner;

    public VistaVeterinario(GestorAtencion gestor) {
        this.gestor = gestor;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\nMenu de atención veterinaria. Elija un número:");
            System.out.println("1. Iniciar nueva consulta");
            System.out.println("2. Registrar diagnóstico");
            System.out.println("3. Indicar tratamiento y medicamentos");
            System.out.println("4. Aplicar vacuna");
            System.out.println("5. Finalizar consulta");
            System.out.println("0. Volver");
            System.out.print("Elija una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1: System.out.println("\n-- Iniciando consulta --"); break;
                case 2: System.out.println("\n-- Registrando diagnóstico --"); break;
                case 3: System.out.println("\n-- Indicando tratamiento --"); break;
                case 4: System.out.println("\n-- Aplicando vacuna --"); break;
                case 5: System.out.println("\n-- Finalizando consulta --"); break;
                case 0: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción incorrecta.");
            }
        }
    }
}