package facturacion;

import enums.TipoAtencion;
import stock.Insumo;

public class DetalleFactura {
    private int nro;
    private TipoAtencion servicio;
    private Insumo producto;
    private int cantidad;
    private double precioUnitario;
    private double importe;

    public DetalleFactura(int nro, TipoAtencion servicio) {
        this.nro = nro;
        this.servicio = servicio;
        this.producto = null;
        this.precioUnitario=servicio.getCosto();
        this.cantidad=1;
        this.importe = servicio.getCosto();
    }

    public DetalleFactura(int nro, Insumo producto, int cantidad) {
        this.nro = nro;
        this.producto = producto;
        this.servicio = null;
        this.precioUnitario=producto.getPrecioVenta();
        this.cantidad = cantidad;
        this.importe = cantidad*precioUnitario;
    }

    public double getImporte() {
        return importe;
    }
}
