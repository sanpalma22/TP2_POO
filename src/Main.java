import model.mascotas.Mascota;
import model.personas.Duenio;
import model.stock.Insumo;
import repositorios.*;
import view.VistaRecepcionista.MenuRecepcionista;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cargando el sistema de la Clínica Veterinaria Patitas...\n");

        // 1. Instanciar repositorios
        DuenioRepository duenioRepo = new DuenioRepository();
        MascotaRepository mascotaRepo = new MascotaRepository();
        InsumoRepository insumoRepo = new InsumoRepository();

        // 2. Cargar datos persistidos desde los archivos .txt
        List<Duenio> clientes = duenioRepo.cargarTodos();
        List<Insumo> inventarioInsumos = insumoRepo.cargarTodos();

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

        // 4. Iniciar la vista del Recepcionista
        MenuRecepcionista menuRecepcionista = new MenuRecepcionista();
        menuRecepcionista.mostrarMenuRecepcionista();
    }

    /**
     * Carga inicial de veterinarios de prueba para asignar turnos
     */
//    private static List<Veterinario> obtenerVeterinariosBase() {
//        List<Veterinario> lista = new ArrayList<>();
//        // Asumiendo constructor básico de Veterinario (nombre, apellido, documento, telefono, matricula, especialidad)
//        lista.add(new Veterinario("Juan", "Pérez", "28999111", "1144556677", "MP-1001", "Clínica General"));
//        lista.add(new Veterinario("Laura", "García", "30111222", "1188990011", "MP-2005", "Cirugía"));
//        return lista;
//    }
}