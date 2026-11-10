package model.stock;

import java.util.Comparator;

/**
 * Orden personalizado (Comparator): de menor a mayor stock disponible.
 * Se usa en el reporte de stock bajo para mostrar primero los más críticos.
 * El orden natural (Comparable) queda para otros casos, como Lote por vencimiento.
 */
public class ComparadorPorStock implements Comparator<Insumo> {
    @Override
    public int compare(Insumo i1, Insumo i2) {
        return Integer.compare(i1.getStockActual(), i2.getStockActual());
    }
}
