package excepciones;

/**
 * Regla de negocio: el veterinario no puede tener dos turnos superpuestos
 * ni atender fuera de su horario.
 */
public class TurnoSuperpuestoException extends Exception {
    public TurnoSuperpuestoException(String mensaje) { super(mensaje); }
}