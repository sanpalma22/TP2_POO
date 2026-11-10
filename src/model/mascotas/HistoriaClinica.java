package model.mascotas;

import model.atencion.Consulta;
import model.atencion.NotaRectificatoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Historia clínica de una mascota (composición: nace con la mascota).
 * Es la que crea las consultas (patrón Creador) y solo crece: los errores se corrigen
 * con notas rectificatorias, no borrando consultas.
 */
public class HistoriaClinica {
    private Mascota mascota;
    private List<Consulta> consultas;
    private List<NotaRectificatoria> notas;

    // Se crea desde el constructor de Mascota (composición)
    public HistoriaClinica(Mascota mascota) {
        this.mascota = mascota;
        this.consultas = new ArrayList<>();
        this.notas = new ArrayList<>();
    }

    public void agregarConsulta(Consulta c) {
        this.consultas.add(c);
    }

    public List<Consulta> getConsultas() {
        return this.consultas;
    }

    public void agregarNota(NotaRectificatoria nota) {
        this.notas.add(nota);
    }

    public List<NotaRectificatoria> getNotas() {
        return this.notas;
    }

    // Patrón Creador: la historia contiene las consultas, por eso es quien las crea
    public Consulta crearConsulta(LocalDate fecha, String motivo, int pesoDia, String diagnostico, String observaciones) {
        Consulta c = new Consulta(fecha, motivo, pesoDia, diagnostico, observaciones);
        this.consultas.add(c);
        return c;
    }

    public Mascota getMascota() {
        return mascota;
    }
}