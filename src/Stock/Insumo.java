package Stock;

import java.util.ArrayList;
import java.util.List;

public class Insumo {
    private String codigo;
    private String nombre;
    private int stockActual;
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
        this.stockActual = 0;
        this.lotes = new ArrayList<>();
    }

    // CU20: registrar ingreso de lote
    /*public void agregarLote(Lote l) {
        if (l.estaVencido()) {
            throw new IllegalArgumentException("No se puede ingresar un lote vencido");
        }
        lotes.add(l);
        stockActual += l.getStockLote();
    }*/

    // Experto en Información: Stock.Insumo conoce sus lotes
    /*public Lote loteProximoAVencer() {
        return lotes.stream()
                .filter(l -> !l.estaVencido() && l.getStockLote() > 0)
                .min(Comparator.comparing(Lote::getFechaVto))
                .orElse(null);
    }*/

    // CU06: descuenta por FIFO, saltando lotes vencidos
    /*public void descontarStock(int cantidad) throws StockInsuficienteException {
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
        stockActual -= cantidad;
    }*/

    // Solo cuenta lotes no vencidos
    /*public int stockDisponible() {
        int total = 0;
        for (Lote l : lotes) {
            if (!l.estaVencido()) total += l.getStockLote();
        }
        return total;
    }*/

    // Experto en Información: tiene stock actual y mínimo
    public boolean estaEnStockBajo() {
        return stockActual <= stockMinimo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getStockActual() { return stockActual; }
    public int getStockMinimo() { return stockMinimo; }
    public double getCosto() { return costo; }
    public double getPrecioVenta() { return precioVenta; }
    //public Proveedor getProveedor() { return proveedor; }
    //public List<Lote> getLotes() { return lotes; }

    // Formato: codigo;nombre;stockMinimo;costo;precioVenta;cuitProveedor
    /*public String toLinea() {
        return codigo + ";" + nombre + ";" + stockMinimo + ";" + costo + ";"
                + precioVenta + ";" + proveedor.getCuit();
    }*/
}
