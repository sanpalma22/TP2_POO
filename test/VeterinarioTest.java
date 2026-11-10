import model.enums.TipoAtencion;
import model.personas.FranjaHoraria;
import model.personas.Veterinario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class VeterinarioTest {
    private static final LocalDate LUNES = LocalDate.of(2030, 1, 7);
    private static final LocalDate MARTES = LUNES.plusDays(1);
    private Veterinario laura;

    @BeforeEach
    void setUp() {
        laura = new Veterinario("Laura", "Gómez", "201", "1", "MP-1", "Clínica");
        laura.agregarHorario(DayOfWeek.MONDAY, new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(19, 0)));
    }

    @Test
    void minutosRestantesCuentaHorasYMinutos() {
        FranjaHoraria franja = new FranjaHoraria(LocalTime.of(9, 0), LocalTime.of(19, 0));
        assertEquals(510, franja.minutosRestantes(LocalTime.of(10, 30)));
    }

    @Test
    void disponibleDentroDeSuHorario() {
        assertTrue(laura.horarioDisponible(LUNES, LocalTime.of(10, 30), TipoAtencion.CONSULTA_GRAL));
    }

    @Test
    void noDisponibleUnDiaQueNoAtiende() {
        assertFalse(laura.horarioDisponible(MARTES, LocalTime.of(10, 30), TipoAtencion.CONSULTA_GRAL));
    }

    @Test
    void noDisponibleFueraDeSuHorario() {
        assertFalse(laura.horarioDisponible(LUNES, LocalTime.of(8, 0), TipoAtencion.CONSULTA_GRAL));
        assertFalse(laura.horarioDisponible(LUNES, LocalTime.of(19, 0), TipoAtencion.CONSULTA_GRAL));
    }

    @Test
    void noDisponibleSiLaAtencionNoTerminaAntesDeSalir() {
        assertFalse(laura.horarioDisponible(LUNES, LocalTime.of(18, 0), TipoAtencion.CIRUGIA));       // 120 min
        assertTrue(laura.horarioDisponible(LUNES, LocalTime.of(18, 30), TipoAtencion.CONSULTA_GRAL)); // justo 30 min
    }
}
