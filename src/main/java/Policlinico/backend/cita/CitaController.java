package Policlinico.backend.cita;

import Policlinico.backend.cita.dto.CitaResponse;
import Policlinico.backend.cita.dto.CancelarCitaRequest;
import Policlinico.backend.cita.dto.PostergarCitaRequest;
import Policlinico.backend.cita.dto.RegistrarCitaRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<CitaResponse> listar(@RequestParam(required = false) String numDoc) {
        return citaService.listar(numDoc);
    }

    @GetMapping("/{codCita}")
    public CitaResponse buscar(@PathVariable Integer codCita) {
        return citaService.buscar(codCita);
    }

    @GetMapping("/medico/{codMed}/pendientes")
    public List<CitaResponse> listarPendientesMedico(@PathVariable String codMed) {
        return citaService.listarPendientesMedico(codMed);
    }

    @GetMapping("/medico/{codMed}/historial")
    public List<CitaResponse> listarHistorialMedico(@PathVariable String codMed) {
        return citaService.listarHistorialMedico(codMed);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaResponse registrar(@Valid @RequestBody RegistrarCitaRequest request) {
        return citaService.registrar(request);
    }

    @PutMapping("/{codCita}/postergar")
    public CitaResponse postergar(
            @PathVariable Integer codCita,
            @Valid @RequestBody PostergarCitaRequest request) {
        return citaService.postergar(codCita, request);
    }

    @PutMapping("/{codCita}/cancelar")
    public CitaResponse cancelar(
            @PathVariable Integer codCita,
            @RequestBody(required = false) CancelarCitaRequest request) {
        String motivo = request != null ? request.getMotivo() : null;
        return citaService.cancelar(codCita, motivo);
    }

    @PutMapping("/{codCita}/atender")
    public CitaResponse atender(@PathVariable Integer codCita) {
        return citaService.atender(codCita);
    }

    @PutMapping("/{codCita}/ausente")
    public CitaResponse marcarAusente(@PathVariable Integer codCita) {
        return citaService.marcarAusente(codCita);
    }
}
