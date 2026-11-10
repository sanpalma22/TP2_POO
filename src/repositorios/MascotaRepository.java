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

/**
 * Persistencia de mascotas en datos/mascotas.txt. El primer campo de cada línea
 * indica la especie, y con eso se decide qué subclase crear al cargar.
 */
public class MascotaRepository {
    private static final String RUTA_MASCOTAS = "datos/mascotas.txt";

    /**
     * Alta de una mascota nueva: se agrega al final del archivo.
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
     * Reescribe el archivo completo. Se usa cuando cambia una mascota ya guardada (peso o dueño).
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

        if (!archivo.exists()) {
            return lista;
        }

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

                // El tipo decide qué subclase crear; se usa el constructor con id para conservarlo
                switch (tipo.toUpperCase()) {
                    case "PERRO" -> {
                        String chip = p[8];
                        Tamano tamano = Tamano.valueOf(p[9]);
                        m = new Perro(idMascota, duenio, nombre, raza, sexo, fechaNacimiento, peso, chip, tamano);
                    }
                    case "GATO" -> {
                        InteriorExterior habitat = InteriorExterior.valueOf(p[8]);
                        m = new Gato(idMascota, duenio, nombre, raza, sexo, fechaNacimiento, peso, habitat);
                    }
                    case "AVE" -> {
                        boolean volador = Boolean.parseBoolean(p[8]);
                        m = new Ave(idMascota, duenio, nombre, raza, sexo, fechaNacimiento, peso, volador);
                    }
                    case "CONEJO" -> {
                        String chip = p[8];
                        Tamano tamano = Tamano.valueOf(p[9]);
                        m = new Conejo(idMascota, duenio, nombre, raza, sexo, fechaNacimiento, peso, chip, tamano);
                    }
                }
                if (m != null) {
                    lista.add(m);
                } else {
                    System.out.println("Tipo de mascota desconocido: " + tipo);
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