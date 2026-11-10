package model.personas;

/**
 * Administrador de la clínica: consulta reportes y gestiona stock y catálogo.
 */
public class Administrador extends Persona{
    public Administrador(String nombre, String apellido, String documento, String telefono){
        super(nombre, apellido, documento, telefono);
    }

    @Override
    public String toLinea() {
        return "ADMINISTRADOR;" + getDocumento() + ";" + nombre + ";" + apellido + ";" + getTelefono() + ";;";
    }
}
