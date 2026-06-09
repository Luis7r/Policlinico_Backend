package Policlinico.backend.especialidad;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public List<Especialidad> listar() {
        return especialidadService.listar();
    }

    @GetMapping("/{codEspe}")
    public Especialidad buscar(@PathVariable Integer codEspe) {
        return especialidadService.buscar(codEspe);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Especialidad guardar(@Valid @RequestBody Especialidad especialidad) {
        return especialidadService.guardar(especialidad);
    }

    @DeleteMapping("/{codEspe}")
    public void eliminar(@PathVariable Integer codEspe) {
        especialidadService.eliminar(codEspe);
    }
}
