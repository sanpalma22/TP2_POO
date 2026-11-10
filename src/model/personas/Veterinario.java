package model.personas;
import model.enums.TipoAtencion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Veterinario con matrícula, especialidad y un horario por día de la semana.
 * Es el único que puede registrar diagnósticos y tratamientos.
 */
public class Veterinario extends Persona {
    private String matricula;
    private String especialidad;
    private Map<DayOfWeek, FranjaHoraria> horarios;

    public Veterinario(String nombre, String apellido, String documento, String telefono, String matricula, String especialidad ){
        super(nombre, apellido, documento, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.horarios = new HashMap<>();
    }

    public boolean horarioDisponible(LocalDate fecha, LocalTime hora, TipoAtencion tipoAtencion) {
        FranjaHoraria franja = horarios.get(fecha.getDayOfWeek());
        if (franja == null) return false;              // ese día no atiende
        if (!franja.contiene(hora)) return false;      // fuera de su horario
        return franja.minutosRestantes(hora) >= tipoAtencion.getTiempo();   // la atención tiene que terminar antes de la salida
    }

    // Se usa al cargar desde horarios.txt: un día por línea
    public void agregarHorario(DayOfWeek dia, FranjaHoraria franja) {
        horarios.put(dia, franja);
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Map<DayOfWeek, FranjaHoraria> getHorarios() {
        return horarios;
    }

    @Override
    public String toLinea(){
        return "VETERINARIO;" + getDocumento() + ";" +nombre + ";" +apellido+ ";" + getTelefono() + ";" + matricula + ";" +especialidad;
    }
}
