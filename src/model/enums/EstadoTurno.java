package model.enums;

/**
 * Ciclo de vida de un turno: arranca PROGRAMADO y termina ATENDIDO, AUSENTE o CANCELADO.
 */
public enum EstadoTurno {
    PROGRAMADO, ATENDIDO, CANCELADO, AUSENTE
}