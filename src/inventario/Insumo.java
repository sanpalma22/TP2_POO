package inventario;

public class Insumo {
    private String codigo;
    private String nombre;
    private int stockMinimo;
    private int stockActual;
    private double costo;
    private double precioVenta;
    private String proveedor;

    public Insumo(String codigo, String nombre, int stockMinimo, int stockActual, double costo, double precioVenta, String proveedor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stockMinimo = stockMinimo;
        this.stockActual = stockActual;
        this.costo = costo;
        this.precioVenta = precioVenta;
        this.proveedor = proveedor;
    }

    public boolean stockBajo(){
        return stockActual<=stockMinimo;
    }
    public void descontarStock(int cantidad){
        if(cantidad<=stockActual){
            this.stockActual-=cantidad;
        }else{
            System.out.println("No hay stock suficiente");
        }
    }

    //lote proximo a vencer


    public double getPrecioVenta() {
        return precioVenta;
    }
}
