package Policlinico.backend.disponibilidad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DisponibilidadMasivaRequest {

    @NotBlank
    private String codMed;

    private String codEncargado;

    private String consultorio;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    private Boolean mismaHora;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Integer duracionMinutos;

    @Valid
    private List<DisponibilidadDiaRequest> dias;

    public String getCodMed() {
        return codMed;
    }

    public void setCodMed(String codMed) {
        this.codMed = codMed;
    }

    public String getCodEncargado() {
        return codEncargado;
    }

    public void setCodEncargado(String codEncargado) {
        this.codEncargado = codEncargado;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
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

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public List<DisponibilidadDiaRequest> getDias() {
        return dias;
    }

    public void setDias(List<DisponibilidadDiaRequest> dias) {
        this.dias = dias;
    }
}
