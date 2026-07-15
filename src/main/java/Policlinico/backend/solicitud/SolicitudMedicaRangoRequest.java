package Policlinico.backend.solicitud;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SolicitudMedicaRangoRequest {

    @NotBlank
    private String codMed;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    private Boolean mismaHora;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    @Valid
    private List<SolicitudMedicaDiaRequest> dias;

    public String getCodMed() {
        return codMed;
    }

    public void setCodMed(String codMed) {
        this.codMed = codMed;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getMismaHora() {
        return mismaHora;
    }

    public void setMismaHora(Boolean mismaHora) {
        this.mismaHora = mismaHora;
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

    public List<SolicitudMedicaDiaRequest> getDias() {
        return dias;
    }

    public void setDias(List<SolicitudMedicaDiaRequest> dias) {
        this.dias = dias;
    }
}
