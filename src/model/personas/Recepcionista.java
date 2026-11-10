package model.personas;

/**
 * Recepcionista: registra clientes y mascotas, da turnos y factura.
 */
public class Recepcionista extends Persona{
    public Recepcionista(String nombre, String apellido, String documento, String telefono){
        super(nombre, apellido, documento, telefono);
    }

    @Override
    public String toLinea() {
        return "RECEPCIONISTA;" + getDocumento() + ";" + nombre + ";" + apellido + ";" + getTelefono() + ";;";
    }
}
