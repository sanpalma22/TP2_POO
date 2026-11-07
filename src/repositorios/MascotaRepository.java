package repositorios;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.enums.InteriorExterior;
import model.enums.Sexo;
import model.enums.Tamano;
import model.mascotas.*;
import model.personas.Duenio;

public class MascotaRepository {
    private static final String RUTA_MASCOTAS = "datos/mascotas.txt";

    /**
     * Guarda una nueva mascota al final del archivo mascotas.txt.
     */
    public void guardar(Mascota m) {
        File directorio = new File("datos");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_MASCOTAS, true))) {
            bw.write(m.toLinea());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar la mascota: " + e.getMessage());
        }
    }

    /**
     * Guarda/sobrescribe todas las mascotas (útil cuando una mascota cambia de dueño).
     */
    public void guardarTodas(List<Mascota> mascotas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_MASCOTAS, false))) {
            for (Mascota m : mascotas) {
                bw.write(m.toLinea());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al sobrescribir las mascotas: " + e.getMessage());
        }
    }

    /**
     * Carga todas las mascotas asociándolas a su dueño correspondiente (usando un mapa DNI -> Duenio).
     */
    public List<Mascota> cargarTodas(Map<String, Duenio> dueniosPorDni) {
        List<Mascota> lista = new ArrayList<>();
        File archivo = new File(RUTA_MASCOTAS);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] p = linea.split(";", -1);

                String tipo = p[0];
                int idMascota = Integer.parseInt(p[1]);
                String dniDuenio = p[2];
                String nombre = p[3];
                String raza = p[4];
                Sexo sexo = Sexo.valueOf(p[5]);
                LocalDate fechaNacimiento = LocalDate.parse(p[6]);
                int peso = Integer.parseInt(p[7]);

                Duenio duenio = dueniosPorDni.get(dniDuenio);
                if (duenio == null) {
                    System.out.println("Dueño no encontrado (DNI: " + dniDuenio + ") para la mascota " + nombre);
                    continue;
                }

                Mascota m = null;

                switch (tipo.toUpperCase()) {
                    case "PERRO" -> {
                        String chip = p[8];
                        Tamano tamano = Tamano.valueOf(p[9]);
                        m = new Perro(duenio, nombre, raza, sexo, fechaNacimiento, peso, chip, tamano);
                    }
                    case "GATO" -> {
                        InteriorExterior habitat = InteriorExterior.valueOf(p[8]);
                        m = new Gato(duenio, nombre, raza, sexo, fechaNacimiento, peso, habitat);
                    }
                    case "AVE" -> {
                        boolean volador = Boolean.parseBoolean(p[8]);
                        m = new Ave(duenio, nombre, raza, sexo, fechaNacimiento, peso, volador);
                    }
                    case "CONEJO" -> {
                        String chip = p[8];
                        Tamano tamano = Tamano.valueOf(p[9]);
                        m = new Conejo(duenio, nombre, raza, sexo, fechaNacimiento, peso, chip, tamano);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las mascotas: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca una mascota por su id único.
     */
    public Mascota buscarPorId(List<Mascota> mascotas, int idMascota) {
        for (Mascota m : mascotas) {
            if (m.getIdMascota() == idMascota) {
                return m;
            }
        }
        return null;
    }
}