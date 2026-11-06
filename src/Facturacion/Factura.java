package Facturacion;

import Atencion.AplicacionVacuna;
import enums.MetodoPago;
import personas.Duenio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String numero;
    private static int contFacturas;
    private LocalDate fecha;
    private List<DetalleFactura> lineasFactura;
    private List<AplicacionVacuna> vacunasAplicadas;
    private MetodoPago metodoPago;
    private double subtotal;
    private double dtoAplicado;
    private double cargoAplicado;
    private double total;
    private Duenio cliente;

    public Factura(String numero, LocalDate fecha, MetodoPago metodoPago, Duenio cliente) {
        this.numero = generarNroFactura();
        contFacturas++;
        this.fecha = LocalDate.now();
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        this.lineasFactura = new ArrayList<>();
        subtotal();

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
    private void aplicarDtoRecargo(EstrategiaDtoRec estrategia){
        this.total = estrategia.calcularMontoFinal(subtotal);
        if(total<subtotal){
            this.dtoAplicado=subtotal-total;
        } else if (total>subtotal) {
            this.cargoAplicado=total-subtotal;
        }
        this.total=montoFinal;
    }

    public void imprimirFactura(){
        System.out.println("Nro de factura: " + numero);
        System.out.println("Fecha: "+fecha);
        System.out.println("Cliente: "+cliente.getNombre() + " | " + cliente.getDocumento());
        System.out.println("Metodo de pago: "+metodoPago.getNombre());
        for (DetalleFactura detalle: lineasFactura){
            this.subtotal+=detalle.getImporte();
        }
        System.out.println();
    }
}
