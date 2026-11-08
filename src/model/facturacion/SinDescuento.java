package model.facturacion;

public class SinDescuento implements EstrategiaDtoRec {
    @Override
    public double calcularMontoFinal(double monto) {
        // El débito no tiene ni recargo ni descuento, devuelve el mismo monto
        return monto;
    }
}