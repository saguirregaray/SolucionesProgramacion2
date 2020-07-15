package uy.um.prog2.examen;

import uy.edu.um.adt.binarytree.MySearchBinaryTree;
import uy.edu.um.adt.binarytree.MySearchBinaryTreeImpl;
import uy.um.prog2.examen.Utils.PlanAfiliacion;

import java.time.LocalDate;
import java.util.Objects;

public class Socio implements Comparable<Socio> {

    private long cedula;
    private String nombre_completo;
    private PlanAfiliacion plan_afiliacion;
    private LocalDate fecha_afiliacion;
    // Guarda la cantidad y el mes que esta contando. Al pasar el mes se debe limpiar
    private int[] cantidadAsistenciasMensuales = new int[2];

    private MySearchBinaryTree<Asistencia, Asistencia> asistencias = new MySearchBinaryTreeImpl<>();

    Socio(long cedula, String nombre_completo, PlanAfiliacion plan_afiliacion, LocalDate fecha_afiliacion) {
        this.cedula = cedula;
        this.nombre_completo = nombre_completo;
        this.plan_afiliacion = plan_afiliacion;
        this.fecha_afiliacion = fecha_afiliacion;
        cantidadAsistenciasMensuales[1] = LocalDate.now().getMonthValue();
    }

    void sumarAsistencia() {
        if (cantidadAsistenciasMensuales[1] != LocalDate.now().getMonthValue())
            limpiarAsistencias();
        else
            cantidadAsistenciasMensuales[0]++;
    }

    public int[] getCantidadAsistenciasMensuales() {
        return cantidadAsistenciasMensuales;
    }

    float getPorcentajeAsistencia() {

        LocalDate date = LocalDate.now();
        if (plan_afiliacion.equals(PlanAfiliacion.PLAN_LIBRE))
            return (float) cantidadAsistenciasMensuales[0] / date.lengthOfMonth();
        else
            // Cantidad de asistencias por mes sobre la cantidad de sabados y domingos del mes (aprox)
            // Tomo la cantidad de dias en el mes y lo divido entre 7 para sacar la cantidad de semanas, asumiendo
            // que todas las semanas tienen un sabado y un domingo multiplico eso por dos
            return (float) cantidadAsistenciasMensuales[0] / ((date.lengthOfMonth() / 7) * 2);
    }

    public long getCedula() {
        return cedula;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public PlanAfiliacion getPlan_afiliacion() {
        return plan_afiliacion;
    }

    public LocalDate getFecha_afiliacion() {
        return fecha_afiliacion;
    }

    MySearchBinaryTree<Asistencia, Asistencia> getAsistencias() {
        return asistencias;
    }

    // Tiene sentido cambiar el plan de afiliacion
    public void setPlan_afiliacion(PlanAfiliacion plan_afiliacion) {
        this.plan_afiliacion = plan_afiliacion;
    }

    void addAsistencia(Asistencia asistencia) {
        asistencias.add(asistencia, asistencia);
    }

    // Una persona es diferente si su cedula es diferente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socio socio = (Socio) o;
        return cedula == socio.cedula;
    }

    // Hashcode por cedula
    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }

    // Comparo por el porcentaje de asistencias
    // Me interesa tener a los con menor porcentaje arriba asi que comparo al reves
    @Override
    public int compareTo(Socio o) {
        if (this.getPorcentajeAsistencia() > o.getPorcentajeAsistencia())
            return -1;
        else if (this.getPorcentajeAsistencia() < o.getPorcentajeAsistencia())
            return 1;
        return 0;

    }

    private void limpiarAsistencias() {
        cantidadAsistenciasMensuales[0] = 0;
        cantidadAsistenciasMensuales[1] = LocalDate.now().getMonthValue();
    }
}
