package model.mascotas;

import model.atencion.Consulta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class HistoriaClinica {
    private Mascota mascota;
    private List<Consulta> consultas = new ArrayList<>();


    public HistoriaClinica(Mascota mascota) {
        this.mascota = mascota;
    }


    public Consulta crearConsulta(LocalDate fecha, String motivo, int pesoDia,
                                  String diagnostico, String observaciones) {
        Consulta c = new Consulta(fecha, motivo, pesoDia, diagnostico, observaciones);
        consultas.add(c);
        return c;
    }


    public void agregarConsulta(Consulta c) {
        consultas.add(c);
    }


    public List<Consulta> getConsultas() {
        return new ArrayList<>(consultas);
    }


}