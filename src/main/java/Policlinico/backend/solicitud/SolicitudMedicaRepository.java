package Policlinico.backend.solicitud;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudMedicaRepository extends JpaRepository<SolicitudMedica, Integer> {

    List<SolicitudMedica> findByMedico_CodMed(String codMed);
}
