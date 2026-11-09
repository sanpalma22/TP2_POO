package model.mascotas;

import model.atencion.Vacuna;
import model.enums.Especie;
import model.enums.InteriorExterior;
import model.enums.Sexo;
import model.enums.Tamano;
import model.personas.Duenio;

import java.time.LocalDate;

public class Gato extends Mascota{
    private InteriorExterior habitat;

    public Gato(Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, InteriorExterior habitat){
        super(duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.habitat = habitat;
    }

    public Gato(int idMascota, Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, InteriorExterior habitat) {
        super(idMascota, duenio, nombre, raza, sexo, fechaNacimiento, peso);
        this.habitat = habitat;
    }

    @Override
    public Especie getEspecie() { return Especie.GATO; }

    @Override
    public boolean esAptaParaVacuna(Vacuna v, LocalDate fecha){
        return (v.cumpleEdadMinima(this.getFechaNacimiento(), fecha) && v.getEspeciesAplicables().contains(Especie.GATO));
    };
    public String toLinea() {
        return "GATO;" + getIdMascota() + ";" + getDuenio().getDocumento() + ";" + getNombre() + ";" +
                getRaza() + ";" + getSexo() + ";" + getFechaNacimiento() + ";" + getPeso() + ";" +
                habitat;
    }
}