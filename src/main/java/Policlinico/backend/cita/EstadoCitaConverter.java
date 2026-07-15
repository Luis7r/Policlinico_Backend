package Policlinico.backend.cita;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoCitaConverter implements AttributeConverter<EstadoCita, String> {

    @Override
    public String convertToDatabaseColumn(EstadoCita estado) {
        if (estado == null) {
            return null;
        }
        return switch (estado) {
            case REGISTRADA -> "pendiente";
            case POSTERGADA -> "reprogramado";
            case CANCELADA -> "cancelado";
            case ATENDIDA -> "atendido";
            case AUSENTE -> "ausente";
        };
    }

    @Override
    public EstadoCita convertToEntityAttribute(String value) {
        return EstadoCita.fromValue(value);
    }
}
