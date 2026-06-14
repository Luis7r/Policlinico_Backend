package Policlinico.backend.solicitud;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.medico.Medico;
import Policlinico.backend.medico.MedicoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SolicitudMedicaService {

    private final SolicitudMedicaRepository solicitudMedicaRepository;
    private final MedicoService medicoService;

    public SolicitudMedicaService(
            SolicitudMedicaRepository solicitudMedicaRepository,
            MedicoService medicoService) {
        this.solicitudMedicaRepository = solicitudMedicaRepository;
        this.medicoService = medicoService;
    }

    public List<SolicitudMedica> listar(String codMed) {
        if (codMed != null && !codMed.isBlank()) {
            return solicitudMedicaRepository.findByMedico_CodMed(CodigoIdentidad.medico(codMed));
        }
        return solicitudMedicaRepository.findAll();
    }

    public SolicitudMedica registrar(SolicitudMedicaRequest request) {
        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor a la hora de fin");
        }

        Medico medico = medicoService.buscar(request.getCodMed());
        SolicitudMedica solicitud = new SolicitudMedica();
        solicitud.setMedico(medico);
        solicitud.setFecha(request.getFecha());
        solicitud.setHoraInicio(request.getHoraInicio());
        solicitud.setHoraFin(request.getHoraFin());
        return solicitudMedicaRepository.save(solicitud);
    }
}
