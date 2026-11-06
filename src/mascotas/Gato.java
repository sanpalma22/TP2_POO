package mascotas;

import atencion.Vacuna;
import enums.Especie;
import enums.InteriorExterior;
import enums.Sexo;
import personas.Duenio;

import java.time.LocalDate;

public class Gato extends Mascota{
    private InteriorExterior habitat;
    public Gato(Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, InteriorExterior habitat){
        super(duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.habitat = habitat;
    }
    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(Especie.GATO));
    };
}