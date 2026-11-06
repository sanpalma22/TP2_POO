package personas;

import mascotas.Mascota;

import java.util.ArrayList;
import java.util.List;

public class Duenio extends Persona {
    private String email;
    private boolean planSalud;
    private List<Mascota> mascotas;
    public Duenio(String nombre, String apellido, String documento, int telefono, String email, boolean planSalud){
        super(nombre, apellido, documento, telefono);
        this.email = email;
        this.planSalud = planSalud;
        this.mascotas = new ArrayList<>();
    }

}
