import model.mascotas.Mascota;
import model.personas.Duenio;
import model.personas.Veterinario;
import model.stock.Insumo;
import repositorios.*;
import view.VistaRecepcionista.MenuRecepcionista;
import view.VistaVeterinario;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.println("Cargando el sistema de la Clínica Veterinaria Patitas...\n");

        HistoriaClinicaRepository histClinRepo=new HistoriaClinicaRepository();
        DuenioRepository duenioRepo = new DuenioRepository();
        MascotaRepository mascotaRepo = new MascotaRepository();
        InsumoRepository insumoRepo = new InsumoRepository();

        // 2. Cargar datos persistidos desde los archivos .txt
        List<Duenio> clientes = duenioRepo.cargarTodos();
        List<Insumo> inventarioInsumos = insumoRepo.cargarTodos();
        //List<HistoriaClinica> historias=histClinRepo.

        // Crear mapa DNI -> Duenio para asociar mascotas a sus dueños
        Map<String, Duenio> dueniosPorDni = new HashMap<>();
        for (Duenio d : clientes) {
            dueniosPorDni.put(d.getDocumento(), d);
        }

        // Cargar mascotas vinculadas a los dueños
        List<Mascota> listaMascotas = mascotaRepo.cargarTodas(dueniosPorDni);

        // Crear mapa idMascota -> Mascota
        Map<Integer, Mascota> mascotasPorId = new HashMap<>();
        for (Mascota m : listaMascotas) {
            mascotasPorId.put(m.getIdMascota(), m);
        }

        // Simular/cargar lista de veterinarios registrados
//        List<Veterinario> veterinarios = obtenerVeterinariosBase();
//        Map<String, Veterinario> vetsPorDni = new HashMap<>();
//        for (Veterinario v : veterinarios) {
//            vetsPorDni.put(v.getDocumento(), v);
//        }



        System.out.println("Quien usa el sistema");
        System.out.println("1. Recepcionista");
        System.out.println("2. Veterinario");
        int op= sc.nextInt();
        sc.nextLine(); // Limpiar el buffer del Scanner

        switch (op) {
            case 1 -> {
                // Iniciar panel de recepción pasando los datos cargados
                MenuRecepcionista menuRecepcionista = new MenuRecepcionista();
                menuRecepcionista.mostrarMenuRecepcionista();
            }
            case 2 -> {
                // Iniciar panel o gestión de turnos/atención del veterinario
                System.out.println("\n--- ACCESO VETERINARIO ---");
                System.out.print("Ingrese DNI/Matrícula del Veterinario: ");
                String dniVet = sc.nextLine().trim();

//                Veterinario vetLogueado = vetsPorDni.get(dniVet);
//                if (vetLogueado != null) {
//                    System.out.println("Bienvenido/a Dr/a. " + vetLogueado.getNombre() + " " + vetLogueado.getApellido());
//                    VistaVeterinario vistaVet=new VistaVeterinario(vetLogueado);
//                } else {
//                    System.out.println("Error: No se encontró ningún profesional registrado con ese documento.");
//                }
            }
            case 0 -> System.out.println("Saliendo del sistema...");
            default -> System.out.println("Opción no válida.");
        }

    }

}