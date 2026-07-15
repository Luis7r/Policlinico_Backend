package Policlinico.backend.solicitud;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoSolicitudMedicaConverter implements AttributeConverter<EstadoSolicitudMedica, String> {

    @Override
    public String convertToDatabaseColumn(EstadoSolicitudMedica estado) {
        if (estado == null) {
            return null;
        }
        return switch (estado) {
            case PENDIENTE -> "pendientes";
            case ACEPTADO -> "aceptado";
            case RECHAZADO -> "rechazado";
        };
    }

    @Override
    public EstadoSolicitudMedica convertToEntityAttribute(String value) {
        return EstadoSolicitudMedica.fromValue(value);
    }
}
