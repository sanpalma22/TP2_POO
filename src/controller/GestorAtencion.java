package controller;

import excepciones.PesoNoRegistradoException;
import excepciones.StockInsuficienteException;
import excepciones.VacunaNoAplicableException;
import model.atencion.*;
import model.mascotas.Mascota;
import model.personas.Duenio;
import model.personas.Veterinario;
import model.stock.Insumo;
import repositorios.HistoriaClinicaRepository;
import repositorios.InsumoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador (GRASP) de la atención veterinaria: registrar consulta (CU03),
 * indicar tratamiento (CU16), aplicar vacuna (CU10), consultar historia clínica (CU17)
 * y recordatorios de vacunas (CU08). La vista solo habla con esta clase y esta clase
 * coordina el modelo y los repositorios.
 */
public class GestorAtencion {
    private final HistoriaClinicaRepository historiaRepo;
    private final InsumoRepository insumoRepo;
    private final GestorClientes gestorClientes;
    private final List<Insumo> insumos;
    private final List<Vacuna> vacunas;
    private final List<Veterinario> veterinarios;

    public GestorAtencion(HistoriaClinicaRepository historiaRepo, InsumoRepository insumoRepo,
                          GestorClientes gestorClientes, List<Insumo> insumos,
                          List<Vacuna> vacunas, List<Veterinario> veterinarios) {
        this.historiaRepo = historiaRepo;
        this.insumoRepo = insumoRepo;
        this.gestorClientes = gestorClientes;
        this.insumos = insumos;
        this.vacunas = vacunas;
        this.veterinarios = veterinarios;
    }

    // BÚSQUEDAS (las usa la vista)

    public List<Veterinario> getVeterinarios() { return veterinarios; }

    public Veterinario buscarVeterinario(String documento) {
        for (Veterinario v : veterinarios) {
            if (v.getDocumento().equals(documento.trim())) return v;
        }
        return null;
    }

    public Duenio buscarDuenio(String dni) {
        return gestorClientes.buscarDuenio(dni.trim());
    }

    public List<Mascota> mascotasDe(Duenio duenio) {
        return gestorClientes.mascotasDe(duenio);
    }

    public List<Insumo> getInsumos() { return insumos; }

    public Insumo buscarInsumo(String codigo) {
        for (Insumo i : insumos) {
            if (i.getCodigo().equalsIgnoreCase(codigo.trim())) return i;
        }
        return null;
    }

    // Solo las vacunas del catálogo que corresponden a la especie de la mascota
    public List<Vacuna> vacunasPara(Mascota mascota) {
        List<Vacuna> resultado = new ArrayList<>();
        for (Vacuna v : vacunas) {
            if (v.aplicaAEspecie(mascota.getEspecie())) resultado.add(v);
        }
        return resultado;
    }

    // Casos de uso

    // Registrar consulta: se pesa a la mascota y la historia clínica crea la consulta (Creador)
    public Consulta iniciarConsulta(Mascota mascota, Veterinario veterinario, String motivo, int pesoDia) {
        mascota.setPeso(pesoDia);   // lanza IllegalArgumentException si el peso no es positivo
        Consulta consulta = mascota.getHistoriaClinica()
                .crearConsulta(LocalDate.now(), motivo, pesoDia, "Pendiente", "");
        consulta.asignarVeterinario(veterinario);
        return consulta;
    }

    // Solo un veterinario puede registrar el diagnóstico: el parámetro es de tipo Veterinario
    public void registrarDiagnostico(Consulta consulta, Veterinario veterinario,
                                     String diagnostico, String observaciones) {
        consulta.registrarDiagnostico(veterinario, diagnostico, observaciones);
    }

    // Indica el tratamiento; el stock NO se descuenta acá sino al finalizar (regla de negocio)
    public void indicarTratamiento(Consulta consulta, Insumo medicamento, String dosis,
                                   String frecuencia, String duracion) throws PesoNoRegistradoException {
        consulta.agregarTratamiento(new Tratamiento(medicamento, dosis, frecuencia, duracion));
    }

    // Aplicar vacuna: valida especie y edad mínima con el polimorfismo de Mascota
    public AplicacionVacuna aplicarVacuna(Consulta consulta, Mascota mascota, Vacuna vacuna, String lote)
            throws VacunaNoAplicableException {
        LocalDate hoy = LocalDate.now();
        if (!mascota.esAptaParaVacuna(vacuna, hoy)) {
            throw new VacunaNoAplicableException("La vacuna " + vacuna.getNombre() + " no se puede aplicar a "
                    + mascota.getNombre() + " (especie incompatible o no tiene la edad mínima de "
                    + vacuna.getEdadMinMeses() + " meses).");
        }
        AplicacionVacuna aplicacion = new AplicacionVacuna(vacuna, hoy, lote);   // calcula el refuerzo
        consulta.agregarAplicacionVacuna(aplicacion);
        return aplicacion;
    }

    /*
     * Finalizar consulta:
     *  1. Verifica que haya stock de TODOS los medicamentos (para no descontar a medias).
     *  2. Descuenta el stock (FIFO por lote) y guarda insumos.txt / lotes.txt.
     *  3. Guarda la consulta en la historia clínica (append) y el peso nuevo de la mascota.
     * Supuesto: cada tratamiento consume 1 unidad del medicamento.
     */
    public void finalizarConsulta(Consulta consulta, Mascota mascota) throws StockInsuficienteException {
        Map<Insumo, Integer> necesarios = new HashMap<>();
        for (Tratamiento t : consulta.getTratamientos()) {
            necesarios.merge(t.getMedicamento(), 1, Integer::sum);
        }
        for (Map.Entry<Insumo, Integer> e : necesarios.entrySet()) {
            if (e.getKey().getStockActual() < e.getValue()) {
                throw new StockInsuficienteException("No hay stock suficiente de " + e.getKey().getNombre()
                        + " (disponible: " + e.getKey().getStockActual() + ", necesario: " + e.getValue() + ")");
            }
        }

        for (Tratamiento t : consulta.getTratamientos()) {
            t.utilizarInsumo(1);
        }
        insumoRepo.guardarTodos(insumos);

        historiaRepo.guardarConsulta(mascota.getIdMascota(), consulta);
        gestorClientes.guardarMascotas();
    }

    // Si se abandona la consulta sin finalizar, se saca de la historia (todavía no se guardó)
    public void descartarConsulta(Consulta consulta, Mascota mascota) {
        mascota.getHistoriaClinica().getConsultas().remove(consulta);
    }

    /*
     * Recordatorios de vacunas (CU08) y reporte del administrador.
     * Por cada mascota y vacuna se mira solo la ÚLTIMA aplicación (las anteriores ya fueron reforzadas).
     * Incluye las que vencen en los próximos 7 días y las ya vencidas (más de 30 días sin refuerzo).
     */
    public List<RecordatorioVacuna> vacunasPorVencer(LocalDate hoy) {
        List<RecordatorioVacuna> resultado = new ArrayList<>();
        for (Mascota m : gestorClientes.getMascotas()) {
            Map<String, AplicacionVacuna> ultimaPorVacuna = new HashMap<>();
            for (Consulta c : m.getHistoriaClinica().getConsultas()) {
                for (AplicacionVacuna a : c.getVacunas()) {
                    AplicacionVacuna previa = ultimaPorVacuna.get(a.getVacuna().getNombre());
                    if (previa == null || a.getFecha().isAfter(previa.getFecha())) {
                        ultimaPorVacuna.put(a.getVacuna().getNombre(), a);
                    }
                }
            }
            for (AplicacionVacuna a : ultimaPorVacuna.values()) {
                if (a.requiereRecordatorio(hoy) || a.estaVencida(hoy)) {
                    resultado.add(new RecordatorioVacuna(m, a));
                }
            }
        }
        resultado.sort((r1, r2) -> r1.getAplicacion().getFechaRefuerzo().compareTo(r2.getAplicacion().getFechaRefuerzo()));
        return resultado;
    }

    public List<Consulta> historiaClinica(Mascota mascota) {
        return mascota.getHistoriaClinica().getConsultas();
    }
}
