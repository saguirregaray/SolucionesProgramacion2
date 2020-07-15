package uy.um.prog2.examen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uy.edu.um.adt.linkedlist.MyList;
import uy.edu.um.adt.queue.EmptyQueueException;
import uy.edu.um.adt.queue.MyQueue;
import uy.um.prog2.examen.Exceptions.InformacionInvalida;
import uy.um.prog2.examen.Exceptions.SocioNoExiste;
import uy.um.prog2.examen.Exceptions.SocioYaAfiliado;
import uy.um.prog2.examen.Utils.PlanAfiliacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

public class ClubDeportivoMgrTest {

    ClubDeportivoMgr club = new ClubDeportivoMgr();

    @Before
    public void setUp() throws Exception {
        club.afiliarSocio(49216836L, "Juan Rodriguez", PlanAfiliacion.PLAN_FIN_DE_SEMANA, LocalDate.now());
        club.afiliarSocio(59216836L, "Juana Rodriguez", PlanAfiliacion.PLAN_LIBRE, LocalDate.now());
        club.afiliarSocio(69216836L, "Juanita Rodriguez", PlanAfiliacion.PLAN_FIN_DE_SEMANA, LocalDate.now());
        club.afiliarSocio(47216836L, "Juanito Rodriguez", PlanAfiliacion.PLAN_LIBRE, LocalDate.now());
        club.afiliarSocio(48216836L, "Juanardo Rodriguez", PlanAfiliacion.PLAN_FIN_DE_SEMANA, LocalDate.now());
        club.afiliarSocio(43216836L, "Juanberto Rodriguez", PlanAfiliacion.PLAN_LIBRE, LocalDate.now());
        club.afiliarSocio(49217636L, "Juanolo Rodriguez", PlanAfiliacion.PLAN_LIBRE, LocalDate.now());
    }

    @After
    public void tearDown() throws Exception {
        club = new ClubDeportivoMgr();
    }

    @Test
    public void afiliarSocioFlujoNormal() throws SocioYaAfiliado, InformacionInvalida {

        assertEquals(7, club.getSocios().size());
        assertTrue(club.getSocios().contains(new Socio(49216836L, "Juan Rodriguez", PlanAfiliacion.PLAN_FIN_DE_SEMANA, LocalDate.now())));
        assertTrue(club.getSocios().contains(new Socio(47216836L, "Juanito Rodriguez", PlanAfiliacion.PLAN_LIBRE, LocalDate.now())));

    }

    @Test(expected = SocioYaAfiliado.class)
    public void afiliarSocioFlujoAlternativo1() throws SocioYaAfiliado, InformacionInvalida {

        club.afiliarSocio(59216836L, "Juana Rodriguez", PlanAfiliacion.PLAN_LIBRE, LocalDate.now());

    }

    @Test(expected = InformacionInvalida.class)
    public void afiliarSocioFlujoAlternativo2() throws SocioYaAfiliado, InformacionInvalida {

        club.afiliarSocio(59216836L, null, PlanAfiliacion.PLAN_LIBRE, LocalDate.now());

    }

    @Test
    public void validarEntradaClubFlujoNormal() {
        try {
            assertFalse(club.validarEntradaClub(49216836L, LocalDateTime.now()));
            assertTrue(club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32)));
            assertTrue(club.validarEntradaClub(59216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32)));
            assertTrue(club.validarEntradaClub(69216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32)));
            assertTrue(club.validarEntradaClub(47216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32)));
        } catch (SocioNoExiste | InformacionInvalida socioNoExiste) {
            socioNoExiste.printStackTrace();
        }

    }

    @Test(expected = SocioNoExiste.class)
    public void validarEntradaClubFlujoAlternativo1() throws InformacionInvalida, SocioNoExiste {

        club.validarEntradaClub(492833336L, LocalDateTime.now());

    }

    @Test(expected = InformacionInvalida.class)
    public void validarEntradaClubFlujoAlternativo2() throws InformacionInvalida, SocioNoExiste {

        club.validarEntradaClub(4336L, LocalDateTime.now());

    }

    @Test
    public void cargarSociosConBajaAsistenciaFlujoNormal() throws InformacionInvalida, SocioNoExiste {
        //Cargo asistencias base
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(59216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(69216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(47216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 4, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 5, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 11, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 12, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 19, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 25, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 26, 20, 32));

        MyQueue<Socio> sociosBajaAsistencia = club.getSociosBajaAsistencia();
        club.cargarSociosConBajaAsistencia();

        assertEquals(6, club.getSociosBajaAsistencia().size());
        assertTrue(sociosBajaAsistencia.contains(new Socio(48216836L, "Juanardo Rodriguez", PlanAfiliacion.PLAN_FIN_DE_SEMANA, LocalDate.now())));
        assertFalse(sociosBajaAsistencia.contains(new Socio(49216836L, "Juan Rodriguez", PlanAfiliacion.PLAN_FIN_DE_SEMANA, LocalDate.now())));
    }


    @Test
    public void obtenerProximoSocioParaLLamar() throws InformacionInvalida, SocioNoExiste {

        //Cargo asistencias base
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(59216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(69216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(47216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 4, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 5, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 11, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 12, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 19, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 25, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 26, 20, 32));


        club.cargarSociosConBajaAsistencia();

        Socio socioALlamar = club.obtenerProximoSocioParaLLamar();
        Socio socioALlamar2 = club.obtenerProximoSocioParaLLamar();
        Socio socioALlamar3 = club.obtenerProximoSocioParaLLamar();
        Socio socioALlamar4 = club.obtenerProximoSocioParaLLamar();
        Socio socioALlamar5 = club.obtenerProximoSocioParaLLamar();
        Socio socioALlamar6 = club.obtenerProximoSocioParaLLamar();


        assertEquals(0, club.getSociosBajaAsistencia().size());
        assertEquals("Juanberto Rodriguez", socioALlamar.getNombre_completo());

    }

    @Test
    public void obtenerListaSociosOrdenadaPorAsistencia() throws InformacionInvalida, SocioNoExiste, EmptyQueueException {

        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(59216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(69216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(47216836L, LocalDateTime.of(2020, Month.JULY, 18, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 4, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 5, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 11, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 12, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 19, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 25, 20, 32));
        club.validarEntradaClub(49216836L, LocalDateTime.of(2020, Month.JULY, 26, 20, 32));

        club.cargarSociosConBajaAsistencia();

        MyQueue<Socio> sociosOrdenados = club.obtenerListaSociosOrdenadaPorAsistencia();

        Socio socio = sociosOrdenados.dequeue();
        assertEquals("Juanberto Rodriguez", socio.getNombre_completo());
        assertEquals(0.0f, socio.getPorcentajeAsistencia(), 1);

        socio = sociosOrdenados.dequeue();
        assertEquals("Juanardo Rodriguez", socio.getNombre_completo());
        assertEquals(0.0f, socio.getPorcentajeAsistencia(), 1);

        socio = sociosOrdenados.dequeue();
        assertEquals("Juanito Rodriguez", socio.getNombre_completo());
        assertEquals(0.03f, socio.getPorcentajeAsistencia(), 2);


    }
}