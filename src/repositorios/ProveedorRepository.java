package repositorios;

import model.stock.Proveedor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia de proveedores en datos/proveedores.txt.
 */
public class ProveedorRepository {
    private static final String RUTA_PROVEEDORES = "datos/proveedores.txt";

    // Formato: cuit;razonSocial;direccion;email;telefono
    public List<Proveedor> cargarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        File archivo = new File(RUTA_PROVEEDORES);

        if (!archivo.exists()) {
            return lista;   // primera ejecución: todavía no hay proveedores
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                linea = linea.replace("\uFEFF", "");   // saca el carácter invisible (BOM) que agrega el Bloc de notas
                lista.add(Proveedor.desdeLinea(linea));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los proveedores: " + e.getMessage());
        }
        return lista;
    }

    // Reescribe el archivo completo (los datos de contacto pueden cambiar)
    public void guardarTodos(List<Proveedor> proveedores) {
        new File("datos").mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_PROVEEDORES))) {
            for (Proveedor p : proveedores) {
                bw.write(p.toLinea());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los proveedores: " + e.getMessage());
        }
    }
}
