package model.facturacion;

/**
 * Estrategia: pago con crédito en cuotas, 8 % de recargo.
 */
public class RecargoCredito implements EstrategiaDtoRec {
    private final double porcentajeRecargo = 0.08;

    @Override
    public double calcularMontoFinal(double monto){
        return monto + porcentajeRecargo*monto;
    }
}
