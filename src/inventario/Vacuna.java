package inventario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Vacuna extends Insumo{
    private int edadMin;
    private int intervaloRefuerzo;
    public Vacuna(String codigo, String nombre, int stockMinimo, int stockActual, double costo, double precioVenta, String proveedor, int edadMin, int intervaloRefuerzo) {
        super(codigo, nombre, stockMinimo, stockActual, costo, precioVenta, proveedor);
        this.edadMin=edadMin;
        this.intervaloRefuerzo=intervaloRefuerzo;
    }

    public LocalDate calcularProximoRefuerzo(LocalDate fechaAplicacion) {
        if (intervaloRefuerzo == 0) {
            return null;
        }
        return fechaAplicacion.plusDays(intervaloRefuerzo);
    }
}
