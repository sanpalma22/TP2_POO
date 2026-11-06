package mascotas;

import atencion.Vacuna;
import enums.Especie;
import enums.Sexo;
import personas.Duenio;

import java.time.LocalDate;
//HAY QUE AGREGAR HISTORIACLINICA COMO ATRIBUTO EN EL DIAGRAMA DE CLASE
//TAMBIEN HAY QUE AGREGAR EL ATRIBUTO DUENIO PARA SABER CUAL ES EL DUENIO DE LA MASCOTA
public abstract class Mascota {
    private static int contador = 0;
    private int idMascota;
    private String nombre;
    private Especie especie;
    private String raza;
    private Sexo sexo;
    private LocalDate fechaNacimiento;
    private int peso;
    private HistoriaClinica historiaClinica;
    private Duenio duenio;

    public Mascota (Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso){
        this.idMascota = ++ contador;
        this.duenio = duenio;
        this.nombre = nombre;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.historiaClinica = new HistoriaClinica(this);
    }

    public HistoriaClinica getHistoriaClinica(){
        return historiaClinica;
    }

    public void cambiarDuenio(Duenio duenio){
        this.duenio = duenio;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Especie getEspecie() {
        return especie;
    }

    public abstract boolean esAptaParaVacuna(Vacuna v, LocalDate fecha);
}
