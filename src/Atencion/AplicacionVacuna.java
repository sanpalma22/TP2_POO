package Atencion;

import java.time.LocalDate;

public class AplicacionVacuna {
    private Vacuna vacuna;
    private LocalDate fecha;
    private LocalDate fechaRefuerzo;
    private String lote;

    // Para una aplicación nueva: calcula el refuerzo
    public AplicacionVacuna(Vacuna vacuna, LocalDate fecha, String lote) {
        this(vacuna, fecha, vacuna.calcularProximoRefuerzo(fecha), lote);
    }

    // Para cargar desde el archivo: usa el refuerzo guardado
    public AplicacionVacuna(Vacuna vacuna, LocalDate fecha, LocalDate fechaRefuerzo, String lote) {
        this.vacuna = vacuna;
        this.fecha = fecha;
        this.fechaRefuerzo = fechaRefuerzo;
        this.lote = lote;
    }

    // Recordatorio: 7 días antes del refuerzo (CU08)
    public boolean requiereRecordatorio(LocalDate hoy) {
        return !hoy.isBefore(fechaRefuerzo.minusDays(7)) && !estaVencida(hoy);
    }

    // Vencida: pasaron más de 30 días del refuerzo sin aplicarlo
    public boolean estaVencida(LocalDate hoy) {
        return hoy.isAfter(fechaRefuerzo.plusDays(30));
    }

    public Vacuna getVacuna() { return vacuna; }
    public LocalDate getFecha() { return fecha; }
    public LocalDate getFechaRefuerzo() { return fechaRefuerzo; }
    public String getLote() { return lote; }

    // Formato: idConsulta;nombreVacuna;fecha;fechaRefuerzo;lote
    public String toLinea(int idConsulta) {
        return idConsulta + ";" + vacuna.getNombre() + ";" + fecha + ";"
                + fechaRefuerzo + ";" + lote;
    }
}
