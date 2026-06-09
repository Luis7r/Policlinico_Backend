package Policlinico.backend.encargado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EncargadoCitasRepository extends JpaRepository<EncargadoCitas, String> {

    boolean existsByNumDoc(String numDoc);
}
