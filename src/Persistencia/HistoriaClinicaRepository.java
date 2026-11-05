package Persistencia;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Atencion.AplicacionVacuna;
import Atencion.Consulta;
import Atencion.Tratamiento;
import Atencion.Vacuna;
import Mascotas.Mascota;
import Stock.Insumo;


public class HistoriaClinicaRepository {
    private static final String RUTA_CONSULTAS = "historiaClinica.txt";
    private static final String RUTA_TRATAMIENTOS = "tratamientos.txt";
    private static final String RUTA_VACUNASAPLICACION = "vacunasAplicacion.txt";

    public void guardarConsulta(int idMascota, Consulta c) {
        try (BufferedWriter bwC = new BufferedWriter(new FileWriter(RUTA_CONSULTAS, true));
             BufferedWriter bwT = new BufferedWriter(new FileWriter(RUTA_TRATAMIENTOS, true));
             BufferedWriter bwV = new BufferedWriter(new FileWriter(RUTA_VACUNASAPLICACION, true))){
            bwC.write(idMascota + ";" + c.getIdConsulta() + ";" + c.getFecha() + ";" + c.getMotivo() + ";" + c.getPesoDia() + ";" + c.getDiagnostico() + ";" + c.getObservaciones());
            bwC.newLine();

            // 2. Sus tratamientos (0, 1 o varios)
            for (Tratamiento t : c.getTratamientos()) {
                bwT.write(c.getIdConsulta() + ";" + t.getMedicamento().getCodigo() + ";" + t.getDosis() + ";" + t.getFrecuencia() + ";" + t.getDuracion());
                bwT.newLine();
            }

            // 3. Sus vacunasAplicacion (0, 1 o varios)
            for (AplicacionVacuna a: c.getVacunas()) {
                bwV.write(c.getIdConsulta() + ";" + a.getVacuna().getNombre() + ";" + a.getFecha() + ";"
                        + a.getFechaRefuerzo() + ";" + a.getLote());
                bwV.newLine();
            }
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void cargar(Mascota m, Map<String, Insumo> insumosPorCodigo, Map<String, Vacuna> vacunasPorNombre) {
        Map<Integer, Consulta> consultasPorId = cargarConsultas(m);

        // le cargamos los tratamientos a esas consulta con el id de la consulta que guardamos
        cargarTratamientosConsultas(insumosPorCodigo, consultasPorId);

        // le cargamos las vacunasAplicadas a esas consulta con el id de la consulta que guardamos
        cargarVacunasAplicadasConsultas(vacunasPorNombre, consultasPorId);
    }

    private Map<Integer, Consulta> cargarConsultas(Mascota m){
        int idMascota = m.getIdMascota();
        Map<Integer, Consulta> consultasPorId = new HashMap<>();
        // Cargamos las consultas y en un diccionario guardamos el id y la consulta para despues poder cargarle a las consultas los tratamientos de la misma
        if (new File(RUTA_CONSULTAS).exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CONSULTAS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(";", -1);
                    if (Integer.parseInt(p[0]) == idMascota) {
                        Consulta c = new Consulta(Integer.parseInt(p[1]), LocalDate.parse(p[2]), p[3], Integer.parseInt(p[4]), p[5], p[6]);
                        m.getHistoriaClinica().agregarConsulta(c);
                        consultasPorId.put(c.getIdConsulta(), c);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return consultasPorId;
    }

    private void cargarTratamientosConsultas(Map<String, Insumo> insumosPorCodigo, Map<Integer, Consulta> consultasPorId){
        // le cargamos los tratamientos a esas consulta con el id de la consulta que guardamos
        if (new File(RUTA_TRATAMIENTOS).exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(RUTA_TRATAMIENTOS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(";");
                    Consulta c = consultasPorId.get(Integer.parseInt(p[0]));
                    if (c != null) {   // null = es de otra mascota
                        Insumo med = insumosPorCodigo.get(p[1]);  // código → objeto
                        if (med == null) {
                            System.out.println("Insumo no encontrado: " + p[1]);
                            continue;
                        }
                        c.agregarTratamiento(new Tratamiento(med, p[2], p[3], p[4]));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void cargarVacunasAplicadasConsultas(Map<String, Vacuna> vacunasPorNombre, Map<Integer, Consulta> consultasPorId){
        // le cargamos las vacunasAplicadas a esas consulta con el id de la consulta que guardamos
        if (new File(RUTA_VACUNASAPLICACION).exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(RUTA_VACUNASAPLICACION))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(";", -1);
                    Consulta c = consultasPorId.get(Integer.parseInt(p[0]));
                    if (c != null) {   // null = es de otra mascota
                        Vacuna vac = vacunasPorNombre.get(p[1]);  // nombre → objeto
                        if (vac == null) {
                            System.out.println("Vacuna no encontrada: " + p[1]);
                            continue;
                        }
                        c.agregarAplicacionVacuna(new AplicacionVacuna(vac, LocalDate.parse(p[2]), LocalDate.parse(p[3]), p[4]));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
