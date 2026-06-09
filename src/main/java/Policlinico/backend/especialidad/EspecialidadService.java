package Policlinico.backend.especialidad;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    public List<Especialidad> listar() {
        return especialidadRepository.findAll();
    }

    public Especialidad buscar(Integer codEspe) {
        return especialidadRepository.findById(codEspe)
                .orElseThrow(() -> new IllegalArgumentException("No existe la especialidad"));
    }

    public Especialidad guardar(Especialidad especialidad) {
        if (especialidadRepository.existsByNombre(especialidad.getNombre())) {
            throw new IllegalArgumentException("Ya existe una especialidad con ese nombre");
        }
        return especialidadRepository.save(especialidad);
    }

    public void eliminar(Integer codEspe) {
        if (!especialidadRepository.existsById(codEspe)) {
            throw new IllegalArgumentException("No existe la especialidad");
        }
        especialidadRepository.deleteById(codEspe);
    }
}
