package facturacion;

public class ImpresionFactura {
    public void imprimirFactura(Factura factura) {
        if (factura == null) {
            System.out.println("No hay factura para imprimir.");
            return;
        }
        System.out.println("Factura Nro: " + factura.getNumero());
        System.out.println("Fecha: " + factura.getFecha());
        System.out.println("Cliente: " + factura.getCliente().getNombre() + " - " + factura.getCliente().getDocumento());
        System.out.println("Metodo de pago: " + factura.getMetodoPago().getNombre());
        System.out.printf("%-4s %-25s %-5s %-10s %-10s\n", "Item", "Descripción", "Cant", "P.Unit", "Importe");
        System.out.println("-------------------------------------------------------");
        for (DetalleFactura detalle : factura.getLineasFactura()) {
            System.out.println(detalle.toString());
        }
        System.out.println("-------------------------------------------------------");
        System.out.printf("Subtotal:       $%.2f\n", factura.getSubtotal());
        System.out.printf("Descuento:     -$%.2f\n", factura.getDtoAplicado());
        System.out.printf("Recargo:       +$%.2f\n", factura.getCargoAplicado());
        System.out.printf("TOTAL:          $%.2f\n", factura.getTotal());
        System.out.println("=======================================================");
    }
}