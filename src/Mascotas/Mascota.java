package Mascotas;

import enums.Especie;
import enums.Sexo;

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
    private int id_duenio;
    public Mascota (int id_duenio, String nombre, Especie especie, String raza, Sexo sexo, LocalDate fechaNacimiento, int peso, HistoriaClinica historiaClinica){
        this.idMascota = ++ contador;
        this.id_duenio = id_duenio;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.historiaClinica = new HistoriaClinica();
    }

    public HistoriaClinica getHistoriaClinica(){
        return historiaClinica;
    };

    public void cambiarDuenio(int idDuenio){
        this.id_duenio = idDuenio;
    };

    public boolean esAptaParaVacuna(){
        //completar
        return true;
    };

    public int getIdMascota() {
        return idMascota;
    }
}
