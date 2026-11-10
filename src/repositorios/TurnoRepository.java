package repositorios;

import model.enums.EstadoTurno;
import model.enums.TipoAtencion;
import model.mascotas.Mascota;
import model.personas.Veterinario;
import model.turnos.Turno;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Persistencia de turnos en datos/turnos.txt. Se reescribe el archivo completo
 * porque los turnos cambian de estado, fecha y hora.
 */
public class TurnoRepository {
    private static final String RUTA_TURNOS = "datos/turnos.txt";

    // Formato: idTurno;idMascota;documentoVet;fecha;hora;TIPO;ESTADO;urgente
    public List<Turno> cargarTodos(Map<Integer, Mascota> mascotasPorId, Map<String, Veterinario> veterinariosPorDocumento) {
        List<Turno> lista = new ArrayList<>();
        File archivo = new File(RUTA_TURNOS);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.replace("﻿", "").split(";", -1);

                Mascota mascota = mascotasPorId.get(Integer.parseInt(p[1]));       // id → objeto
                Veterinario veterinario = veterinariosPorDocumento.get(p[2]);       // documento → objeto
                if (mascota == null || veterinario == null) {
                    System.out.println("Turno " + p[0] + " ignorado: mascota o veterinario inexistente.");
                    continue;
                }
                lista.add(new Turno(Integer.parseInt(p[0]), mascota, veterinario,
                        LocalDate.parse(p[3]), LocalTime.parse(p[4]),
                        TipoAtencion.valueOf(p[5]), EstadoTurno.valueOf(p[6]), Boolean.parseBoolean(p[7])));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los turnos: " + e.getMessage());
        }
        return lista;
    }

    // Reescribe el archivo completo (los turnos cambian de estado, fecha y hora)
    public void guardarTodos(List<Turno> turnos) {
        new File("datos").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_TURNOS))) {
            for (Turno t : turnos) {
                bw.write(t.toLinea());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los turnos: " + e.getMessage());
        }
    }
}
