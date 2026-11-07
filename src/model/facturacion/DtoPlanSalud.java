package model.facturacion;

public class DtoPlanSalud implements EstrategiaDtoRec {
    private final double porcentajeDto = 0.15;

    @Override
    public double calcularMontoFinal(double monto){
        return monto - porcentajeDto*monto;
    }
}
