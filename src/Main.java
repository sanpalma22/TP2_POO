import controller.GestorFacturacion;
import controller.GestorAtencion;
import controller.GestorClientes;
import controller.GestorTurnos;
import controller.GestorStock;
import view.VistaAdministrador;
import model.turnos.Turno;
import model.atencion.Vacuna;
import model.mascotas.Mascota;
import model.personas.Duenio;
import model.stock.Insumo;
import model.stock.Proveedor;
import repositorios.*;
import view.VistaRecepcionista.MenuRecepcionista;
import view.VistaVeterinario;
import model.personas.Persona;
import model.personas.Veterinario;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Punto de entrada del sistema. Arma todo en este orden:
 * 1) repositorios, 2) carga de datos desde los .txt, 3) controladores, 4) vistas.
 * Los datos se cargan una sola vez y se comparten, así todas las pantallas
 * trabajan sobre los mismos objetos (un cambio en una se ve en las demás).
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        new File("datos").mkdirs();

        // 1. Repositorios
        ProveedorRepository proveedorRepo = new ProveedorRepository();
        InsumoRepository insumoRepo = new InsumoRepository();
        DuenioRepository duenioRepo = new DuenioRepository();
        MascotaRepository mascotaRepo = new MascotaRepository();
        HistoriaClinicaRepository historiaRepo = new HistoriaClinicaRepository();
        VacunaRepository vacunaRepo = new VacunaRepository();
        PersonalRepository personalRepo = new PersonalRepository();
        TurnoRepository turnoRepo = new TurnoRepository();

        // 2. Carga (si A guarda el id de B, B se carga antes)
        List<Proveedor> proveedores = proveedorRepo.cargarTodos();
        Map<String, Proveedor> proveedoresPorCuit = new HashMap<>();
        for (Proveedor p : proveedores) proveedoresPorCuit.put(p.getCuit(), p);

        List<Insumo> insumos = insumoRepo.cargarTodos(proveedoresPorCuit);
        Map<String, Insumo> insumosPorCodigo = new HashMap<>();
        for (Insumo i : insumos) insumosPorCodigo.put(i.getCodigo(), i);

        // Catálogo de vacunas (antes que las historias: las vacunas aplicadas guardan el nombre)
        List<Vacuna> vacunas = vacunaRepo.cargarTodas();
        Map<String, Vacuna> vacunasPorNombre = new HashMap<>();
        for (Vacuna v : vacunas) vacunasPorNombre.put(v.getNombre(), v);

        // Personal + horarios
        List<Persona> personal = personalRepo.cargarTodos();
        List<Veterinario> veterinarios = new ArrayList<>();
        for (Persona p : personal) {
            if (p instanceof Veterinario v) veterinarios.add(v);
        }

        List<Duenio> duenios = duenioRepo.cargarTodos();
        Map<String, Duenio> dueniosPorDni = new HashMap<>();
        for (Duenio d : duenios) dueniosPorDni.put(d.getDocumento(), d);

        List<Mascota> mascotas = mascotaRepo.cargarTodas(dueniosPorDni);

        for (Mascota m : mascotas) {
            historiaRepo.cargar(m, insumosPorCodigo, vacunasPorNombre);
        }

        // Turnos (necesitan mascota y veterinario)
        Map<Integer, Mascota> mascotasPorId = new HashMap<>();
        for (Mascota m : mascotas) mascotasPorId.put(m.getIdMascota(), m);
        Map<String, Veterinario> veterinariosPorDocumento = new HashMap<>();
        for (Veterinario v : veterinarios) veterinariosPorDocumento.put(v.getDocumento(), v);
        List<Turno> turnos = turnoRepo.cargarTodos(mascotasPorId, veterinariosPorDocumento);

        // 3. Controladores
        GestorClientes gestorClientes = new GestorClientes(duenios, mascotas, duenioRepo, mascotaRepo);
        GestorFacturacion facturacion = new GestorFacturacion(gestorClientes, insumos);
        GestorAtencion gestorAtencion = new GestorAtencion(historiaRepo, insumoRepo, gestorClientes,
                insumos, vacunas, veterinarios);
        GestorTurnos gestorTurnos = new GestorTurnos(turnos, turnoRepo, gestorClientes, veterinarios);
        GestorStock gestorStock = new GestorStock(proveedores, insumos, vacunas, proveedorRepo, insumoRepo, vacunaRepo);

        // 4. Vistas
        MenuRecepcionista menuRecepcion = new MenuRecepcionista(scanner, gestorClientes, facturacion, gestorTurnos);
        VistaVeterinario vistaVeterinario = new VistaVeterinario(gestorAtencion, scanner);
        VistaAdministrador vistaAdministrador = new VistaAdministrador(scanner, gestorStock, gestorTurnos, gestorAtencion);

        // 5. Menú de roles
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== CLÍNICA VETERINARIA \"PATITAS\" ===");
            System.out.println("1. Recepción");
            System.out.println("2. Veterinario");
            System.out.println("3. Administrador");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                switch (opcion) {
                    case 1 -> menuRecepcion.mostrarMenuRecepcionista();
                    case 2 -> vistaVeterinario.mostrarMenu();
                    case 3 -> vistaAdministrador.mostrarMenu();
                    case 0 -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            } finally {
                System.out.println("-----------------------------------------");
            }
        }
        scanner.close();
    }
}