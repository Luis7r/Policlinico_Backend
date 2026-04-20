package Policlinico.backend.paciente;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Paciente guardar(@RequestBody Paciente paciente) {
        return pacienteService.guardar(paciente);
    }

    @DeleteMapping("/{numDoc}")
    public void eliminar(@PathVariable String numDoc) {
        pacienteService.eliminar(numDoc);
    }
}
