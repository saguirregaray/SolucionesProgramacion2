package uy.um.prog2.examen;

import uy.edu.um.adt.hash.MyHash;
import uy.edu.um.adt.hash.MyHashImpl;
import uy.edu.um.adt.heap.MyHeap;
import uy.edu.um.adt.heap.MyHeapImpl;
import uy.edu.um.adt.linkedlist.MyLinkedListImpl;
import uy.edu.um.adt.linkedlist.MyList;
import uy.edu.um.adt.queue.EmptyQueueException;
import uy.edu.um.adt.queue.MyQueue;
import uy.um.prog2.examen.Exceptions.InformacionInvalida;
import uy.um.prog2.examen.Exceptions.SocioNoExiste;
import uy.um.prog2.examen.Exceptions.SocioYaAfiliado;
import uy.um.prog2.examen.Utils.PlanAfiliacion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClubDeportivoMgr implements ClubDeportivoMgt {

    private MyHash<Long, Socio> socios = new MyHashImpl<>(1000);
    private MyQueue<Socio> sociosBajaAsistencia = new MyLinkedListImpl<>();


    // Para que sea O(1) promedio utilizo un hash (asumo que es suficientemente grande como para no tener muchas colisones)
    @Override
    public void afiliarSocio(long cedula, String nombre_completo, PlanAfiliacion plan_afiliacion, LocalDate fecha_afiliacion) throws InformacionInvalida, SocioYaAfiliado {

        if (String.valueOf(cedula).length() < 8 || nombre_completo == null || plan_afiliacion == null || fecha_afiliacion == null)
            throw new InformacionInvalida();

        if (!socios.contains(cedula))
            socios.put(cedula, new Socio(cedula, nombre_completo, plan_afiliacion, fecha_afiliacion));
        else
            throw new SocioYaAfiliado();
    }

    // Valida en O(1) promedio porque que encuentra al socio en O(1) por el hash y luego solo hace comparaciones
    // La asistencia se agrega a un bst por lo tanto tiene un average search de O(log n)
    @Override
    public boolean validarEntradaClub(long cedula, LocalDateTime fechahora) throws SocioNoExiste, InformacionInvalida {
        if (String.valueOf(cedula).length() < 8 || fechahora == null)
            throw new InformacionInvalida();

        Socio socio = socios.get(cedula);

        if (socio == null)
            throw new SocioNoExiste();

        // Si es libre esta siempre habilitado. Si no es libre -> es fin de semana entonces solo me fijo si es sabado o domingo, sino retorno false
        // Si ya existe esa asistencia retorno false
        if (socio.getPlan_afiliacion().equals(PlanAfiliacion.PLAN_LIBRE) || fechahora.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || fechahora.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {

            Asistencia asistencia = new Asistencia(fechahora);
            if (!socio.getAsistencias().contains(asistencia)) {
                socio.addAsistencia(asistencia);
                socio.sumarAsistencia();
                return true;
            }
        }

        return false;
    }

    // Sera O(n) porque debo recorrer todos los socios para ver sus asistencias
    @Override
    public void cargarSociosConBajaAsistencia() {

        MyList<Socio> listaSocios = socios.values();

        for (int i = 0; i < listaSocios.size(); i++) {
            Socio socioAux = listaSocios.get(i);
            if (socioAux.getPorcentajeAsistencia() <= 0.25f)
                sociosBajaAsistencia.enqueue(socioAux);
        }

    }


    // O(1) en el peor caso porque solo saca el primero de la queue
    @Override
    public Socio obtenerProximoSocioParaLLamar() {
        try {
            obtenerListaSociosOrdenadaPorAsistencia();
            return sociosBajaAsistencia.dequeue();
        } catch (EmptyQueueException e) {
            return null;
        }

    }

    @Override
    public MyQueue<Socio> obtenerListaSociosOrdenadaPorAsistencia() {

        MyHeap<Socio> sociosHeap = new MyHeapImpl<>();

        try {
            sociosHeap.insert(sociosBajaAsistencia.dequeue());
        } catch (EmptyQueueException e) {
            // Cuando se vacie la queue, la recargo con los valores ordenados que saque del heap
            while (sociosHeap.size() > 0)
                sociosBajaAsistencia.enqueue(sociosHeap.delete());
        }
        return sociosBajaAsistencia;

    }

    // Auxiliares para testeo

    public MyList<Socio> getSocios() {
        return socios.values();
    }

    public MyQueue<Socio> getSociosBajaAsistencia() {
        return sociosBajaAsistencia;
    }
}
