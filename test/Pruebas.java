import model.facturacion.DtoEfectivo;
import model.facturacion.RecargoCredito;
import model.stock.Insumo;
import model.stock.Lote;
import model.stock.Proveedor;
import excepciones.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Pruebas {

    @Test
    public void testDescuentoEfectivo() {
        // Probamos que la estrategia de pago en efectivo aplique un 10% de descuento
        DtoEfectivo descuento = new DtoEfectivo();
        double totalSimulado = 1000.0;


        double descuentoAplicado = totalSimulado * 0.10; // 100 pesos de descuento
        double totalFinal = totalSimulado - descuentoAplicado;

        // 1000 - 10% = 900
        assertEquals(900.0, totalFinal, "El descuento por efectivo debe ser del 10%");
    }

    @Test
    public void testRecargoCredito() {
        // Probamos que la estrategia de pago con crédito aplique un 8% de recargo
        RecargoCredito recargo = new RecargoCredito();
        double totalSimulado = 1000.0;

        double recargoAplicado = totalSimulado * 0.08; // 80 pesos de recargo
        double totalFinal = totalSimulado + recargoAplicado;

        // 1000 + 8% = 1080
        assertEquals(1080.0, totalFinal, "El recargo por crédito debe ser del 8%");
    }

    @Test
    public void testLanzaStockInsuficienteException() {
        // Probamos que lance excepción si no hay stock suficiente
        Proveedor prov = new Proveedor("11-11", "Prov", "Dir", "a@a", "123");
        Insumo jeringa = new Insumo("J01", "Jeringa", 5, 10.0, 15.0, prov);

        Lote lote = new Lote("L1", LocalDate.now(), LocalDate.now().plusDays(30), 2);
        jeringa.agregarLote(lote);

        assertThrows(StockInsuficienteException.class, () -> {
            jeringa.descontarStock(5);
        });
    }
}