import model.atencion.AplicacionVacuna;
import model.atencion.Vacuna;
import model.enums.Especie;
import model.enums.InteriorExterior;
import model.enums.Sexo;
import model.enums.Tamano;
import model.mascotas.Gato;
import model.mascotas.Perro;
import model.personas.Duenio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VacunacionTest {
    private final Duenio ana = new Duenio("Ana", "Paz", "111", "115", "a@a.com", false, 0);
    private final Vacuna antirrabica = new Vacuna("Antirrábica", "Bagó", List.of(Especie.PERRO, Especie.GATO), 3, 365, 3000, 6000);
    private final Vacuna sextuple = new Vacuna("Séxtuple", "Zoetis", List.of(Especie.PERRO), 2, 365, 4000, 8000);

    @Test
    void elRefuerzoSeCalculaSegunElIntervalo() {
        AplicacionVacuna a = new AplicacionVacuna(antirrabica, LocalDate.of(2026, 1, 10), "L1");
        assertEquals(LocalDate.of(2027, 1, 10), a.getFechaRefuerzo());
    }

    @Test
    void recordatorioDesde7DiasAntesDelRefuerzo() {
        AplicacionVacuna a = new AplicacionVacuna(antirrabica, LocalDate.of(2025, 1, 10), LocalDate.of(2026, 1, 10), "L1");
        assertFalse(a.requiereRecordatorio(LocalDate.of(2026, 1, 2)));   // faltan 8 días
        assertTrue(a.requiereRecordatorio(LocalDate.of(2026, 1, 3)));    // faltan 7 días
    }

    @Test
    void vencidaPasados30DiasSinRefuerzo() {
        AplicacionVacuna a = new AplicacionVacuna(antirrabica, LocalDate.of(2025, 1, 10), LocalDate.of(2026, 1, 10), "L1");
        assertFalse(a.estaVencida(LocalDate.of(2026, 2, 9)));            // día 30
        assertTrue(a.estaVencida(LocalDate.of(2026, 2, 10)));            // día 31
    }

    @Test
    void noSeAplicaAUnaEspecieQueNoCorresponde() {
        Gato michi = new Gato(ana, "Michi", "Siamés", Sexo.HEMBRA, LocalDate.of(2020, 1, 1), 4, InteriorExterior.INTERIOR);
        assertFalse(michi.esAptaParaVacuna(sextuple, LocalDate.of(2026, 1, 1)));   // Séxtuple es solo para perros
        assertTrue(michi.esAptaParaVacuna(antirrabica, LocalDate.of(2026, 1, 1)));
    }

    @Test
    void noSeAplicaSiNoTieneLaEdadMinima() {
        Perro cachorro = new Perro(ana, "Toby", "Caniche", Sexo.MACHO, LocalDate.of(2026, 1, 1), 2, "CH1", Tamano.CHICO);
        assertFalse(cachorro.esAptaParaVacuna(antirrabica, LocalDate.of(2026, 3, 1)));   // 2 meses, mínimo 3
        assertTrue(cachorro.esAptaParaVacuna(antirrabica, LocalDate.of(2026, 4, 1)));    // 3 meses
    }
}
