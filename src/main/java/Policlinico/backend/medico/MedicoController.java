package Policlinico.backend.medico;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public List<Medico> listar(@RequestParam(required = false) Integer codEspe) {
        return medicoService.listar(codEspe);
    }

    @GetMapping("/{codMed}")
    public Medico buscar(@PathVariable String codMed) {
        return medicoService.buscar(codMed);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medico guardar(@Valid @RequestBody MedicoRequest request) {
        return medicoService.guardar(request);
    }

    @DeleteMapping("/{codMed}")
    public void eliminar(@PathVariable String codMed) {
        medicoService.eliminar(codMed);
    }
}
