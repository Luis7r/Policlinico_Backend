package Policlinico.backend.horario;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping
    public List<Horario> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String codMed,
            @RequestParam(required = false) String codEncargado) {
        return horarioService.listar(fecha, codMed, codEncargado);
    }

    @GetMapping("/{codHor}")
    public Horario buscar(@PathVariable Integer codHor) {
        return horarioService.buscar(codHor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Horario guardar(@Valid @RequestBody HorarioRequest request) {
        return horarioService.guardar(request);
    }

    @DeleteMapping("/{codHor}")
    public void eliminar(@PathVariable Integer codHor) {
        horarioService.eliminar(codHor);
    }
}
