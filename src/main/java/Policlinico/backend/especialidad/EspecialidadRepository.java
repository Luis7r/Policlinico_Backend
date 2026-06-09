package Policlinico.backend.especialidad;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    boolean existsByNombre(String nombre);
}
