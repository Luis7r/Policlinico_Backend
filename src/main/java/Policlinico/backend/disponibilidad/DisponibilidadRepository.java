package Policlinico.backend.disponibilidad;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {

    List<Disponibilidad> findByEstado(EstadoDisponibilidad estado);

    List<Disponibilidad> findByHorario_Fecha(LocalDate fecha);

    List<Disponibilidad> findByHorario_Medico_CodMed(String codMed);
}
