package mascotas;

import atencion.Vacuna;
import enums.Especie;
import enums.Sexo;
import enums.Tamano;
import personas.Duenio;

import java.time.LocalDate;

public class Perro extends Mascota{
    private String numeroChip;
    private Tamano tamano;

    public Perro(Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, String numeroChip, Tamano tamano){
        super(duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.numeroChip = numeroChip;
        this.tamano = tamano;
    }

    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(Especie.PERRO));
    };
}
