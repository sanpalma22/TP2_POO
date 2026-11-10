package repositorios;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import excepciones.PesoNoRegistradoException;
import model.atencion.AplicacionVacuna;
import model.atencion.Consulta;
import model.atencion.Tratamiento;
import model.atencion.Vacuna;
import model.mascotas.Mascota;
import model.stock.Insumo;


/**
 * Persistencia de la historia clínica en tres archivos: consultas, tratamientos y vacunas
 * aplicadas, unidos por idConsulta. Usa append (agregar al final) porque la historia
 * solo crece: nada se borra ni se modifica.
 */
public class HistoriaClinicaRepository {
    private static final String RUTA_CONSULTAS = "datos/historiaClinica.txt";
    private static final String RUTA_TRATAMIENTOS = "datos/tratamientos.txt";
    private static final String RUTA_VACUNASAPLICACION = "datos/vacunasAplicacion.txt";

    // Se llama una sola vez, al finalizar la consulta, para no duplicar registros
    public void guardarConsulta(int idMascota, Consulta c) {
        try (BufferedWriter bwC = new BufferedWriter(new FileWriter(RUTA_CONSULTAS, true));
             BufferedWriter bwT = new BufferedWriter(new FileWriter(RUTA_TRATAMIENTOS, true));
             BufferedWriter bwV = new BufferedWriter(new FileWriter(RUTA_VACUNASAPLICACION, true))){
            bwC.write(idMascota + ";" + c.getIdConsulta() + ";" + c.getFecha() + ";" + c.getMotivo() + ";" + c.getPesoDia() + ";" + c.getDiagnostico() + ";" + c.getObservaciones());
            bwC.newLine();

            // Una línea por tratamiento, con el idConsulta para poder unirlos al cargar
            for (Tratamiento t : c.getTratamientos()) {
                bwT.write(c.getIdConsulta() + ";" + t.getMedicamento().getCodigo() + ";" + t.getDosis() + ";" + t.getFrecuencia() + ";" + t.getDuracion());
                bwT.newLine();
            }

            // Una línea por vacuna aplicada, también con el idConsulta
            for (AplicacionVacuna a: c.getVacunas()) {
                bwV.write(c.getIdConsulta() + ";" + a.getVacuna().getNombre() + ";" + a.getFecha() + ";"
                        + a.getFechaRefuerzo() + ";" + a.getLote());
                bwV.newLine();
            }
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Carga en tres pasos: las consultas de la mascota y después sus tratamientos y vacunas
    public void cargar(Mascota m, Map<String, Insumo> insumosPorCodigo, Map<String, Vacuna> vacunasPorNombre) {
        Map<Integer, Consulta> consultasPorId = cargarConsultas(m);

        cargarTratamientosConsultas(insumosPorCodigo, consultasPorId);

        cargarVacunasAplicadasConsultas(vacunasPorNombre, consultasPorId);
    }

    private Map<Integer, Consulta> cargarConsultas(Mascota m){
        int idMascota = m.getIdMascota();
        Map<Integer, Consulta> consultasPorId = new HashMap<>();
        // Cada consulta se guarda en un Map por id para después engancharle tratamientos y vacunas
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

    // Lee todos los tratamientos y se queda con los de las consultas de esta mascota
    private void cargarTratamientosConsultas(Map<String, Insumo> insumosPorCodigo, Map<Integer, Consulta> consultasPorId){
        if (new File(RUTA_TRATAMIENTOS).exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(RUTA_TRATAMIENTOS))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(";", -1);
                    Consulta c = consultasPorId.get(Integer.parseInt(p[0]));
                    if (c != null) {   // null: es de una consulta de otra mascota
                        Insumo med = insumosPorCodigo.get(p[1]);  // el archivo guarda el código; acá se busca el objeto
                        if (med == null) {
                            System.out.println("Insumo no encontrado: " + p[1]);
                            continue;
                        }
                        try {
                            c.agregarTratamiento(new Tratamiento(med, p[2], p[3], p[4]));
                        } catch (excepciones.PesoNoRegistradoException e) {
                            System.out.println("Aviso al cargar historial: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());

            }
        }
    }

    // Igual que los tratamientos, pero con las vacunas aplicadas
    private void cargarVacunasAplicadasConsultas(Map<String, Vacuna> vacunasPorNombre, Map<Integer, Consulta> consultasPorId){
        if (new File(RUTA_VACUNASAPLICACION).exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(RUTA_VACUNASAPLICACION))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split(";", -1);
                    Consulta c = consultasPorId.get(Integer.parseInt(p[0]));
                    if (c != null) {   // null: es de una consulta de otra mascota
                        Vacuna vac = vacunasPorNombre.get(p[1]);  // el archivo guarda el nombre; acá se busca el objeto
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
