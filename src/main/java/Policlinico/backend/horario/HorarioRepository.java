package Policlinico.backend.horario;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    List<Horario> findByFecha(LocalDate fecha);

    List<Horario> findByMedico_CodMed(String codMed);

    List<Horario> findByEncargadoCitas_CodEncargado(String codEncargado);
}
