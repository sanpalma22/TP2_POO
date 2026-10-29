import java.time.LocalTime;
public class FranjaHoraria {
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    public FranjaHoraria(LocalTime horaEntrada, LocalTime horaSalida){
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public boolean contiene(LocalTime hora){
        return (!hora.isBefore(horaEntrada) && hora.isBefore(horaSalida));
    }

    public int minutosRestantes(LocalTime hora){
        if (contiene(hora)){
            int minsRestantes = (horaSalida.getMinute() - hora.getMinute());
            return minsRestantes;
        }
        else {
            throw new IllegalArgumentException("A esta hora el veterinario no esta disponible");
        }
    }
}

