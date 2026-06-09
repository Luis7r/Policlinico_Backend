package Policlinico.backend.paciente;

import Policlinico.backend.paciente.dto.RegistroPacienteRequest;
import Policlinico.backend.paciente.dto.RegistroPacienteResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listar();
    }

    @GetMapping("/{numDoc}")
    public Optional<Paciente> buscar(@PathVariable String numDoc) {
        return pacienteService.buscar(numDoc);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroPacienteResponse guardar(@Valid @RequestBody RegistroPacienteRequest request) {
        return pacienteService.registrar(request);
    }

    @DeleteMapping("/{numDoc}")
    public void eliminar(@PathVariable String numDoc) {
        pacienteService.eliminar(numDoc);
    }
}
