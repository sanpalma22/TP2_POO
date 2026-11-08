package controller;

import model.atencion.*;
import model.mascotas.*;
import model.personas.*;
import model.stock.*;
import excepciones.*;
import repositorios.HistoriaClinicaRepository;

import java.time.LocalDate;

public class GestorAtencion {

    private HistoriaClinicaRepository historiaRepo;
    private Object gestorStockFacturacion;

    public GestorAtencion(HistoriaClinicaRepository historiaRepo, Object gestorStockFacturacion) {
        this.historiaRepo = historiaRepo;
        this.gestorStockFacturacion = gestorStockFacturacion;
    }

    public Consulta iniciarConsulta(Object turno, String motivo, int pesoDia, Mascota mascota) {
        mascota.setPeso(pesoDia);
        return mascota.getHistoriaClinica().crearConsulta(LocalDate.now(), motivo, pesoDia, "A definir", "Sin observaciones");
    }

    public void registrarDiagnostico(Consulta consulta, Persona usuarioActual, String diagnostico, String observaciones) {
        if (usuarioActual instanceof Veterinario) {

            System.out.println("Diagnóstico registrado por el veterinario.");
        } else {
            System.out.println("Error: Solo un veterinario puede registrar un diagnóstico.");
        }
    }

    public void indicarTratamiento(Consulta consulta, Insumo insumo, String dosis, String frecuencia, String duracion) {
        Tratamiento nuevoTratamiento = new Tratamiento(insumo, dosis, frecuencia, duracion);
        try {
            consulta.agregarTratamiento(nuevoTratamiento);
            System.out.println("Tratamiento indicado correctamente. (Stock a descontar al finalizar)");
        } catch (PesoNoRegistradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registrarVacuna(Consulta consulta, Object vacuna) {
        System.out.println("Vacuna registrada en la consulta.");
    }

    public void finalizarConsulta(Consulta consulta) {
        System.out.println("Consulta finalizada. Procediendo a descontar stock...");
    }

    // CU 17: Consultar Historia Clínica
    public void mostrarHistoriaClinica(Mascota mascota) {
        System.out.println("\n=== HISTORIA CLÍNICA DE LA MASCOTA ===");
        HistoriaClinica hc = mascota.getHistoriaClinica();


        System.out.println("--- Consultas Médicas ---");
        for (Consulta c : hc.getConsultas()) {
            System.out.println("- Motivo: " + c.getMotivo());
            System.out.println("  Diagnóstico: " + c.getDiagnostico());
            System.out.println("  .........................");
        }

    }
}