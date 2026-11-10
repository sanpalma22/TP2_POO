package view.VistaRecepcionista;

import controller.GestorClientes;
import model.enums.InteriorExterior;
import model.enums.Sexo;
import model.enums.Tamano;
import model.mascotas.*;
import model.personas.Duenio;
import repositorios.DuenioRepository;
import repositorios.MascotaRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Pantalla de alta de dueños y mascotas (CU09). Pide los datos y delega en GestorClientes.
 */
public class RegistroView {
    private final Scanner scanner;
    private GestorClientes gestor;

    public RegistroView(Scanner scanner, GestorClientes gestor) {
        this.scanner = scanner;
        this.gestor = gestor;
    }

    /**
     * Registrar un nuevo cliente (Dueño) en el sistema y guardarlo en el .txt
     */
    public void registrarCliente() {
        System.out.println("\n=========================================");
        System.out.println("          REGISTRO DE CLIENTE            ");
        System.out.println("=========================================");

        System.out.print("Ingrese DNI/Documento: ");
        String dni = scanner.nextLine().trim();

        System.out.print("Ingrese Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Ingrese Apellido: ");
        String apellido = scanner.nextLine().trim();

        System.out.print("Ingrese Teléfono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Ingrese Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("¿Posee Plan de Salud Animal? (s/n): ");
        boolean planSalud = scanner.nextLine().trim().equalsIgnoreCase("s");

        try {
            gestor.registrarDuenio(nombre, apellido, dni, telefono, email, planSalud);
            System.out.println("Cliente " + nombre + " " + apellido + " registrado con éxito.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Registrar una nueva mascota vinculada a un Dueño por DNI
     */
    public void registrarMascota() {
        System.out.println("\n=========================================");
        System.out.println("          REGISTRO DE MASCOTA            ");
        System.out.println("=========================================");

        System.out.print("Ingrese el DNI del dueño responsable: ");
        String dni = scanner.nextLine().trim();

        Duenio duenio = gestor.buscarDuenio(dni);

        if (duenio == null) {
            System.out.println("Error: No se encontró ningún cliente registrado con el DNI " + dni);
            return;
        }

        System.out.println("Dueño encontrado: " + duenio.getNombre() + " " + duenio.getApellido());

        System.out.print("Nombre de la mascota: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Raza: ");
        String raza = scanner.nextLine().trim();

        System.out.print("Sexo (1. MACHO / 2. HEMBRA): ");
        int opSexo = Integer.parseInt(scanner.nextLine().trim());
        Sexo sexo = (opSexo == 2) ? Sexo.HEMBRA : Sexo.MACHO;

        LocalDate fechaNacimiento = null;
        try {
            System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
            fechaNacimiento = LocalDate.parse(scanner.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha inválida. Se asignará la fecha actual por defecto.");
            fechaNacimiento = LocalDate.now();
        }

        System.out.print("Peso actual (en kg): ");
        int peso = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("\nSeleccione la especie:");
        System.out.println("1. Perro");
        System.out.println("2. Gato");
        System.out.println("3. Ave");
        System.out.println("4. Conejo");
        System.out.print("Opción: ");
        int especieOp = Integer.parseInt(scanner.nextLine().trim());

        Mascota nuevaMascota = null;

        switch (especieOp) {
            case 1 -> {
                System.out.print("Número de chip: ");
                String chip = scanner.nextLine().trim();
                Tamano tamano = seleccionarTamano();
                nuevaMascota = new Perro(duenio, nombre, raza, sexo, fechaNacimiento, peso, chip, tamano);
            }
            case 2 -> {
                System.out.print("¿Es de INTERIOR o EXTERIOR? (1. Interior / 2. Exterior): ");
                int habOp = Integer.parseInt(scanner.nextLine().trim());
                InteriorExterior hab = (habOp == 2) ? InteriorExterior.EXTERIOR : InteriorExterior.INTERIOR;
                nuevaMascota = new Gato(duenio, nombre, raza, sexo, fechaNacimiento, peso, hab);
            }
            case 3 -> {
                System.out.print("¿Es ave voladora? (s/n): ");
                boolean volador = scanner.nextLine().trim().equalsIgnoreCase("s");
                nuevaMascota = new Ave(duenio, nombre, raza, sexo, fechaNacimiento, peso, volador);
            }
            case 4 -> {
                System.out.print("Número de chip: ");
                String chip = scanner.nextLine().trim();
                Tamano tamano = seleccionarTamano();
                nuevaMascota = new Conejo(duenio, nombre, raza, sexo, fechaNacimiento, peso, chip, tamano);
            }
            default -> System.out.println("Especie no válida.");
        }

        if (nuevaMascota != null) {
            gestor.registrarMascota(nuevaMascota);
            System.out.println("Mascota " + nombre + " registrada y asignada correctamente a " + duenio.getNombre());
        }
    }

    private Tamano seleccionarTamano() {
        System.out.println("Tamaño (1. CHICO / 2. MEDIANO / 3. GRANDE): ");
        int op = Integer.parseInt(scanner.nextLine().trim());
        return switch (op) {
            case 1 -> Tamano.CHICO;
            case 3 -> Tamano.GRANDE;
            default -> Tamano.MEDIANO;
        };
    }
}