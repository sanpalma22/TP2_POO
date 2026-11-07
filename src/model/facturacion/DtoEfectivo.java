package model.facturacion;

public class DtoEfectivo implements EstrategiaDtoRec {
    private final double porcentajeDto = 0.1;

    @Override
    public double calcularMontoFinal(double monto){
        return monto - porcentajeDto*monto;
    }
}
