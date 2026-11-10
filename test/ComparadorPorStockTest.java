import model.stock.ComparadorPorStock;
import model.stock.Insumo;
import model.stock.Lote;
import model.stock.Proveedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparadorPorStockTest {

    private Insumo insumoCon(String codigo, int stock) {
        Proveedor p = new Proveedor("30-1", "Prov", "", "", "");
        Insumo i = new Insumo(codigo, codigo, 5, 1, 2, p);
        if (stock > 0) i.cargarLote(new Lote("L-" + codigo, LocalDate.now(), LocalDate.now().plusYears(1), stock));
        return i;
    }

    @Test
    void ordenaDeMenorAMayorStock() {
        Insumo veinte = insumoCon("A", 20);
        Insumo cero = insumoCon("B", 0);
        Insumo tres = insumoCon("C", 3);
        List<Insumo> lista = new ArrayList<>(List.of(veinte, cero, tres));

        lista.sort(new ComparadorPorStock());

        assertSame(cero, lista.get(0));
        assertSame(tres, lista.get(1));
        assertSame(veinte, lista.get(2));
    }
}
