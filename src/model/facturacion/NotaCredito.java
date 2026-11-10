package model.facturacion;

import java.time.LocalDate;

/**
 * Anula una factura sin modificarla, porque una factura emitida no se cambia (CU14).
 */
public class NotaCredito {
    private String numeroFactura;
    private LocalDate fecha;
    private String motivo;
    private double monto;

    public NotaCredito(String numeroFactura, LocalDate fecha, String motivo, double monto) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.motivo = motivo;
        this.monto = monto;
    }

    public String getNumeroFactura() { return numeroFactura; }
    public LocalDate getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public double getMonto() { return monto; }
}