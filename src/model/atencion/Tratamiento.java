package model.atencion;

import excepciones.StockInsuficienteException;
import model.stock.Insumo;


/**
 * Medicamento indicado en una consulta (CU16), con dosis, frecuencia y duración.
 * El stock no se descuenta al indicarlo sino al finalizar la consulta.
 */
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

    // Se llama al finalizar la consulta (CU06): recién ahí se descuenta el stock
    public void utilizarInsumo(int cantidad) throws StockInsuficienteException {
        medicamento.descontarStock(cantidad);
    }

    public Insumo getMedicamento() { return medicamento; }
    public String getDosis() { return dosis; }
    public String getFrecuencia() { return frecuencia; }
    public String getDuracion() { return duracion; }

    // Formato: idConsulta;codigoInsumo;dosis;frecuencia;duracion
    public String toLinea(int idConsulta) {
        return idConsulta + ";" + medicamento.getCodigo() + ";" + dosis + ";"
                + frecuencia + ";" + duracion;
    }
}