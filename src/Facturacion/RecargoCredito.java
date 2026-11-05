package Facturacion;

public class RecargoCredito implements EstrategiaDtoRec {
    private final double porcentajeRecargo = 0.08;

    @Override
    public double calcularMontoFinal(double monto){
        return monto + porcentajeRecargo*monto;
    }
}
