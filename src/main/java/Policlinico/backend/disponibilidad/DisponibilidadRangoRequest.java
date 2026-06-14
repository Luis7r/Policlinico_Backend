package Policlinico.backend.disponibilidad;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class DisponibilidadRangoRequest {

    @NotNull
    private Integer codHor;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    public Integer getCodHor() {
        return codHor;
    }

    public void setCodHor(Integer codHor) {
        this.codHor = codHor;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
