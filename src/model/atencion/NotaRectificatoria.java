package model.atencion;

import java.time.LocalDate;

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