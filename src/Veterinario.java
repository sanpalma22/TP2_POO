import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

public class Veterinario {
    private String matricula;
    private String especialidad;
    private Map<DayOfWeek, FranjaHoraria> horarios;

    public Veterinario(String matricula, String especialidad, Map<DayOfWeek, FranjaHoraria> horarios){
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.horarios = horarios;
    }

    public void indicarTratamiento(){
    }

    public boolean horarioDisponible(LocalDate fecha, LocalTime hora, TipoAtencion tipoatencion){
        Set<DayOfWeek> dias = this.horarios.keySet();
        if (dias.contains(fecha.getDayOfWeek())) {
            int minutosRes = this.horarios.get(fecha.getDayOfWeek()).minutosRestantes(hora); //calcula los minutos que le quedarian libres en ese horario para saber si se puede llegar a terminar la atencion, pero no evalua si tiene un turno para ese horario
            if (minutosRes <= tipoatencion.getValor()){
                return true;
            }
            else {return false;}
        }
        return false;
    }


}
