package repositorios;

import model.stock.Insumo;
import model.stock.Lote;
import model.stock.Proveedor;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Persistencia de insumos (datos/insumos.txt) y sus lotes (datos/lotes.txt).
 * Se reescribe todo en cada cambio porque el stock de los lotes cambia constantemente.
 */
public class InsumoRepository {
    private static final String RUTA_INSUMOS = "datos/insumos.txt";
    private static final String RUTA_LOTES = "datos/lotes.txt";

    public List<Insumo> cargarTodos(Map<String, Proveedor> proveedoresPorCuit) {
        List<Insumo> insumos = new ArrayList<>();
        Map<String, Insumo> insumosPorCodigo = new HashMap<>();

        cargarInsumos(proveedoresPorCuit, insumos, insumosPorCodigo);
        cargarLotes(insumosPorCodigo);

        return insumos;
    }

    // Formato: codigo;nombre;stockMinimo;costo;precioVenta;cuitProveedor
    private void cargarInsumos(Map<String, Proveedor> proveedoresPorCuit,
                               List<Insumo> insumos, Map<String, Insumo> insumosPorCodigo) {
        File archivo = new File(RUTA_INSUMOS);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.replace("\uFEFF", "").split(";", -1);

                Proveedor proveedor = proveedoresPorCuit.get(p[5]);   // CUIT → objeto
                if (proveedor == null) {
                    System.out.println("Proveedor no encontrado (CUIT " + p[5] + ") para el insumo " + p[0]);
                    continue;
                }

                Insumo insumo = new Insumo(p[0], p[1], Integer.parseInt(p[2]),
                        Double.parseDouble(p[3]), Double.parseDouble(p[4]), proveedor);
                insumos.add(insumo);
                insumosPorCodigo.put(insumo.getCodigo(), insumo);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los insumos: " + e.getMessage());
        }
    }

    // Formato: codigoInsumo;numeroLote;fechaIngreso;fechaVto;cantidadInicial;stockLote
    private void cargarLotes(Map<String, Insumo> insumosPorCodigo) {
        File archivo = new File(RUTA_LOTES);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.replace("\uFEFF", "").split(";", -1);

                Insumo insumo = insumosPorCodigo.get(p[0]);   // código → objeto
                if (insumo == null) {
                    System.out.println("Insumo no encontrado (" + p[0] + ") para el lote " + p[1]);
                    continue;
                }

                Lote lote = new Lote(p[1], LocalDate.parse(p[2]), LocalDate.parse(p[3]),
                        Integer.parseInt(p[4]), Integer.parseInt(p[5]));
                insumo.cargarLote(lote);   // cargarLote y no agregarLote: un lote que venció con el programa cerrado no debe frenar la carga
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los lotes: " + e.getMessage());
        }
    }


    // Reescribe los dos archivos completos (el stock cambia todo el tiempo)
    public void guardarTodos(List<Insumo> insumos) {
        new File("datos").mkdirs();

        try (BufferedWriter bwI = new BufferedWriter(new FileWriter(RUTA_INSUMOS));
             BufferedWriter bwL = new BufferedWriter(new FileWriter(RUTA_LOTES))) {

            for (Insumo i : insumos) {
                bwI.write(i.toLinea());
                bwI.newLine();

                for (Lote l : i.getLotes()) {
                    bwL.write(l.toLinea(i.getCodigo()));
                    bwL.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los insumos: " + e.getMessage());
        }
    }

    // Busca en la lista que ya está en memoria (no relee el archivo)
    public Insumo buscarPorCodigo(List<Insumo> insumos, String codigo) {
        if (codigo == null) return null;
        for (Insumo i : insumos) {
            if (i.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return i;
            }
        }
        return null;
    }
}