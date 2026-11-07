package model.atencion;

import excepciones.StockInsuficienteException;
import model.stock.Insumo;   // ajusten al paquete donde tengan Stock.Insumo


public class Tratamiento {
    private Insumo medicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;

    public Tratamiento(Insumo medicamento, String dosis, String frecuencia, String duracion) {
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
    }

    public void utilizarInsumo(int cantidad) throws StockInsuficienteException {
        medicamento.descontarStock(cantidad);
    }

    public model.stock.Insumo getMedicamento() { return medicamento; }
    public String getDosis() { return dosis; }
    public String getFrecuencia() { return frecuencia; }
    public String getDuracion() { return duracion; }

    // Formato: idConsulta;codigoInsumo;dosis;frecuencia;duracion
    public String toLinea(int idConsulta) {
        return idConsulta + ";" + medicamento.getCodigo() + ";" + dosis + ";"
                + frecuencia + ";" + duracion;
    }
}