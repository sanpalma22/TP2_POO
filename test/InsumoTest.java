import excepciones.StockInsuficienteException;
import model.stock.Insumo;
import model.stock.Lote;
import model.stock.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InsumoTest {
    private Insumo amoxicilina;
    private Lote venceAntes;
    private Lote venceDespues;
    private Lote vencido;

    @BeforeEach
    void setUp() {
        LocalDate hoy = LocalDate.now();
        Proveedor proveedor = new Proveedor("30-11111111-1", "Droguería Sur", "Av. 1", "a@a.com", "111");
        amoxicilina = new Insumo("MED001", "Amoxicilina", 10, 800, 1500, proveedor);

        venceAntes = new Lote("L1", hoy.minusDays(30), hoy.plusMonths(2), 50, 20);
        venceDespues = new Lote("L2", hoy.minusDays(10), hoy.plusMonths(6), 50, 50);
        vencido = new Lote("L0", hoy.minusYears(1), hoy.minusDays(1), 30, 30);

        amoxicilina.cargarLote(venceDespues);   // se cargan desordenados a propósito
        amoxicilina.cargarLote(vencido);
        amoxicilina.cargarLote(venceAntes);
    }

    @Test
    void elStockSoloCuentaLotesNoVencidos() {
        assertEquals(70, amoxicilina.getStockActual());   // 20 + 50; el vencido no suma
    }

    @Test
    void descuentaPrimeroDelLoteQueVenceAntes() throws StockInsuficienteException {
        amoxicilina.descontarStock(25);
        assertEquals(0, venceAntes.getStockLote());       // FIFO: se agota primero
        assertEquals(45, venceDespues.getStockLote());    // los 5 restantes salen de acá
        assertEquals(30, vencido.getStockLote());         // el vencido no se toca
    }

    @Test
    void sinStockSuficienteLanzaExcepcionYNoDescuentaNada() {
        assertThrows(StockInsuficienteException.class, () -> amoxicilina.descontarStock(71));
        assertEquals(70, amoxicilina.getStockActual());
    }

    @Test
    void stockBajoCuandoLlegaAlMinimo() throws StockInsuficienteException {
        assertFalse(amoxicilina.estaEnStockBajo());
        amoxicilina.descontarStock(60);                   // quedan 10 = mínimo
        assertTrue(amoxicilina.estaEnStockBajo());
    }

    @Test
    void noSePuedeIngresarUnLoteVencido() {
        Lote otroVencido = new Lote("LX", LocalDate.now().minusYears(1), LocalDate.now().minusDays(5), 10);
        assertThrows(IllegalArgumentException.class, () -> amoxicilina.agregarLote(otroVencido));
    }
}
