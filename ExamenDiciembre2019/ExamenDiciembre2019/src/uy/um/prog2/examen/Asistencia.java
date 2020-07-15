package uy.um.prog2.examen;

import java.time.LocalDateTime;

public class Asistencia implements Comparable<Asistencia> {

    private LocalDateTime fechaHoraAsistencia;

    Asistencia(LocalDateTime fechaHoraAsistencia) {
        this.fechaHoraAsistencia = fechaHoraAsistencia;
    }

    public LocalDateTime getFechaHoraAsistencia() {
        return fechaHoraAsistencia;
    }

    public void setFechaHoraAsistencia(LocalDateTime fechaHoraAsistencia) {
        this.fechaHoraAsistencia = fechaHoraAsistencia;
    }

    // Asistencia diferente si fecha y hora son diferentes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asistencia that = (Asistencia) o;
        return fechaHoraAsistencia.equals(that.fechaHoraAsistencia);
    }

    @Override
    public int compareTo(Asistencia o) {
        return fechaHoraAsistencia.compareTo(o.fechaHoraAsistencia);
    }
}
