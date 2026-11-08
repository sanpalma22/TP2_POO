package model.mascotas;


import model.atencion.Consulta;
import model.atencion.NotaRectificatoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {
    private Mascota mascota;
    private List<Consulta> consultas = new ArrayList<>();
    private List<NotaRectificatoria> notas = new ArrayList<>();

    public HistoriaClinica(Mascota mascota) {
        this.mascota = mascota;
    }

    public void agregarConsulta(Consulta c) { consultas.add(c); }
    public List<Consulta> getConsultas() { return java.util.Collections.unmodifiableList(consultas); }

    public void agregarNota(NotaRectificatoria nota) {
        this.notas.add(nota);
    }

    public List<NotaRectificatoria> getNotas() {
        return this.notas;
    }
    public Consulta crearConsulta(LocalDate fecha, String motivo, int pesoDia, String diagnostico, String observaciones) {

        Consulta c = new Consulta(fecha, motivo, pesoDia, diagnostico, observaciones);

        this.consultas.add(c);
        return c;
    }
}
