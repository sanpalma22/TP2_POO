package model.enums;

public enum TipoAtencion {
    CONSULTA_GRAL(15000.0,30),
    VACUNACION(15000.0,15),
    CIRUGIA(50000.0,120);

    private final double costo;
    private final int tiempo;
    TipoAtencion(double costo, int valor){
        this.costo = costo;
        this.tiempo = valor;
    }

    public int getTiempo(){
        return tiempo;
    }

    public double getCosto() {
        return costo;
    }
}


