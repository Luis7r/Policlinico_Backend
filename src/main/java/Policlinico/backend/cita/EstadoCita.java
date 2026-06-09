package Policlinico.backend.cita;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoCita {
    REGISTRADA,
    POSTERGADA,
    CANCELADA,
    ATENDIDA;

    @JsonCreator
    public static EstadoCita fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "PENDIENTE" -> REGISTRADA;
            case "REPROGRAMADO" -> POSTERGADA;
            case "REGISTRADO" -> REGISTRADA;
            case "POSTERGADO" -> POSTERGADA;
            case "CANCELADO" -> CANCELADA;
            case "ATENDIDO" -> ATENDIDA;
            default -> EstadoCita.valueOf(normalized);
        };
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
