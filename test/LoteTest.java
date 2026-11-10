import model.stock.Lote;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoteTest {

    @Test
    void estaVencidoSegunLaFechaRecibida() {
        Lote lote = new Lote("L1", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), 10);
        assertFalse(lote.estaVencido(LocalDate.of(2026, 6, 30)));   // el mismo día todavía sirve
        assertTrue(lote.estaVencido(LocalDate.of(2026, 7, 1)));
    }

    @Test
    void ordenNaturalPorVencimiento() {
        Lote julio = new Lote("A", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 7, 1), 10);
        Lote marzo = new Lote("B", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 1), 10);
        List<Lote> lotes = new ArrayList<>(List.of(julio, marzo));
        Collections.sort(lotes);                                     // usa compareTo (Comparable)
        assertSame(marzo, lotes.get(0));
    }

    @Test
    void noAceptaCantidadInicialCeroONegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> new Lote("L1", LocalDate.now(), LocalDate.now().plusYears(1), 0));
    }

    @Test
    void noDescuentaMasDeLoQueTiene() {
        Lote lote = new Lote("L1", LocalDate.now(), LocalDate.now().plusYears(1), 5);
        assertThrows(IllegalArgumentException.class, () -> lote.descontarCantidad(6));
    }
}
