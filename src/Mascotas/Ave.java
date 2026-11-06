package Mascotas;

import Atencion.AplicacionVacuna;
import Atencion.Vacuna;
import enums.Especie;
import enums.Sexo;
import enums.Tamano;
import personas.Duenio;

import java.time.LocalDate;

public class Ave extends Mascota{
    private boolean volador;

    public Ave(Duenio duenio, String nombre, Especie especie, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, boolean volador){
        super(duenio, nombre, especie, raza, sexo, fechaNacimiento, peso);
        this.volador = volador;
    }

    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(this.getEspecie()));
    };
}