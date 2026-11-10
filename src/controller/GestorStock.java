package controller;

import model.atencion.Vacuna;
import model.enums.Especie;
import model.stock.ComparadorPorStock;
import model.stock.Insumo;
import model.stock.Lote;
import model.stock.Proveedor;
import repositorios.InsumoRepository;
import repositorios.ProveedorRepository;
import repositorios.VacunaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de stock y catálogo (lo usa el administrador): alerta de stock bajo (CU07),
 * ingreso de lotes (CU20), alta de insumos y proveedores, y catálogo de vacunas (CU21).
 */
public class GestorStock {
    private final List<Proveedor> proveedores;
    private final List<Insumo> insumos;
    private final List<Vacuna> vacunas;
    private final ProveedorRepository proveedorRepo;
    private final InsumoRepository insumoRepo;
    private final VacunaRepository vacunaRepo;

    public GestorStock(List<Proveedor> proveedores, List<Insumo> insumos, List<Vacuna> vacunas,
                       ProveedorRepository proveedorRepo, InsumoRepository insumoRepo, VacunaRepository vacunaRepo) {
        this.proveedores = proveedores;
        this.insumos = insumos;
        this.vacunas = vacunas;
        this.proveedorRepo = proveedorRepo;
        this.insumoRepo = insumoRepo;
        this.vacunaRepo = vacunaRepo;
    }

    // Consultas

    public List<Proveedor> getProveedores() { return proveedores; }
    public List<Insumo> getInsumos() { return insumos; }
    public List<Vacuna> getVacunas() { return vacunas; }

    public Proveedor buscarProveedor(String cuit) {
        for (Proveedor p : proveedores) {
            if (p.getCuit().equals(cuit.trim())) return p;
        }
        return null;
    }

    public Insumo buscarInsumo(String codigo) {
        for (Insumo i : insumos) {
            if (i.getCodigo().equalsIgnoreCase(codigo.trim())) return i;
        }
        return null;
    }

    public Vacuna buscarVacuna(String nombre) {
        for (Vacuna v : vacunas) {
            if (v.getNombre().equalsIgnoreCase(nombre.trim())) return v;
        }
        return null;
    }

    // Reporte / alerta de reposición: insumos con stock <= mínimo, los más críticos primero (Comparator)
    public List<Insumo> insumosConStockBajo() {
        List<Insumo> resultado = new ArrayList<>();
        for (Insumo i : insumos) {
            if (i.estaEnStockBajo()) resultado.add(i);
        }
        resultado.sort(new ComparadorPorStock());
        return resultado;
    }

    // Altas

    public Proveedor registrarProveedor(String cuit, String razonSocial, String direccion,
                                        String email, String telefono) {
        if (buscarProveedor(cuit) != null) {
            throw new IllegalArgumentException("Ya existe un proveedor con el CUIT " + cuit);
        }
        Proveedor p = new Proveedor(cuit, razonSocial, direccion, email, telefono);   // valida CUIT obligatorio
        proveedores.add(p);
        proveedorRepo.guardarTodos(proveedores);
        return p;
    }

    public Insumo registrarInsumo(String codigo, String nombre, int stockMinimo, double costo,
                                  double precioVenta, String cuitProveedor) {
        if (buscarInsumo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un insumo con el código " + codigo);
        }
        Proveedor proveedor = buscarProveedor(cuitProveedor);
        if (proveedor == null) {
            throw new IllegalArgumentException("No existe un proveedor con el CUIT " + cuitProveedor);
        }
        if (stockMinimo < 0 || costo < 0 || precioVenta < 0) {
            throw new IllegalArgumentException("Stock mínimo, costo y precio no pueden ser negativos.");
        }
        Insumo insumo = new Insumo(codigo.trim().toUpperCase(), nombre, stockMinimo, costo, precioVenta, proveedor);
        insumos.add(insumo);
        insumoRepo.guardarTodos(insumos);
        return insumo;
    }

    // CU: registrar ingreso de lote. Insumo.agregarLote rechaza lotes vencidos.
    public void registrarIngresoLote(Insumo insumo, String numeroLote, LocalDate fechaVto, int cantidad) {
        for (Lote l : insumo.getLotes()) {
            if (l.getNumeroLote().equalsIgnoreCase(numeroLote)) {
                throw new IllegalArgumentException("El insumo ya tiene un lote " + numeroLote);
            }
        }
        insumo.agregarLote(new Lote(numeroLote, LocalDate.now(), fechaVto, cantidad));
        insumoRepo.guardarTodos(insumos);
    }

    // CU21: alta en el catálogo de vacunas, sin duplicados por nombre
    public Vacuna registrarVacuna(String nombre, String laboratorio, List<Especie> especies, int edadMinMeses,
                                  int intervaloRefuerzoDias, double costo, double precioVenta) {
        if (buscarVacuna(nombre) != null) {
            throw new IllegalArgumentException("Ya existe la vacuna " + nombre + " en el catálogo.");
        }
        if (especies.isEmpty()) {
            throw new IllegalArgumentException("La vacuna debe aplicarse al menos a una especie.");
        }
        Vacuna v = new Vacuna(nombre, laboratorio, especies, edadMinMeses, intervaloRefuerzoDias, costo, precioVenta);
        vacunas.add(v);
        vacunaRepo.guardarTodas(vacunas);
        return v;
    }
}
