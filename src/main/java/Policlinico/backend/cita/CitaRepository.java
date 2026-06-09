package Policlinico.backend.cita;

import java.util.List;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByPaciente_NumDoc(String numDoc);

    List<Cita> findByDisponibilidad_Horario_Medico_CodMedAndEstadoIn(String codMed, Collection<EstadoCita> estados);

    List<Cita> findByDisponibilidad_Horario_Medico_CodMedAndEstado(String codMed, EstadoCita estado);
}
