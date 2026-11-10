package controller;

import model.mascotas.Mascota;
import model.personas.Duenio;
import repositorios.DuenioRepository;
import repositorios.MascotaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de dueños y mascotas: registro (CU09) y transferencia (CU12).
 * Mantiene las listas en memoria y las guarda en archivo después de cada cambio.
 */
public class GestorClientes {
    private final List<Duenio> duenios;
    private final List<Mascota> mascotas;
    private final DuenioRepository duenioRepo;
    private final MascotaRepository mascotaRepo;

    public GestorClientes(List<Duenio> duenios, List<Mascota> mascotas, DuenioRepository duenioRepo, MascotaRepository mascotaRepo) {
        this.duenios = duenios;
        this.mascotas = mascotas;
        this.duenioRepo = duenioRepo;
        this.mascotaRepo = mascotaRepo;
    }
    public Duenio buscarDuenio(String dni){
        for (Duenio d : duenios){
            if (d.getDocumento().equals(dni)){
                return d;
            }
        }
        return null;
    }

    public void registrarDuenio(String nombre, String apellido, String dni, String telefono, String email, boolean planSalud){
        if (buscarDuenio(dni) != null){
            throw new IllegalArgumentException("Ya existe un cliente con el DNI " + dni);
        }
        else {
            Duenio d = new Duenio(nombre, apellido, dni, telefono, email, planSalud, 0);
            duenios.add(d);
            duenioRepo.guardar(d);
        }
    }

    public Mascota buscarMascota(int id){
        for (Mascota m : mascotas){
            if (m.getIdMascota() == id){
                return m;
            }
        }
        return null;
    }

    public void registrarMascota(Mascota m){
        mascotas.add(m);
        mascotaRepo.guardar(m);
    }

    public List<Mascota> mascotasDe (Duenio d){
        List<Mascota> filtradas = new ArrayList<Mascota>();
        for (Mascota m : mascotas){
            if (m.getDuenio().getDocumento().equals(d.getDocumento())){
                filtradas.add(m);
            }
        }
        return filtradas;
    }

    public void transferirMascota(Mascota m, Duenio nuevo){
        m.cambiarDuenio(nuevo);
        mascotaRepo.guardarTodas(mascotas);
    }

    // Reescribe duenios.txt (por ejemplo, cuando cambian las ausencias)
    public void guardarDuenios() {
        duenioRepo.guardarTodos(duenios);
    }

    // Reescribe mascotas.txt (por ejemplo, cuando cambia el peso en una consulta)
    public void guardarMascotas() {
        mascotaRepo.guardarTodas(mascotas);
    }

    public List<Duenio> getDuenios() {
        return duenios;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }
}
