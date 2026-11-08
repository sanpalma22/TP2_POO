package view.VistaRecepcionista;

import controller.FacturacionController;
import model.enums.MetodoPago;
import model.enums.TipoAtencion;
import model.facturacion.Factura;
import model.personas.Duenio;
import model.stock.Insumo;
import repositorios.DuenioRepository;
import repositorios.InsumoRepository;

import java.util.List;
import java.util.Scanner;

public class FacturacionView {
    // Inicializamos las variables directamente para evitar errores de NullPointerException
    private FacturacionController controller = new FacturacionController();
    private Scanner scanner = new Scanner(System.in);
    private DuenioRepository repoDuenio = new DuenioRepository();
    private InsumoRepository repoInsumos = new InsumoRepository();

    // Le cambiamos el nombre a "generarFactura" para que coincida con el Menú
    public void generarFactura() {
        System.out.println("Nueva factura");

        // 1. Ingrese y busque al dueño por DNI
        System.out.print("Ingrese el DNI del dueño: ");
        String dni = scanner.nextLine().trim();
        Duenio cliente = repoDuenio.buscarPorDocumento(dni);

        if (cliente == null) {
            System.out.println("Error: No se encontró ningún cliente registrado con el DNI '" + dni + "'.");
            System.out.println("Operación cancelada.");
            return;
        }

        System.out.println("Cliente seleccionado: " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Seleccione el Método de Pago:");
        System.out.println("1. Efectivo");
        System.out.println("2. Débito");
        System.out.println("3. Crédito");
        System.out.print("Opción: ");
        int opcionPago = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        MetodoPago metodoPago = switch (opcionPago) {
            case 1 -> MetodoPago.EFECTIVO;
            case 3 -> MetodoPago.CREDITO;
            default -> MetodoPago.DEBITO;
        };

        // Iniciar Factura
        Factura factura = controller.iniciarFactura(cliente, metodoPago);

        // 2. Pregunta por el Tipo de Atención/Consulta
        System.out.println("\nTIPO DE ATENCIÓN");
        System.out.println("1. CONSULTA GENERAL");
        System.out.println("2. VACUNACIÓN");
        System.out.println("3. CIRUGÍA");
        System.out.println("4. Solo compra de productos");
        System.out.print("Opción: ");
        int opcionAtencion = scanner.nextInt();
        scanner.nextLine();

        if (opcionAtencion >= 1 && opcionAtencion <= 3) {
            TipoAtencion servicio = switch (opcionAtencion) {
                case 1 -> TipoAtencion.CONSULTA_GRAL;
                case 2 -> TipoAtencion.VACUNACION;
                case 3 -> TipoAtencion.CIRUGIA;
                default -> null;
            };
            controller.agregarServicioAFactura(factura, servicio);
        }

        System.out.println("\nCARGA DE INSUMOS, VACUNAS Y MEDICAMENTOS");
        boolean fin = false;
        while (!fin) {
            System.out.print("¿Desea agregar un insumo, vacuna o medicamento a la factura? (s/n): ");
            String resp = scanner.nextLine();

            if (resp.equalsIgnoreCase("s")) {
                mostrarInsumos();
                System.out.print("Ingrese el código del producto/insumo: ");
                String cod = scanner.nextLine();
                Insumo prod = repoInsumos.buscarPorCodigo(cod);

                if (prod != null) {
                    System.out.print("Cantidad: ");
                    int cant = scanner.nextInt();
                    scanner.nextLine();
                    controller.agregarInsumoAFactura(factura, prod, cant);
                    System.out.println("Ítem agregado a la factura.");
                } else {
                    System.out.println("Error: Producto no encontrado.");
                }
            } else {
                fin = true;
            }
        }

        // Cierre y Emisión
        controller.finalizarFactura(factura);

        System.out.println("\nCarga finalizada. Imprimiendo comprobante...");
        controller.imprimirComprobante(factura);
    }

    private void mostrarInsumos() {
        List<Insumo> lista = repoInsumos.cargarTodos();
        if (lista != null && !lista.isEmpty()) {
            System.out.println("Disponibles:");
            for (Insumo i : lista) {
                System.out.println("[" + i.getCodigo() + "] " + i.getNombre() + " - $" + i.getPrecioVenta());
            }
        } else {
            System.out.println("No hay insumos registrados en la base de datos.");
        }
    }
}