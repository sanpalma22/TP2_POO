package model.stock;

import excepciones.StockInsuficienteException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Insumo {
    private String codigo;
    private String nombre;
    private int stockMinimo;
    private double costo;
    private double precioVenta;
    private Proveedor proveedor;
    private List<Lote> lotes;

    public Insumo(String codigo, String nombre, int stockMinimo,
                  double costo, double precioVenta, Proveedor proveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stockMinimo = stockMinimo;
        this.costo = costo;
        this.precioVenta = precioVenta;
        this.proveedor = proveedor;
        this.lotes = new ArrayList<>();
    }

    public Insumo(String codigo, String nombre, int stockMinimo, int stockActual, double costo, double precioVenta, String proveedor) {
    }

    // CU20: registrar ingreso de lote
    public void agregarLote(Lote l) {
        if (l.estaVencido()) {
            throw new IllegalArgumentException("No se puede ingresar un lote vencido");
        }
        lotes.add(l);
    }

    //cuando el repository reconstruye un lote que ya estaba guardado. No valida nada, solo lo agrega
    public void cargarLote(Lote l) {
        lotes.add(l);
    }

    // Experto en Información: Stock.Insumo conoce sus lotes
    public Lote loteProximoAVencer() {
        return lotes.stream()
                .filter(l -> !l.estaVencido() && l.getStockLote() > 0)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    // CU06: descuenta por FIFO, saltando lotes vencidos
    public void descontarStock(int cantidad) throws StockInsuficienteException {
        if (cantidad > stockDisponible()) {
            throw new StockInsuficienteException(
                    "Stock insuficiente de " + nombre + ": disponible " + stockDisponible());
        }
        int restante = cantidad;
        while (restante > 0) {
            Lote l = loteProximoAVencer();
            int aDescontar = Math.min(restante, l.getStockLote());
            l.descontarCantidad(aDescontar);
            restante -= aDescontar;
        }
    }

    // Solo cuenta lotes no vencidos
    public int stockDisponible() {
        int total = 0;
        for (Lote l : lotes) {
            if (!l.estaVencido()) total += l.getStockLote();
        }
        return total;
    }
    // Experto en Información: tiene model.stock actual y mínimo
    public boolean estaEnStockBajo() {
        return stockDisponible() <= stockMinimo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getStockActual() { return stockDisponible(); }
    public int getStockMinimo() { return stockMinimo; }
    public double getCosto() { return costo; }
    public double getPrecioVenta() { return precioVenta; }
    public Proveedor getProveedor() { return proveedor; }
    public List<Lote> getLotes() { return lotes; }

    // Formato: codigo;nombre;stockMinimo;costo;precioVenta;cuitProveedor
    public String toLinea() {
        return codigo + ";" + nombre + ";" + stockMinimo + ";" + costo + ";"
                + precioVenta + ";" + proveedor.getCuit();
    }
}
