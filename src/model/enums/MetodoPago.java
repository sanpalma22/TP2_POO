package model.enums;

/**
 * Medios de pago aceptados. Cada uno puede tener su descuento o recargo en la factura.
 */
public enum MetodoPago {
    EFECTIVO("Efectivo"),
    DEBITO("Debito"),
    CREDITO("Credito");

    private final String nombre;
    MetodoPago(String nombre){this.nombre=nombre;}

    public String getNombre() {
        return nombre;
    }
}
