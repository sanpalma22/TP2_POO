package model.mascotas;

import model.atencion.Vacuna;
import model.enums.Especie;
import model.enums.InteriorExterior;
import model.enums.Sexo;
import model.enums.Tamano;
import model.personas.Duenio;

import java.time.LocalDate;

public class Conejo extends Mascota{
    private String numeroChip;
    private Tamano tamano;

    public Conejo (Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, String numeroChip, Tamano tamano){
        super(duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.numeroChip = numeroChip;
        this.tamano = tamano;
    }

    public Conejo(int idMascota, Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, String numeroChip, Tamano tamano) {
        super(idMascota, duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.numeroChip = numeroChip;
        this.tamano = tamano;
    }

    @Override
    public Especie getEspecie() { return Especie.CONEJO; }

    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(Especie.CONEJO));
    };
    public String toLinea() {
        return "CONEJO;" + getIdMascota() + ";" + getDuenio().getDocumento() + ";" + getNombre() + ";" +
                getRaza() + ";" + getSexo() + ";" + getFechaNacimiento() + ";" + getPeso() + ";" +
                numeroChip + ";" + tamano;
    }
}