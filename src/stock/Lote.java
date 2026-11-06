package stock;

import java.time.LocalDate;

public class Lote implements Comparable<Lote> {
    private String numeroLote;
    private LocalDate fechaIngreso;
    private LocalDate fechaVto;
    private int cantidadInicial;
    private int stockLote;

    // Lote nuevo: el stock arranca igual a la cantidad recibida
    public Lote(String numeroLote, LocalDate fechaIngreso, LocalDate fechaVto, int cantidadInicial) {
        this(numeroLote, fechaIngreso, fechaVto, cantidadInicial, cantidadInicial);
    }

    // Lote cargado desde archivo: respeta el stock guardado
    public Lote(String numeroLote, LocalDate fechaIngreso, LocalDate fechaVto,
                int cantidadInicial, int stockLote) {
        if (cantidadInicial <= 0) {
            throw new IllegalArgumentException("La cantidad del lote debe ser mayor a 0");
        }
        this.numeroLote = numeroLote;
        this.fechaIngreso = fechaIngreso;
        this.fechaVto = fechaVto;
        this.cantidadInicial = cantidadInicial;
        this.stockLote = stockLote;
    }

    // Un lote vencido no puede usarse (regla de negocio)
    public boolean estaVencido() {
        return estaVencido(LocalDate.now());
    }

    // Versión con fecha, para poder probarlo en los tests
    public boolean estaVencido(LocalDate hoy) {
        return hoy.isAfter(fechaVto);
    }

    public void descontarCantidad(int cantidad) {
        if (cantidad <= 0 || cantidad > stockLote) {
            throw new IllegalArgumentException("Cantidad inválida para el lote " + numeroLote);
        }
        stockLote -= cantidad;
    }

    // Orden natural: primero el que vence antes (FIFO por vencimiento)
    @Override
    public int compareTo(Lote otro) {
        return this.fechaVto.compareTo(otro.fechaVto);
    }

    public String getNumeroLote() { return numeroLote; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public LocalDate getFechaVto() { return fechaVto; }
    public int getCantidadInicial() { return cantidadInicial; }
    public int getStockLote() { return stockLote; }

    // Formato: codigoInsumo;numeroLote;fechaIngreso;fechaVto;cantidadInicial;stockLote
    public String toLinea(String codigoInsumo) {
        return codigoInsumo + ";" + numeroLote + ";" + fechaIngreso + ";" + fechaVto + ";"
                + cantidadInicial + ";" + stockLote;
    }
}
