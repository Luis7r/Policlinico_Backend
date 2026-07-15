package Policlinico.backend.solicitud;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SolicitudEstadoRequest {

    @NotEmpty
    private List<Integer> ids;

    @NotNull
    private EstadoSolicitudMedica estado;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public EstadoSolicitudMedica getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitudMedica estado) {
        this.estado = estado;
    }
}
