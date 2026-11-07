package model.mascotas;

import model.atencion.Vacuna;
import model.enums.Especie;
import model.enums.Sexo;
import model.personas.Duenio;

import java.time.LocalDate;

public class Ave extends Mascota{
    private boolean volador;

    public Ave(Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, boolean volador){
        super(duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.volador = volador;
    }

    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(Especie.AVE));
    };
}