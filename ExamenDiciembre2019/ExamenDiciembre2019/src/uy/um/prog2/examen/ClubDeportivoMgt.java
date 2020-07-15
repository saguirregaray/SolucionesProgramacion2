package uy.um.prog2.examen;

import uy.edu.um.adt.linkedlist.MyList;
import uy.edu.um.adt.queue.MyQueue;
import uy.um.prog2.examen.Exceptions.InformacionInvalida;
import uy.um.prog2.examen.Exceptions.SocioNoExiste;
import uy.um.prog2.examen.Exceptions.SocioYaAfiliado;
import uy.um.prog2.examen.Utils.PlanAfiliacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ClubDeportivoMgt {

    void afiliarSocio(long cedula, String nombre_completo, PlanAfiliacion plan_afiliacion, LocalDate fecha_afiliacion) throws InformacionInvalida, SocioYaAfiliado;

    boolean validarEntradaClub(long cedula, LocalDateTime fechahora) throws SocioNoExiste, InformacionInvalida;

    void cargarSociosConBajaAsistencia();

    Socio obtenerProximoSocioParaLLamar();

    MyQueue<Socio> obtenerListaSociosOrdenadaPorAsistencia();


}
