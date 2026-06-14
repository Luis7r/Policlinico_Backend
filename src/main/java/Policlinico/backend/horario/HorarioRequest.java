package Policlinico.backend.horario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class HorarioRequest {

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String codMed;

    private String codEncargado;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

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
}
