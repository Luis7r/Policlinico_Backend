package Policlinico.backend.cita.dto;

import jakarta.validation.constraints.NotNull;

public class PostergarCitaRequest {

    @NotNull
    private Integer nuevoCodDis;

    public Integer getNuevoCodDis() {
        return nuevoCodDis;
    }

    public void setNuevoCodDis(Integer nuevoCodDis) {
        this.nuevoCodDis = nuevoCodDis;
    }
}
