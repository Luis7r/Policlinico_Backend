package Policlinico.backend.solicitud;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solicitudes-medicas")
public class SolicitudMedicaController {

    private final SolicitudMedicaService solicitudMedicaService;

    public SolicitudMedicaController(SolicitudMedicaService solicitudMedicaService) {
        this.solicitudMedicaService = solicitudMedicaService;
    }

    @GetMapping
    public List<SolicitudMedica> listar(
            @RequestParam(required = false) String codMed,
            @RequestParam(required = false) Integer codEspe,
            @RequestParam(required = false) EstadoSolicitudMedica estado) {
        return solicitudMedicaService.listar(codMed, codEspe, estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudMedica registrar(@Valid @RequestBody SolicitudMedicaRequest request) {
        return solicitudMedicaService.registrar(request);
    }

    @PostMapping("/rango")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SolicitudMedica> registrarRango(@Valid @RequestBody SolicitudMedicaRangoRequest request) {
        return solicitudMedicaService.registrarRango(request);
    }

    @PutMapping("/estado")
    public List<SolicitudMedica> cambiarEstado(@Valid @RequestBody SolicitudEstadoRequest request) {
        return solicitudMedicaService.cambiarEstado(request);
    }
}
