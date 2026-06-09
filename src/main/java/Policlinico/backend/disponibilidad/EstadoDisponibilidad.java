package Policlinico.backend.disponibilidad;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoDisponibilidad {
    DISPONIBLE,
    RESERVADO,
    NO_DISPONIBLE;

    @JsonCreator
    public static EstadoDisponibilidad fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim().toUpperCase().replace('-', '_');
        return EstadoDisponibilidad.valueOf(normalized);
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
