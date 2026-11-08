package controller;

import excepciones.SeniaRequeridaException;
import excepciones.TurnoSuperpuestoException;
import model.enums.TipoAtencion;
import model.mascotas.Mascota;
import model.personas.Veterinario;
import model.turnos.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GestorTurnos {
    private List<Turno> turnos = new ArrayList<>();

    public Turno asignarTurno(Mascota mascota, Veterinario veterinario, LocalDate fecha, LocalTime hora, TipoAtencion tipo) throws SeniaRequeridaException, TurnoSuperpuestoException {

        if (mascota.getDuenio().debePagarSenia()) {
            throw new SeniaRequeridaException("El dueño tiene 3 ausencias. Debe pagar seña para el nuevo turno.");
        }


        if (!veterinario.horarioDisponible(fecha, hora, tipo)) {
            throw new TurnoSuperpuestoException("El veterinario no atiende en ese horario o no le alcanza el tiempo.");
        }

        Turno nuevo = new Turno(mascota, veterinario, fecha, hora, tipo, false);


        for (Turno t : turnos) {
            if (nuevo.seSuperponeCon(t)) {
                throw new TurnoSuperpuestoException("El veterinario ya tiene un turno en ese horario.");
            }
        }

        turnos.add(nuevo);
        System.out.println("Turno asignado exitosamente.");
        return nuevo;
    }
}