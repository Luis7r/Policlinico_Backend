package Policlinico.backend.disponibilidad;

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
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping
    public List<Disponibilidad> listar(
            @RequestParam(required = false) EstadoDisponibilidad estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false, defaultValue = "false") boolean incluirPasadas,
            @RequestParam(required = false) String codMed) {
        return disponibilidadService.listar(estado, fecha, fechaInicio, fechaFin, incluirPasadas, codMed);
    }

    @GetMapping("/{codDis}")
    public Disponibilidad buscar(@PathVariable Integer codDis) {
        return disponibilidadService.buscar(codDis);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Disponibilidad guardar(@Valid @RequestBody DisponibilidadRequest request) {
        return disponibilidadService.guardar(request);
    }

    @PostMapping("/rango")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Disponibilidad> guardarRango(@Valid @RequestBody DisponibilidadRangoRequest request) {
        return disponibilidadService.guardarRango(request);
    }

    @PostMapping("/masivo")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Disponibilidad> guardarMasivo(@Valid @RequestBody DisponibilidadMasivaRequest request) {
        return disponibilidadService.guardarMasivo(request);
    }

    @DeleteMapping("/{codDis}")
    public void eliminar(@PathVariable Integer codDis) {
        disponibilidadService.eliminar(codDis);
    }
}
