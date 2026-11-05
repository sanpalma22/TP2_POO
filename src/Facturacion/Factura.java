package Facturacion;

import enums.MetodoPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String numero;
    private static int contFacturas;
    private LocalDate fecha;
    private List<DetalleFactura> lineasFactura;
    //private List<AplicacionVacunas> vacunasAplicadas;
    private MetodoPago metodoPago;
    private double subtotal;
    private double dtoAplicado;
    private double cargoAplicado;
    private double total;
    private Duenio cliente;

    public Factura(String numero, LocalDate fecha, List<DetalleFactura> lineasFactura, MetodoPago metodoPago, double dtoAplicado, double cargoAplicado, double total, Duenio cliente) {
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

    }
    private void aplicarDtoRecargo(EstrategiaDtoRec estrategia){
        double montoFinal = estrategia.calcularMontoFinal(subtotal);
        if(montoFinal<subtotal){
            this.dtoAplicado=subtotal-montoFinal;
        } else if (montoFinal>subtotal) {
            this.cargoAplicado=montoFinal-subtotal;
        }
        this.total=montoFinal;
    }

    public void imprimirFactura(){
        System.out.println("Nro de factura: " + numero);
        System.out.println("Fecha: "+fecha);
        System.out.println("Cliente: "+cliente.nombre + " | " + cliente.documento);
        System.out.println("Metodo de pago: "+metodoPago.getNombre());
        //Lineas factura
        System.out.println();
    }
}
