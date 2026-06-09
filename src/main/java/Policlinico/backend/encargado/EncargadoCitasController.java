package Policlinico.backend.encargado;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/encargados-citas")
public class EncargadoCitasController {

    private final EncargadoCitasService encargadoCitasService;

    public EncargadoCitasController(EncargadoCitasService encargadoCitasService) {
        this.encargadoCitasService = encargadoCitasService;
    }

    @GetMapping
    public List<EncargadoCitas> listar() {
        return encargadoCitasService.listar();
    }

    @GetMapping("/{codEncargado}")
    public EncargadoCitas buscar(@PathVariable String codEncargado) {
        return encargadoCitasService.buscar(codEncargado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EncargadoCitas registrar(@Valid @RequestBody EncargadoCitasRequest request) {
        return encargadoCitasService.registrar(request);
    }
}
