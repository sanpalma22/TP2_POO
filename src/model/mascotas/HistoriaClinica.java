package model.mascotas;


import model.atencion.Consulta;

import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {
    private Mascota mascota;
    private List<Consulta> consultas = new ArrayList<>();

    public HistoriaClinica(Mascota mascota) {
        this.mascota = mascota;
    }

    public void agregarConsulta(Consulta c) { consultas.add(c); }
    public List<Consulta> getConsultas() { return consultas; }
}
