package model.atencion;

import model.mascotas.Mascota;

import java.time.LocalDate;

/**
 * Une una vacuna aplicada con su mascota. Lo usan los recordatorios (CU08)
 * y el reporte del administrador.
 */
public class RecordatorioVacuna {
    private final Mascota mascota;
    private final AplicacionVacuna aplicacion;

    public RecordatorioVacuna(Mascota mascota, AplicacionVacuna aplicacion) {
        this.mascota = mascota;
        this.aplicacion = aplicacion;
    }

    public Mascota getMascota() { return mascota; }
    public AplicacionVacuna getAplicacion() { return aplicacion; }

    public boolean estaVencida(LocalDate hoy) { return aplicacion.estaVencida(hoy); }
}
