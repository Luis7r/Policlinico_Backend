package Policlinico.backend.disponibilidad;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class DisponibilidadRequest {

    @NotNull
    private Integer codHor;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    private EstadoDisponibilidad estado;

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

    public EstadoDisponibilidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoDisponibilidad estado) {
        this.estado = estado;
    }
}
