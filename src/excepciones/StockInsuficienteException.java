package excepciones;

/**
 * Regla de negocio: no se puede aplicar ni vender un insumo sin stock disponible.
 */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
