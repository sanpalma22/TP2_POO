package facturacion;

import atencion.AplicacionVacuna;
import enums.MetodoPago;
import enums.TipoAtencion;
import personas.Duenio;
import stock.Insumo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String numero;
    private static int contFacturas = 1;
    private LocalDate fecha;
    private ArrayList<DetalleFactura> lineasFactura;
    private MetodoPago metodoPago;
    private double subtotal;
    private double dtoAplicado;
    private double cargoAplicado;
    private double total;
    private Duenio cliente;

    public Factura(MetodoPago metodoPago, Duenio cliente) {
        this.numero = generarNroFactura();
        this.fecha = LocalDate.now();
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        this.lineasFactura = new ArrayList<>();
    }

    public void agregarLineaFactura(TipoAtencion servicio) {
        if (servicio != null) {
            int nroItem = this.lineasFactura.size() + 1;
            DetalleFactura detalle = new DetalleFactura(nroItem, servicio);
            this.lineasFactura.add(detalle);
        }
    }

    public void agregarLineaFactura(Insumo producto, int cantidad) {
        if (producto != null && cantidad > 0) {
            int nroItem = this.lineasFactura.size() + 1;
            DetalleFactura detalle = new DetalleFactura(nroItem, producto, cantidad);
            this.lineasFactura.add(detalle);
        }
    }
    private String generarNroFactura(){
        String nro=String.format("%05d",contFacturas);
        contFacturas++;
        return nro;
    }
    private void subtotal(){
        this.subtotal=0;
        for (DetalleFactura detalle: lineasFactura){
            this.subtotal+=detalle.getImporte();
        }
    }
    private void aplicarDtoRecargo(){
        EstrategiaDtoRec estrategia;
        if (cliente.tienePlanSalud()) {
            estrategia=new DtoPlanSalud();
        } else if (metodoPago == MetodoPago.EFECTIVO) {
            estrategia = new DtoEfectivo();
        } else if (metodoPago == MetodoPago.CREDITO) {
            estrategia = new RecargoCredito();
        }else estrategia=null;
        if(estrategia!=null) {
            this.total = estrategia.calcularMontoFinal(subtotal);
            if (total < subtotal) {
                this.dtoAplicado = subtotal - total;
            } else if (total > subtotal) {
                this.cargoAplicado = total - subtotal;
            }
        }
    }
    public void cerrarFactura(){
        subtotal();
        aplicarDtoRecargo();
    }

    public Duenio getCliente() {
        return cliente;
    }

    public double getTotal() {
        return total;
    }

    public double getCargoAplicado() {
        return cargoAplicado;
    }

    public double getDtoAplicado() {
        return dtoAplicado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public ArrayList<DetalleFactura> getLineasFactura() {
        return lineasFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public static int getContFacturas() {
        return contFacturas;
    }

    public String getNumero() {
        return numero;
    }
}
