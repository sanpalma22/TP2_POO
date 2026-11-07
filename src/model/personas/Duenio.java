package model.personas;

import model.mascotas.Mascota;

import java.util.ArrayList;
import java.util.List;

public class Duenio extends Persona {
    private String email;
    private boolean planSalud;
    private int ausenciasConsecutivas;
    public Duenio(String nombre, String apellido, String documento, String telefono, String email, boolean planSalud, int ausenciasConsecutivas){
        super(nombre, apellido, documento, telefono);
        this.email = email;
        this.planSalud = planSalud;
        this.ausenciasConsecutivas = ausenciasConsecutivas;
    }

    public void registrarAusencia(){
        ausenciasConsecutivas++;
    }

    public void registrarAsistencia(){
        ausenciasConsecutivas = 0;
    }

    public boolean debePagarSenia(){
        return ausenciasConsecutivas >= 3;
    }

    public String getEmail(){
        return  email;
    }

    public boolean tienePlanSalud(){
        return planSalud;
    }

    public String toLinea(){
        return getDocumento() + ";" + nombre + ";" + apellido + ";" +getTelefono() + ";" + email + ";" +planSalud + ";" +ausenciasConsecutivas;
    }
}
