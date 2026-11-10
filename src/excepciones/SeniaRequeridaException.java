package excepciones;

/**
 * Regla de negocio: un dueño con tres ausencias seguidas debe pagar una seña para pedir turno (CU19).
 */
public class SeniaRequeridaException extends Exception {
    public SeniaRequeridaException(String mensaje) { super(mensaje); }
}