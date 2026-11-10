package model.atencion;

import java.time.LocalDate;

/**
 * Corrección de una consulta. La historia clínica no se modifica:
 * los errores se corrigen agregando una nota (CU13).
 */
public class NotaRectificatoria {
    private LocalDate fecha;
    private String texto;

    public NotaRectificatoria(LocalDate fecha, String texto) {
        this.fecha = fecha;
        this.texto = texto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTexto() {
        return texto;
    }
}