package model.facturacion;

/**
 * Patrón Strategy (principio Abierto/Cerrado): cada descuento o recargo es una clase
 * que implementa esta interfaz. Para agregar uno nuevo se crea otra clase sin tocar Factura.
 */
public interface EstrategiaDtoRec {
    double calcularMontoFinal(double monto);
}
