package Policlinico.backend.solicitud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoSolicitudMedica {
    PENDIENTE,
    ACEPTADO,
    RECHAZADO;

    @JsonCreator
    public static EstadoSolicitudMedica fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "PENDIENTES" -> PENDIENTE;
            case "ACEPTADA", "ACEPTADAS" -> ACEPTADO;
            case "RECHAZADA", "RECHAZADAS" -> RECHAZADO;
            default -> EstadoSolicitudMedica.valueOf(normalized);
        };
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
