package Mascotas;

import Atencion.AplicacionVacuna;
import Atencion.Vacuna;
import enums.Especie;
import enums.Sexo;
import enums.Tamano;
import personas.Duenio;

import java.time.LocalDate;

public class Perro extends Mascota{
    private String numeroChip;
    private Tamano tamano;

    public Perro(Duenio duenio, String nombre, Especie especie, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, String numeroChip, Tamano tamano){
        super(duenio, nombre, especie, raza, sexo, fechaNacimiento, peso);
        this.numeroChip = numeroChip;
        this.tamano = tamano;
    }

    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(this.getEspecie()));
    };
}
