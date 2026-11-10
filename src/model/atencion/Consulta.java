package model.atencion;

import excepciones.PesoNoRegistradoException;
import model.personas.Veterinario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Atención de una mascota: motivo, peso del día, diagnóstico, tratamientos y vacunas.
 * La crea HistoriaClinica (patrón Creador). El id es autoincremental y se conserva al recargar.
 */
public class Consulta {
    private static int contador = 0;
    private final int idConsulta;
    private LocalDate fecha;
    private String motivo;
    private int pesoDia;
    private String diagnostico;
    private List<Tratamiento> tratamientos;
    private List<AplicacionVacuna> vacunas;
    private String observaciones;
    private Veterinario veterinario;

    // Consulta nueva: el id sale del contador
    public Consulta(LocalDate fecha, String motivo, int pesoDia, String diagnostico, String observaciones){
        this(++contador, fecha, motivo, pesoDia, diagnostico, observaciones);
    }


    // Consulta cargada desde archivo: respeta el id guardado y deja el contador en el máximo
    public Consulta(int idConsulta, LocalDate fecha, String motivo, int pesoDia, String diagnostico, String observaciones){
        this.idConsulta = idConsulta;
        this.fecha = fecha;
        this.motivo = motivo;
        this.pesoDia = pesoDia;
        this.diagnostico = diagnostico;
        this.tratamientos = new ArrayList<Tratamiento>();
        this.vacunas = new ArrayList<AplicacionVacuna>();
        this.observaciones = observaciones;
        if (idConsulta > contador) contador = idConsulta;
    }

    public LocalDate getFecha(){
        return fecha;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getPesoDia() {
        return pesoDia;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public List<AplicacionVacuna> getVacunas() {
        return vacunas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    // No se indica medicamento sin el peso del día, porque la dosis depende de él
    public void agregarTratamiento(Tratamiento t) throws PesoNoRegistradoException {
        if (pesoDia <= 0) {
            throw new PesoNoRegistradoException("Registre el peso antes de indicar un medicamento");
        }
        tratamientos.add(t);
    }

    public void agregarAplicacionVacuna(AplicacionVacuna v) { vacunas.add(v); }

    // Solo un veterinario registra el diagnóstico: el parámetro es de tipo Veterinario,
    // así que el compilador no deja pasar a otro tipo de persona
    public void registrarDiagnostico(Veterinario veterinario, String diagnostico, String observaciones) {
        this.veterinario = veterinario;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
    }

    public void asignarVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }


}
