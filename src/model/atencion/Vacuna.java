package model.atencion;

import model.enums.Especie;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Vacuna del catálogo: especies a las que se aplica, edad mínima e intervalo de refuerzo.
 * Es experta en calcular el próximo refuerzo y en validar especie y edad.
 */
public class Vacuna {
    private String nombre;
    private String laboratorio;
    private List<Especie> especiesAplicables;
    private int edadMinMeses;
    private int intervaloRefuerzoDias;
    private double costo;
    private double precioVenta;

    public Vacuna(String nombre, String laboratorio, List<Especie> especiesAplicables,
                  int edadMinMeses, int intervaloRefuerzoDias, double costo, double precioVenta) {
        this.nombre = nombre;
        this.laboratorio = laboratorio;
        this.especiesAplicables = new ArrayList<>(especiesAplicables);
        this.edadMinMeses = edadMinMeses;
        this.intervaloRefuerzoDias = intervaloRefuerzoDias;
        this.costo = costo;
        this.precioVenta = precioVenta;
    }

    // Experto en Información: la vacuna conoce su intervalo
    public LocalDate calcularProximoRefuerzo(LocalDate fechaAplicacion) {
        return fechaAplicacion.plusDays(intervaloRefuerzoDias);
    }

    // CU10: la vacuna tiene que corresponder a la especie
    public boolean aplicaAEspecie(Especie especie) {
        return especiesAplicables.contains(especie);
    }

    // CU10: la mascota tiene que tener la edad mínima
    public boolean cumpleEdadMinima(LocalDate fechaNacimiento, LocalDate fechaAplicacion) {
        long meses = ChronoUnit.MONTHS.between(fechaNacimiento, fechaAplicacion);
        return meses >= edadMinMeses;
    }

    public String getNombre() { return nombre; }
    public String getLaboratorio() { return laboratorio; }
    public List<Especie> getEspeciesAplicables() { return especiesAplicables; }
    public int getEdadMinMeses() { return edadMinMeses; }
    public int getIntervaloRefuerzoDias() { return intervaloRefuerzoDias; }
    public double getCosto() { return costo; }
    public double getPrecioVenta() { return precioVenta; }

    // Formato: nombre;laboratorio;PERRO,GATO;edadMinMeses;intervaloDias;costo;precioVenta
    public String toLinea() {
        List<String> esp = new ArrayList<>();
        for (Especie e : especiesAplicables) esp.add(e.name());
        return nombre + ";" + laboratorio + ";" + String.join(",", esp) + ";"
                + edadMinMeses + ";" + intervaloRefuerzoDias + ";" + costo + ";" + precioVenta;
    }

    // Reconstruye una vacuna desde una línea del archivo
    public static Vacuna desdeLinea(String linea) {
        String[] p = linea.split(";");
        List<Especie> especies = new ArrayList<>();
        for (String e : p[2].split(",")) especies.add(Especie.valueOf(e));
        return new Vacuna(p[0], p[1], especies, Integer.parseInt(p[3]),
                Integer.parseInt(p[4]), Double.parseDouble(p[5]), Double.parseDouble(p[6]));
    }
}
