package model.atencion;

import model.personas.Veterinario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public Consulta(LocalDate fecha, String motivo, int pesoDia, String diagnostico, String observaciones){
        this(++contador, fecha, motivo, pesoDia, diagnostico, observaciones);
    }


    //creamos un segundo constructor para que en historiaClinica al momneto de cargar la historia clinica de una mascota se cargue con el id que esta en el archivo.txt y no con un nuevo id
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

    public void agregarTratamiento(Tratamiento t) throws PesoNoRegistradoException {
        if (pesoDia <= 0) {
            throw new PesoNoRegistradoException("Registre el peso antes de indicar un medicamento");
        }
        tratamientos.add(t);
    }

    public void agregarAplicacionVacuna(AplicacionVacuna v) { vacunas.add(v); }

    public Veterinario getVeterinario() {
        return veterinario;
    }
}
