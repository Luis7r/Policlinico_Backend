package Policlinico.backend.paciente;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscar(String numDoc) {
        return pacienteRepository.findById(numDoc);
    }

    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void eliminar(String numDoc) {
        pacienteRepository.deleteById(numDoc);
    }
}
