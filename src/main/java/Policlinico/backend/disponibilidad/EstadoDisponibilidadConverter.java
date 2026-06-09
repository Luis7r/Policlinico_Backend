package Policlinico.backend.disponibilidad;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoDisponibilidadConverter implements AttributeConverter<EstadoDisponibilidad, String> {

    @Override
    public String convertToDatabaseColumn(EstadoDisponibilidad estado) {
        return estado == null ? null : estado.name().toLowerCase();
    }

    @Override
    public EstadoDisponibilidad convertToEntityAttribute(String value) {
        return EstadoDisponibilidad.fromValue(value);
    }
}
