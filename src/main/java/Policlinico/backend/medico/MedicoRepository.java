package Policlinico.backend.medico;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, String> {

    List<Medico> findByEspecialidad_CodEspe(Integer codEspe);
}
