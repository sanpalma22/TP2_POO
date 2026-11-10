package model.personas;

/**
 * Datos comunes a todo el personal y a los dueños. nombre y apellido son protected
 * para que las subclases los usen al armar su línea de archivo. toLinea() es abstracto:
 * cada tipo de persona sabe cómo guardarse.
 */
public abstract class Persona {
    protected String nombre;
    protected String apellido;
    private String documento;
    private String telefono;

    public Persona (String nombre, String apellido, String documento, String telefono){
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellido() {

        return apellido;
    }

    public void setApellido(String apellido) {

        this.apellido = apellido;
    }

    public String getDocumento() {

        return documento;
    }

    public String getTelefono() {

        return telefono;
    }

    public void setTelefono(String telefono) {

        this.telefono = telefono;
    }

    public abstract String toLinea();
}
