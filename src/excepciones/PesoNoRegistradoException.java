package excepciones;

/**
 * Regla de negocio: no se puede indicar un medicamento si en la consulta no se registró
 * el peso, porque la dosis depende de él. Es checked: quien llama está obligado a manejarla.
 */
public class PesoNoRegistradoException extends Exception {
    public PesoNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
