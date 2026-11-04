package enums;

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
