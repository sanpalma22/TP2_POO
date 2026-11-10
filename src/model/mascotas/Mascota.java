package model.mascotas;

import model.atencion.Vacuna;
import model.enums.Especie;
import model.enums.Sexo;
import model.personas.Duenio;

import java.time.LocalDate;

/**
 * Clase abstracta de las mascotas. Cada especie (Perro, Gato, Ave, Conejo) define su
 * especie y su regla de vacunación sobrescribiendo los métodos abstractos (polimorfismo y
 * principio de Liskov: cualquier subclase se usa donde se espera una Mascota).
 * El id es autoincremental y se conserva al recargar desde archivo.
 */
public abstract class Mascota {
    private static int contador = 0;
    private int idMascota;
    private String nombre;
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

    public Mascota(int idMascota, Duenio duenio, String nombre, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso) {
        this.idMascota = idMascota;
        this.duenio = duenio;
        this.nombre = nombre;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.historiaClinica = new HistoriaClinica(this);
        if (idMascota > contador) contador = idMascota;
    }

    public abstract String toLinea();
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


    public abstract boolean esAptaParaVacuna(Vacuna v, LocalDate fecha);

    public void setPeso(int peso) {
        if (peso <= 0) throw new IllegalArgumentException("Peso inválido");
        this.peso = peso;
    }

    public abstract Especie getEspecie();

    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public int getPeso() {
        return peso;
    }

    public Duenio getDuenio() {
        return duenio;
    }
}
