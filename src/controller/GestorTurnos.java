package controller;

import java.util.ArrayList;
import java.util.List;

public class GestorTurnos {


    private List<Object> turnos;

    public GestorTurnos() {
        this.turnos = new ArrayList<>();
    }


    public Object asignarTurno(Object nuevo) {
        turnos.add(nuevo);
        System.out.println("Turno asignado exitosamente.");
        return nuevo;
    }
}