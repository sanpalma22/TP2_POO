package repositorios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.stock.Insumo;

public class InsumoRepository {
    private static final String RUTA_INSUMOS = "datos/insumos.txt";

    /**
     * Carga todos los insumos/productos almacenados en el archivo insumos.txt.
     * Retorna una lista vacía si el archivo aún no existe.
     */
    public List<Insumo> cargarTodos() {
        List<Insumo> lista = new ArrayList<>();
        File archivo = new File(RUTA_INSUMOS);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                // Estructura esperada en el TXT:
                // codigo;nombre;stockMinimo;stockActual;costo;precioVenta;proveedor
                String[] p = linea.split(";", -1);

                String codigo = p[0];
                String nombre = p[1];
                int stockMinimo = Integer.parseInt(p[2]);
                int stockActual = Integer.parseInt(p[3]);
                double costo = Double.parseDouble(p[4]);
                double precioVenta = Double.parseDouble(p[5]);
                String proveedor = p[6];

                Insumo insumo = new Insumo(codigo, nombre, stockMinimo, stockActual, costo, precioVenta, proveedor);
                lista.add(insumo);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los insumos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca un insumo específico por su código (ej: "INS-001").
     * Retorna null si no lo encuentra.
     */
    public Insumo buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        List<Insumo> insumos = cargarTodos();
        for (Insumo ins : insumos) {
            if (ins.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return ins;
            }
        }
        return null;
    }
}