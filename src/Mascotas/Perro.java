package Mascotas;

import enums.Especie;
import enums.Sexo;
import enums.Tamano;

import java.time.LocalDate;

public class Perro extends Mascota{
    private String numeroChip;
    private Tamano tamano;

    public Perro(String nombre, Especie especie, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, String numeroChip, Tamano tamano){
        super(nombre, especie, raza, sexo, fechaNacimiento, peso);
        this.numeroChip = numeroChip;
        this.tamano = tamano;
    }
    @Override
    public boolean esAptaParaVacuna(){
        //cambiar esto
        return true;
    };
}
