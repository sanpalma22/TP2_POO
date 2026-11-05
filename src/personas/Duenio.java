package personas;

import java.util.ArrayList;

public class Duenio extends Persona {
    private String email;
    private PlanSaludAnimal planSalud;
    private List<Mascotas> mascotas;
    public Duenio(String nombre, String apellido, String documento, int telefono, String email, PlanSaludAnimal planSalud){
        super(nombre, apellido, documento, telefono);
        this.email = email;
        this.planSalud = planSalud;
        this.mascotas = new List<Mascotas>();
    }

}
