import model.enums.EstadoTurno;
import model.enums.InteriorExterior;
import model.enums.Sexo;
import model.enums.TipoAtencion;
import model.mascotas.Gato;
import model.mascotas.Mascota;
import model.personas.Duenio;
import model.personas.Veterinario;
import model.turnos.Turno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TurnoTest {
    private static final LocalDate LUNES = LocalDate.of(2030, 1, 7);
    private Mascota michi;
    private Veterinario laura;
    private Veterinario martin;

    @BeforeEach
    void setUp() {
        Duenio ana = new Duenio("Ana", "Paz", "111", "115", "a@a.com", false, 0);
        michi = new Gato(ana, "Michi", "Siamés", Sexo.HEMBRA, LocalDate.of(2022, 1, 1), 4, InteriorExterior.INTERIOR);
        laura = new Veterinario("Laura", "Gómez", "201", "1", "MP-1", "Clínica");
        martin = new Veterinario("Martín", "Ruiz", "202", "2", "MP-2", "Cirugía");
    }

    private Turno turno(Veterinario v, int hora, int minuto, TipoAtencion tipo) {
        return new Turno(michi, v, LUNES, LocalTime.of(hora, minuto), tipo, false);
    }

    @Test
    void laDuracionDependeDelTipoDeAtencion() {
        assertEquals(LocalTime.of(10, 30), turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL).getHoraFin());
        assertEquals(LocalTime.of(10, 15), turno(laura, 10, 0, TipoAtencion.VACUNACION).getHoraFin());
        assertEquals(LocalTime.of(12, 0), turno(laura, 10, 0, TipoAtencion.CIRUGIA).getHoraFin());
    }

    @Test
    void mismoVeterinarioYHorarioQueSePisaSeSuperpone() {
        Turno a = turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL);   // 10:00-10:30
        Turno b = turno(laura, 10, 15, TipoAtencion.VACUNACION);    // 10:15-10:30
        assertTrue(a.seSuperponeCon(b));
    }

    @Test
    void turnosConsecutivosNoSeSuperponen() {
        Turno a = turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL);   // termina 10:30
        Turno b = turno(laura, 10, 30, TipoAtencion.CONSULTA_GRAL);  // empieza 10:30
        assertFalse(a.seSuperponeCon(b));
    }

    @Test
    void distintosVeterinariosNoSeSuperponen() {
        assertFalse(turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL)
                .seSuperponeCon(turno(martin, 10, 0, TipoAtencion.CONSULTA_GRAL)));
    }

    @Test
    void unTurnoCanceladoNoOcupaElHorario() {
        Turno a = turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL);
        a.cancelarSinCargo();
        assertFalse(a.seSuperponeCon(turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL)));
    }

    @Test
    void cancelarConMasDeDosHorasNoTieneCargo() {
        Turno t = turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL);
        double cargo = t.cancelar(LocalDateTime.of(LUNES, LocalTime.of(7, 59)));
        assertEquals(0.0, cargo, 0.01);
        assertEquals(EstadoTurno.CANCELADO, t.getEstado());
    }

    @Test
    void cancelarConMenosDeDosHorasCobraEl20PorCiento() {
        Turno t = turno(laura, 10, 0, TipoAtencion.CONSULTA_GRAL);
        double cargo = t.cancelar(LocalDateTime.of(LUNES, LocalTime.of(8, 30)));
        assertEquals(3000.0, cargo, 0.01);   // 20 % de 15000
    }

    @Test
    void ordenNaturalPorFechaYHora() {
        Turno tarde = turno(laura, 15, 0, TipoAtencion.CONSULTA_GRAL);
        Turno temprano = turno(laura, 9, 0, TipoAtencion.CONSULTA_GRAL);
        assertTrue(temprano.compareTo(tarde) < 0);
    }
}
