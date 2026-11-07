package repositorios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.personas.Duenio;

public class DuenioRepository {
    private static final String RUTA_DUENIOS = "datos/duenios.txt";


    public void guardar(Duenio d) {
        // Verificar que el directorio "datos" exista
        File directorio = new File("datos");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_DUENIOS, true))) {
            bw.write(d.toLinea());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar el dueño: " + e.getMessage());
        }
    }

    public List<Duenio> cargarTodos() {
        List<Duenio> lista = new ArrayList<>();
        File archivo = new File(RUTA_DUENIOS);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";", -1);
                String documento = p[0];
                String nombre = p[1];
                String apellido = p[2];
                String telefono = p[3];
                String email = p[4];
                boolean planSalud = Boolean.parseBoolean(p[5]);
                int ausencias = Integer.parseInt(p[6]);

                Duenio duenio = new Duenio(nombre, apellido, documento, telefono, email, planSalud, ausencias);
                lista.add(duenio);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los dueños: " + e.getMessage());
        }

        return lista;
    }
    
    public Duenio buscarPorDocumento(String documento) {
        List<Duenio> todos = cargarTodos();
        for (Duenio d : todos) {
            if (d.getDocumento().equalsIgnoreCase(documento)) {
                return d;
            }
        }
        return null;
    }
}