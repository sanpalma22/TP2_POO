import excepciones.PesoNoRegistradoException;
import model.atencion.Consulta;
import model.atencion.Tratamiento;
import model.enums.Sexo;
import model.enums.Tamano;
import model.mascotas.Perro;
import model.personas.Duenio;
import model.stock.Insumo;
import model.stock.Proveedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AtencionTest {
    private final Insumo amoxicilina = new Insumo("MED001", "Amoxicilina", 10, 800, 1500,
            new Proveedor("30-1", "Prov", "", "", ""));

    @Test
    void noSeIndicaMedicamentoSinPesoRegistrado() {
        Consulta sinPeso = new Consulta(LocalDate.now(), "Control", 0, "", "");
        assertThrows(PesoNoRegistradoException.class,
                () -> sinPeso.agregarTratamiento(new Tratamiento(amoxicilina, "5ml", "12hs", "7 días")));
        assertEquals(0, sinPeso.getTratamientos().size());
    }

    @Test
    void conPesoSeIndicaElTratamiento() throws PesoNoRegistradoException {
        Consulta c = new Consulta(LocalDate.now(), "Control", 8, "", "");
        c.agregarTratamiento(new Tratamiento(amoxicilina, "5ml", "12hs", "7 días"));
        assertEquals(1, c.getTratamientos().size());
    }

    @Test
    void laHistoriaClinicaCreaLaConsulta() {
        Duenio ana = new Duenio("Ana", "Paz", "111", "115", "a@a.com", false, 0);
        Perro toby = new Perro(ana, "Toby", "Caniche", Sexo.MACHO, LocalDate.of(2020, 1, 1), 8, "CH1", Tamano.CHICO);
        Consulta c = toby.getHistoriaClinica().crearConsulta(LocalDate.now(), "Control", 9, "Sano", "");
        assertEquals(1, toby.getHistoriaClinica().getConsultas().size());
        assertSame(c, toby.getHistoriaClinica().getConsultas().get(0));
    }

    @Test
    void elPesoDebeSerPositivo() {
        Duenio ana = new Duenio("Ana", "Paz", "111", "115", "a@a.com", false, 0);
        Perro toby = new Perro(ana, "Toby", "Caniche", Sexo.MACHO, LocalDate.of(2020, 1, 1), 8, "CH1", Tamano.CHICO);
        assertThrows(IllegalArgumentException.class, () -> toby.setPeso(0));
        assertEquals(8, toby.getPeso());
    }

    @Test
    void tresAusenciasSeguidasExigenSenia() {
        Duenio ana = new Duenio("Ana", "Paz", "111", "115", "a@a.com", false, 0);
        ana.registrarAusencia();
        ana.registrarAusencia();
        assertFalse(ana.debePagarSenia());
        ana.registrarAusencia();
        assertTrue(ana.debePagarSenia());
        ana.registrarAsistencia();                 // vuelve a cero: son ausencias SEGUIDAS
        assertFalse(ana.debePagarSenia());
    }
}
