public enum TipoAtencion {
    CONSULTA_GRAL(30),
    VACUNACION(15),
    CIRUGIA(120);

    private final int valor;
    TipoAtencion(int valor){
        this.valor = valor;
    }

    public int getValor(){
        return valor;
    }
}

}
