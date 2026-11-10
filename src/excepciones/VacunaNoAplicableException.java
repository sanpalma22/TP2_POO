package excepciones;

/**
 * Regla de negocio: no se aplica una vacuna a una especie que no corresponde
 * ni a un animal menor a la edad mínima (CU10).
 */
public class VacunaNoAplicableException extends Exception {
    public VacunaNoAplicableException(String mensaje) { super(mensaje); }
}