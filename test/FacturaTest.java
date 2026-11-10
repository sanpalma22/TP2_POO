import model.enums.MetodoPago;
import model.enums.TipoAtencion;
import model.facturacion.Factura;
import model.personas.Duenio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Consulta general = $15.000
class FacturaTest {

    private double totalDe(MetodoPago pago, boolean planSalud) {
        Duenio cliente = new Duenio("Ana", "Paz", "111", "115", "a@a.com", planSalud, 0);
        Factura f = new Factura(pago, cliente);
        f.agregarLineaFactura(TipoAtencion.CONSULTA_GRAL);
        f.cerrarFactura();
        assertEquals(15000.0, f.getSubtotal(), 0.01);
        return f.getTotal();
    }

    @Test
    void efectivoTiene10PorCientoDeDescuento() {
        assertEquals(13500.0, totalDe(MetodoPago.EFECTIVO, false), 0.01);
    }

    @Test
    void debitoNoTieneDescuentoNiRecargo() {
        assertEquals(15000.0, totalDe(MetodoPago.DEBITO, false), 0.01);
    }

    @Test
    void creditoTiene8PorCientoDeRecargo() {
        assertEquals(16200.0, totalDe(MetodoPago.CREDITO, false), 0.01);
    }

    @Test
    void planDeSaludTiene15PorCientoDeDescuento() {
        assertEquals(12750.0, totalDe(MetodoPago.DEBITO, true), 0.01);
    }

    @Test
    void planYEfectivoNoSeAcumulanSeAplicaElMayor() {
        assertEquals(12750.0, totalDe(MetodoPago.EFECTIVO, true), 0.01);   // 15 %, no 25 %
    }

    @Test
    void sumaServiciosEInsumosEnElSubtotal() {
        Duenio cliente = new Duenio("Ana", "Paz", "111", "115", "a@a.com", false, 0);
        Factura f = new Factura(MetodoPago.DEBITO, cliente);
        f.agregarLineaFactura(TipoAtencion.CONSULTA_GRAL);   // 15000
        f.agregarLineaFactura(TipoAtencion.VACUNACION);      // 15000
        f.cerrarFactura();
        assertEquals(30000.0, f.getSubtotal(), 0.01);
        assertEquals(2, f.getLineasFactura().size());
    }
}
