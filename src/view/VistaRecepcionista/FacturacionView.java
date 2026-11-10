package view.VistaRecepcionista;

import controller.GestorFacturacion;
import model.enums.MetodoPago;
import model.enums.TipoAtencion;
import model.facturacion.Factura;
import model.personas.Duenio;
import model.stock.Insumo;

import java.util.List;
import java.util.Scanner;

/**
 * Pantalla para armar e imprimir una factura (CU04).
 */
public class FacturacionView {
    private final GestorFacturacion gestor;
    private final Scanner scanner;

    public FacturacionView(Scanner scanner, GestorFacturacion gestor) {
        this.scanner = scanner;
        this.gestor = gestor;
    }

    public void generarFactura() {
        System.out.println("Nueva factura");

        // 1. Cliente y medio de pago
        System.out.print("Ingrese el DNI del dueño: ");
        String dni = scanner.nextLine().trim();
        Duenio cliente = gestor.buscarCliente(dni);

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
        int opcionPago = Integer.parseInt(scanner.nextLine().trim());

        MetodoPago metodoPago = switch (opcionPago) {
            case 1 -> MetodoPago.EFECTIVO;
            case 3 -> MetodoPago.CREDITO;
            default -> MetodoPago.DEBITO;
        };

        Factura factura = gestor.iniciarFactura(cliente, metodoPago);

        // 2. Servicio (opcional: puede ser solo compra de productos)
        System.out.println("\nTIPO DE ATENCIÓN");
        System.out.println("1. CONSULTA GENERAL");
        System.out.println("2. VACUNACIÓN");
        System.out.println("3. CIRUGÍA");
        System.out.println("4. Solo compra de productos");
        System.out.print("Opción: ");
        int opcionAtencion = Integer.parseInt(scanner.nextLine().trim());

        if (opcionAtencion >= 1 && opcionAtencion <= 3) {
            TipoAtencion servicio = switch (opcionAtencion) {
                case 1 -> TipoAtencion.CONSULTA_GRAL;
                case 2 -> TipoAtencion.VACUNACION;
                case 3 -> TipoAtencion.CIRUGIA;
                default -> null;
            };
            gestor.agregarServicioAFactura(factura, servicio);
        }

        // 3. Productos
        System.out.println("\nCARGA DE INSUMOS, VACUNAS Y MEDICAMENTOS");
        boolean fin = false;
        while (!fin) {
            System.out.print("¿Desea agregar un insumo, vacuna o medicamento a la factura? (s/n): ");
            String resp = scanner.nextLine();

            if (resp.equalsIgnoreCase("s")) {
                mostrarInsumos();
                System.out.print("Ingrese el código del producto/insumo: ");
                String cod = scanner.nextLine();
                Insumo prod = gestor.buscarInsumo(cod);

                if (prod != null) {
                    System.out.print("Cantidad: ");
                    int cant = Integer.parseInt(scanner.nextLine().trim());
                    gestor.agregarInsumoAFactura(factura, prod, cant);
                    System.out.println("Ítem agregado a la factura.");
                } else {
                    System.out.println("Error: Producto no encontrado.");
                }
            } else {
                fin = true;
            }
        }

        // 4. Cierre: calcula subtotal y descuento o recargo, e imprime
        gestor.finalizarFactura(factura);

        System.out.println("\nCarga finalizada. Imprimiendo comprobante...");
        gestor.imprimirComprobante(factura);
    }

    private void mostrarInsumos() {
        List<Insumo> lista = gestor.getInsumos();
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