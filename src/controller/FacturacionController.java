package controller;

import model.enums.MetodoPago;
import model.enums.TipoAtencion;
import model.facturacion.Factura;
import model.facturacion.ImpresionFactura;
import model.personas.Duenio;
import model.stock.Insumo;

public class FacturacionController {

    /**
     * Paso 1: Inicia la factura con el cliente y método de pago seleccionados por el recepcionista.
     */
    public Factura iniciarFactura(Duenio cliente, MetodoPago metodoPago) {
        if (cliente == null || metodoPago == null) {
            throw new IllegalArgumentException("El cliente y el método de pago son obligatorios.");
        }
        return new Factura(metodoPago, cliente);
    }

    /**
     * Paso 2a: Agrega un servicio de atención (Consulta General, Vacunación, Cirugía) desde la View.
     */
    public void agregarServicioAFactura(Factura factura, TipoAtencion servicio) {
        if (factura != null && servicio != null) {
            factura.agregarLineaFactura(servicio);
        }
    }

    /**
     * Paso 2b: Agrega un insumo o vacuna como producto desde la View.
     */
    public void agregarInsumoAFactura(Factura factura, Insumo producto, int cantidad) {
        if (factura != null && producto != null && cantidad > 0) {
            factura.agregarLineaFactura(producto, cantidad);
        }
    }

    /**
     * Paso 3: Finaliza la factura (calcula subtotales, aplica descuentos/recargos) y la guarda en el historial.
     */
    public void finalizarFactura(Factura factura) {
        if (factura != null) {
            factura.cerrarFactura();
        }
    }

    /**
     * Paso 4: Imprime la factura procesada.
     */
    public void imprimirComprobante(Factura factura) {
        ImpresionFactura.imprimirFactura(factura);
    }


}