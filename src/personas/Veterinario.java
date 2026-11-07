package personas;
import enums.TipoAtencion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Veterinario extends Persona {
    private String matricula;
    private String especialidad;
    private Map<DayOfWeek, FranjaHoraria> horarios;

    public Veterinario(String nombre, String apellido, String documento, String telefono, String matricula, String especialidad, Map<DayOfWeek, FranjaHoraria> horarios ){
        super(nombre, apellido, documento, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.horarios = horarios;
    }

    public boolean horarioDisponible(LocalDate fecha, LocalTime hora, TipoAtencion tipoatencion){
        Set<DayOfWeek> dias = this.horarios.keySet();
        if (dias.contains(fecha.getDayOfWeek())) {
            int minutosRes = this.horarios.get(fecha.getDayOfWeek()).minutosRestantes(hora); //calcula los minutos que le quedarian libres en ese horario para saber si se puede llegar a terminar la atencion, pero no evalua si tiene un turno para ese horario
            if (minutosRes >= tipoatencion.getTiempo()){
                return true;
            }
            else {return false;}
        }
        return false;
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

    public String toLinea(){
        return "VETERINARIO;" + getDocumento() + ";" +nombre + ";" +apellido+ ";" + getTelefono() + ";" + matricula + ";" +especialidad;
    }
}
