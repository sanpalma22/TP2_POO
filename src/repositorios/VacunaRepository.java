package repositorios;

import model.atencion.Vacuna;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia del catálogo de vacunas en datos/vacunas.txt.
 */
public class VacunaRepository {
    private static final String RUTA_VACUNAS = "datos/vacunas.txt";

    // Formato: nombre;laboratorio;PERRO,GATO;edadMinMeses;intervaloRefuerzoDias;costo;precioVenta
    public List<Vacuna> cargarTodas() {
        List<Vacuna> lista = new ArrayList<>();
        File archivo = new File(RUTA_VACUNAS);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                lista.add(Vacuna.desdeLinea(linea.replace("\uFEFF", "")));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las vacunas: " + e.getMessage());
        }
        return lista;
    }

    // Reescribe el catálogo completo (CU21: se pueden dar de alta y modificar vacunas)
    public void guardarTodas(List<Vacuna> vacunas) {
        new File("datos").mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_VACUNAS))) {
            for (Vacuna v : vacunas) {
                bw.write(v.toLinea());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar las vacunas: " + e.getMessage());
        }
    }
}
