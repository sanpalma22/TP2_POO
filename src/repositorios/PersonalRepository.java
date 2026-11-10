package repositorios;

import model.personas.*;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

/**
 * Persistencia del personal (datos/personal.txt) y de los horarios de los
 * veterinarios (datos/horarios.txt, una línea por día).
 */
public class PersonalRepository {
    private static final String RUTA_PERSONAL = "datos/personal.txt";
    private static final String RUTA_HORARIOS = "datos/horarios.txt";

    // Carga

    public List<Persona> cargarTodos() {
        List<Persona> personal = new ArrayList<>();
        Map<String, Veterinario> veterinariosPorDocumento = new HashMap<>();

        cargarPersonal(personal, veterinariosPorDocumento);
        cargarHorarios(veterinariosPorDocumento);

        return personal;
    }

    // Formato: tipo;documento;nombre;apellido;telefono;matricula;especialidad
    private void cargarPersonal(List<Persona> personal, Map<String, Veterinario> veterinariosPorDocumento) {
        File archivo = new File(RUTA_PERSONAL);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.replace("\uFEFF", "").split(";", -1);

                String tipo = p[0].trim().toUpperCase();
                String documento = p[1];
                String nombre = p[2];
                String apellido = p[3];
                String telefono = p[4];

                switch (tipo) {
                    case "VETERINARIO" -> {
                        // Los horarios se agregan después, al leer horarios.txt
                        Veterinario v = new Veterinario(nombre, apellido, documento, telefono,
                                p[5], p[6]);
                        personal.add(v);
                        veterinariosPorDocumento.put(documento, v);
                    }
                    case "RECEPCIONISTA" -> personal.add(new Recepcionista(nombre, apellido, documento, telefono));
                    case "ADMINISTRADOR" -> personal.add(new Administrador(nombre, apellido, documento, telefono));
                    default -> System.out.println("Tipo de personal desconocido: " + tipo);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el personal: " + e.getMessage());
        }
    }

    // Formato: documentoVeterinario;DIA;horaEntrada;horaSalida
    private void cargarHorarios(Map<String, Veterinario> veterinariosPorDocumento) {
        File archivo = new File(RUTA_HORARIOS);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.replace("\uFEFF", "").split(";", -1);

                Veterinario v = veterinariosPorDocumento.get(p[0]);   // documento → objeto
                if (v == null) {
                    System.out.println("Veterinario no encontrado para el horario: " + p[0]);
                    continue;
                }
                DayOfWeek dia = DayOfWeek.valueOf(p[1].trim().toUpperCase());
                FranjaHoraria franja = new FranjaHoraria(LocalTime.parse(p[2].trim()), LocalTime.parse(p[3].trim()));
                v.agregarHorario(dia, franja);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los horarios: " + e.getMessage());
        }
    }

    // Guardado

    // Reescribe los dos archivos completos (los horarios pueden cambiar)
    public void guardarTodos(List<Persona> personal) {
        new File("datos").mkdirs();

        try (BufferedWriter bwP = new BufferedWriter(new FileWriter(RUTA_PERSONAL));
             BufferedWriter bwH = new BufferedWriter(new FileWriter(RUTA_HORARIOS))) {

            for (Persona p : personal) {
                bwP.write(p.toLinea());   // toLinea() es abstracto en Persona: cada tipo escribe su propia línea
                bwP.newLine();

                if (p instanceof Veterinario v) {
                    for (Map.Entry<DayOfWeek, FranjaHoraria> e : v.getHorarios().entrySet()) {
                        bwH.write(v.getDocumento() + ";" + e.getKey() + ";"
                                + e.getValue().getHoraEntrada() + ";" + e.getValue().getHoraSalida());
                        bwH.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el personal: " + e.getMessage());
        }
    }
}
