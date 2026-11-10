package model.facturacion;

/**
 * Estrategia: pago con débito, sin descuento ni recargo.
 */
public class SinDescuento implements EstrategiaDtoRec {
    @Override
    public double calcularMontoFinal(double monto) {
        // El débito no tiene ni recargo ni descuento, devuelve el mismo monto
        return monto;
    }
}