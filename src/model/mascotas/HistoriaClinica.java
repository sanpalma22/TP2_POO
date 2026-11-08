package model.mascotas;

import model.atencion.Consulta;
import model.atencion.NotaRectificatoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {
    private Mascota mascota;
    private List<Consulta> consultas;
    private List<NotaRectificatoria> notas;

    // Constructor vacío por si se crea sin asignarle la mascota de entrada
    public HistoriaClinica() {
        this.consultas = new ArrayList<>();
        this.notas = new ArrayList<>();
    }

    // Constructor con mascota
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

    public Consulta crearConsulta(LocalDate fecha, String motivo, int pesoDia, String diagnostico, String observaciones) {
        // Asumiendo que el constructor de Consulta recibe estos 5 parámetros en este orden
        Consulta c = new Consulta(fecha, motivo, pesoDia, diagnostico, observaciones);
        this.consultas.add(c);
        return c;
    }

    public Mascota getMascota() {
        return mascota;
    }
}