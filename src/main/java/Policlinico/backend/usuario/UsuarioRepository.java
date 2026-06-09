package Policlinico.backend.usuario;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCorreo(String correo);

    boolean existsByPaciente_NumDoc(String numDoc);

    boolean existsByMedico_CodMed(String codMed);

    boolean existsByEncargadoCitas_CodEncargado(String codEncargado);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByPaciente_NumDoc(String numDoc);

    Optional<Usuario> findByMedico_CodMed(String codMed);

    Optional<Usuario> findByEncargadoCitas_CodEncargado(String codEncargado);
}
