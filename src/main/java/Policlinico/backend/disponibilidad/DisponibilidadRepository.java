package Policlinico.backend.disponibilidad;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {

    List<Disponibilidad> findByEstado(EstadoDisponibilidad estado);

    List<Disponibilidad> findByHorario_Fecha(LocalDate fecha);
    
    List<Disponibilidad> findByEstadoAndHorario_Medico_CodMed(
        EstadoDisponibilidad estado,
        String codMed);

    List<Disponibilidad> findByHorario_Medico_CodMed(String codMed);
    
    List<Disponibilidad> findByEstadoAndHorario_FechaAndHorario_Medico_CodMed(
        EstadoDisponibilidad estado,
        LocalDate fecha,
        String codMed);

    boolean existsByHorario_CodHorAndHoraInicioAndHoraFin(Integer codHor, LocalTime horaInicio, LocalTime horaFin);
}
