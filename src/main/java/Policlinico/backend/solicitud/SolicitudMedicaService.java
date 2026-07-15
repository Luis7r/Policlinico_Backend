package Policlinico.backend.solicitud;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.medico.Medico;
import Policlinico.backend.medico.MedicoService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public List<SolicitudMedica> listar(String codMed, Integer codEspe, EstadoSolicitudMedica estado) {
        String codigoMedico = codMed != null && !codMed.isBlank() ? CodigoIdentidad.medico(codMed) : null;
        return solicitudMedicaRepository.findAll().stream()
                .filter(solicitud -> codigoMedico == null || solicitud.getMedico().getCodMed().equals(codigoMedico))
                .filter(solicitud -> codEspe == null || solicitud.getMedico().getEspecialidad().getCodEspe().equals(codEspe))
                .filter(solicitud -> estado == null || solicitud.getEstado() == estado)
                .toList();
    }

    public SolicitudMedica registrar(SolicitudMedicaRequest request) {
        Medico medico = medicoService.buscar(request.getCodMed());
        return guardarSolicitud(medico, request.getFecha(), request.getHoraInicio(), request.getHoraFin());
    }

    public List<SolicitudMedica> registrarRango(SolicitudMedicaRangoRequest request) {
        validarRangoFechas(request.getFechaInicio(), request.getFechaFin());

        Medico medico = medicoService.buscar(request.getCodMed());
        List<SolicitudMedica> solicitudes = new ArrayList<>();
        Map<LocalDate, SolicitudMedicaDiaRequest> dias = mapearDias(request.getDias());

        for (LocalDate fecha = request.getFechaInicio(); !fecha.isAfter(request.getFechaFin()); fecha = fecha.plusDays(1)) {
            LocalTime horaInicio = request.getMismaHora() ? request.getHoraInicio() : obtenerDia(dias, fecha).getHoraInicio();
            LocalTime horaFin = request.getMismaHora() ? request.getHoraFin() : obtenerDia(dias, fecha).getHoraFin();
            solicitudes.add(guardarSolicitud(medico, fecha, horaInicio, horaFin));
        }

        return solicitudes;
    }

    public List<SolicitudMedica> cambiarEstado(SolicitudEstadoRequest request) {
        List<SolicitudMedica> solicitudes = solicitudMedicaRepository.findAllById(request.getIds());
        if (solicitudes.size() != request.getIds().size()) {
            throw new IllegalArgumentException("Una o mas solicitudes no existen");
        }
        solicitudes.forEach(solicitud -> solicitud.setEstado(request.getEstado()));
        return solicitudMedicaRepository.saveAll(solicitudes);
    }

    private SolicitudMedica guardarSolicitud(Medico medico, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        validarHoras(horaInicio, horaFin);

        SolicitudMedica solicitud = new SolicitudMedica();
        solicitud.setMedico(medico);
        solicitud.setFecha(fecha);
        solicitud.setHoraInicio(horaInicio);
        solicitud.setHoraFin(horaFin);
        solicitud.setEstado(EstadoSolicitudMedica.PENDIENTE);
        return solicitudMedicaRepository.save(solicitud);
    }

    private void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser menor o igual a la fecha de fin");
        }
    }

    private void validarHoras(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null || horaFin == null || !horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor a la hora de fin");
        }
    }

    private Map<LocalDate, SolicitudMedicaDiaRequest> mapearDias(List<SolicitudMedicaDiaRequest> dias) {
        if (dias == null) {
            return Map.of();
        }
        return dias.stream()
                .collect(Collectors.toMap(SolicitudMedicaDiaRequest::getFecha, Function.identity(), (actual, repetido) -> actual));
    }

    private SolicitudMedicaDiaRequest obtenerDia(Map<LocalDate, SolicitudMedicaDiaRequest> dias, LocalDate fecha) {
        SolicitudMedicaDiaRequest dia = dias.get(fecha);
        if (dia == null) {
            throw new IllegalArgumentException("Debe indicar el rango horario para la fecha " + fecha);
        }
        return dia;
    }
}
