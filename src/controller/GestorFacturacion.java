package controller;

import model.enums.MetodoPago;
import model.enums.TipoAtencion;
import model.facturacion.Factura;
import model.facturacion.ImpresionFactura;
import model.personas.Duenio;
import model.stock.Insumo;

import java.util.List;

/**
 * Controlador de facturación (CU04). Arma la factura paso a paso a pedido de la vista;
 * el cálculo de descuentos y recargos lo resuelve Factura con el patrón Strategy (CU05).
 */
public class GestorFacturacion {
    private final GestorClientes gestorClientes;
    private final List<Insumo> insumos;

    public GestorFacturacion(GestorClientes gestorClientes, List<Insumo> insumos) {
        this.gestorClientes = gestorClientes;
        this.insumos = insumos;
    }

    public Duenio buscarCliente(String dni) {
        return gestorClientes.buscarDuenio(dni);   // delega: no duplica la búsqueda
    }

    public Insumo buscarInsumo(String codigo) {
        for (Insumo i : insumos) {
            if (i.getCodigo().equalsIgnoreCase(codigo.trim())) return i;
        }
        return null;
    }

    public List<Insumo> getInsumos() {
        return insumos;
    }

    /**
     * Paso 1: crea la factura con el cliente y el medio de pago.
     */
    public Factura iniciarFactura(Duenio cliente, MetodoPago metodoPago) {
        if (cliente == null || metodoPago == null) {
            throw new IllegalArgumentException("El cliente y el método de pago son obligatorios.");
        }
        return new Factura(metodoPago, cliente);
    }

    /**
     * Paso 2a: agrega un servicio (consulta general, vacunación o cirugía).
     */
    public void agregarServicioAFactura(Factura factura, TipoAtencion servicio) {
        if (factura != null && servicio != null) {
            factura.agregarLineaFactura(servicio);
        }
    }

    /**
     * Paso 2b: agrega un producto con su cantidad.
     */
    public void agregarInsumoAFactura(Factura factura, Insumo producto, int cantidad) {
        if (factura != null && producto != null && cantidad > 0) {
            factura.agregarLineaFactura(producto, cantidad);
        }
    }

    /**
     * Paso 3: cierra la factura: calcula el subtotal y aplica el descuento o recargo.
     */
    public void finalizarFactura(Factura factura) {
        if (factura != null) {
            factura.cerrarFactura();
        }
    }

    /**
     * Paso 4: imprime el comprobante.
     */
    public void imprimirComprobante(Factura factura) {
        ImpresionFactura.imprimirFactura(factura);
    }


}