package Policlinico.backend.cita.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrarCitaRequest {

    @NotBlank
    @Size(max = 12)
    private String numDoc;

    @NotNull
    private Integer codDis;

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public Integer getCodDis() {
        return codDis;
    }

    public void setCodDis(Integer codDis) {
        this.codDis = codDis;
    }
}
