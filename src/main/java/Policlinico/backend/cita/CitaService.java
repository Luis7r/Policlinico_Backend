package Policlinico.backend.cita;

import Policlinico.backend.cita.dto.CitaResponse;
import Policlinico.backend.cita.dto.PostergarCitaRequest;
import Policlinico.backend.cita.dto.RegistrarCitaRequest;
import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.disponibilidad.Disponibilidad;
import Policlinico.backend.disponibilidad.DisponibilidadService;
import Policlinico.backend.disponibilidad.EstadoDisponibilidad;
import Policlinico.backend.medico.Medico;
import Policlinico.backend.notificacion.NotificacionService;
import Policlinico.backend.paciente.Paciente;
import Policlinico.backend.paciente.PacienteRepository;
import Policlinico.backend.usuario.Usuario;
import Policlinico.backend.usuario.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final DisponibilidadService disponibilidadService;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public CitaService(
            CitaRepository citaRepository,
            PacienteRepository pacienteRepository,
            DisponibilidadService disponibilidadService,
            UsuarioRepository usuarioRepository,
            NotificacionService notificacionService) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.disponibilidadService = disponibilidadService;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listar(String numDoc) {
        List<Cita> citas = numDoc != null && !numDoc.isBlank()
                ? citaRepository.findByPaciente_NumDoc(numDoc)
                : citaRepository.findAll();
        return citas.stream()
                .map(cita -> toResponse(cita, correoRegistradoOpcional(cita.getPaciente()), null))
                .toList();
    }

    @Transactional(readOnly = true)
    public CitaResponse buscar(Integer codCita) {
        Cita cita = buscarCita(codCita);
        return toResponse(cita, correoRegistradoOpcional(cita.getPaciente()), null);
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listarPendientesMedico(String codMed) {
        return citaRepository
                .findByDisponibilidad_Horario_Medico_CodMedAndEstadoIn(
                        CodigoIdentidad.medico(codMed), List.of(EstadoCita.REGISTRADA, EstadoCita.POSTERGADA))
                .stream()
                .map(cita -> toResponse(cita, correoRegistradoOpcional(cita.getPaciente()), null))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> listarHistorialMedico(String codMed) {
        return citaRepository
                .findByDisponibilidad_Horario_Medico_CodMedAndEstado(CodigoIdentidad.medico(codMed), EstadoCita.ATENDIDA)
                .stream()
                .map(cita -> toResponse(cita, correoRegistradoOpcional(cita.getPaciente()), null))
                .toList();
    }

    @Transactional
    public CitaResponse registrar(RegistrarCitaRequest request) {
        String documento = CodigoIdentidad.documentoBase(request.getNumDoc());
        String codPaciente = CodigoIdentidad.paciente(documento);
        Paciente paciente = pacienteRepository.findById(codPaciente)
                .or(() -> pacienteRepository.findById(documento))
                .orElseThrow(() -> new IllegalArgumentException("No existe el paciente"));
        String correo = correoRegistrado(paciente);

        Disponibilidad disponibilidad = disponibilidadService.buscar(request.getCodDis());
        validarDisponible(disponibilidad);

        disponibilidad.setEstado(EstadoDisponibilidad.RESERVADO);

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setDisponibilidad(disponibilidad);
        cita.setEstado(EstadoCita.REGISTRADA);
        cita = citaRepository.save(cita);

        boolean notificacionEnviada = notificacionService.enviar(
                correo,
                "Cita registrada",
                mensajeCita("Su cita fue registrada correctamente.", cita));

        return toResponse(cita, correo, notificacionEnviada);
    }

    @Transactional
    public CitaResponse postergar(Integer codCita, PostergarCitaRequest request) {
        Cita cita = buscarCita(codCita);
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("No se puede postergar una cita cancelada");
        }

        String correo = correoRegistrado(cita.getPaciente());
        Disponibilidad anterior = cita.getDisponibilidad();
        Disponibilidad nueva = disponibilidadService.buscar(request.getNuevoCodDis());
        if (anterior.getCodDis().equals(nueva.getCodDis())) {
            throw new IllegalArgumentException("La nueva disponibilidad debe ser diferente a la actual");
        }

        validarDisponible(nueva);
        anterior.setEstado(EstadoDisponibilidad.DISPONIBLE);
        nueva.setEstado(EstadoDisponibilidad.RESERVADO);

        cita.setDisponibilidad(nueva);
        cita.setEstado(EstadoCita.POSTERGADA);
        cita = citaRepository.save(cita);

        boolean notificacionEnviada = notificacionService.enviar(
                correo,
                "Cita postergada",
                mensajeCita("Su cita fue postergada. Estos son los nuevos datos:", cita));

        return toResponse(cita, correo, notificacionEnviada);
    }

    @Transactional
    public CitaResponse cancelar(Integer codCita) {
        Cita cita = buscarCita(codCita);
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("La cita ya fue cancelada");
        }

        String correo = correoRegistrado(cita.getPaciente());
        cita.getDisponibilidad().setEstado(EstadoDisponibilidad.DISPONIBLE);
        cita.setEstado(EstadoCita.CANCELADA);
        cita = citaRepository.save(cita);

        boolean notificacionEnviada = notificacionService.enviar(
                correo,
                "Cita cancelada",
                mensajeCita("Su cita fue cancelada.", cita));

        return toResponse(cita, correo, notificacionEnviada);
    }

    @Transactional
    public CitaResponse atender(Integer codCita) {
        Cita cita = buscarCita(codCita);
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("No se puede atender una cita cancelada");
        }
        if (cita.getEstado() == EstadoCita.ATENDIDA) {
            throw new IllegalArgumentException("La cita ya fue atendida");
        }
        cita.setEstado(EstadoCita.ATENDIDA);
        cita = citaRepository.save(cita);
        return toResponse(cita, correoRegistradoOpcional(cita.getPaciente()), null);
    }

    private Cita buscarCita(Integer codCita) {
        return citaRepository.findById(codCita)
                .orElseThrow(() -> new IllegalArgumentException("No existe la cita"));
    }

    private void validarDisponible(Disponibilidad disponibilidad) {
        if (disponibilidad.getEstado() != EstadoDisponibilidad.DISPONIBLE) {
            throw new IllegalArgumentException("La disponibilidad seleccionada no esta disponible");
        }
    }

    private String correoRegistrado(Paciente paciente) {
        Usuario usuario = usuarioRepository.findByPaciente_NumDoc(paciente.getNumDoc())
                .orElseThrow(() -> new IllegalArgumentException("El paciente no tiene correo registrado"));
        return usuario.getCorreo();
    }

    private String correoRegistradoOpcional(Paciente paciente) {
        return usuarioRepository.findByPaciente_NumDoc(paciente.getNumDoc())
                .map(Usuario::getCorreo)
                .orElse(null);
    }

    private String mensajeCita(String encabezado, Cita cita) {
        Disponibilidad disponibilidad = cita.getDisponibilidad();
        Medico medico = disponibilidad.getHorario().getMedico();
        return """
                %s

                Paciente: %s %s
                Fecha: %s
                Hora: %s - %s
                Medico: %s %s
                Especialidad: %s
                Estado: %s
                """.formatted(
                encabezado,
                cita.getPaciente().getNombre(),
                cita.getPaciente().getApellido(),
                disponibilidad.getHorario().getFecha(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin(),
                medico.getNombre(),
                medico.getApellido(),
                medico.getEspecialidad().getNombre(),
                cita.getEstado().name());
    }

    private CitaResponse toResponse(Cita cita, String correo, Boolean notificacionEnviada) {
        Disponibilidad disponibilidad = cita.getDisponibilidad();
        Medico medico = disponibilidad.getHorario().getMedico();

        CitaResponse response = new CitaResponse();
        response.setCodCita(cita.getCodCita());
        response.setEstado(cita.getEstado().name());
        response.setNumDoc(cita.getPaciente().getNumDoc());
        response.setPaciente(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido());
        response.setCorreo(correo);
        response.setCodDis(disponibilidad.getCodDis());
        response.setFecha(disponibilidad.getHorario().getFecha());
        response.setHoraInicio(disponibilidad.getHoraInicio());
        response.setHoraFin(disponibilidad.getHoraFin());
        response.setCodMed(medico.getCodMed());
        response.setMedico(medico.getNombre() + " " + medico.getApellido());
        response.setEspecialidad(medico.getEspecialidad().getNombre());
        response.setNotificacionEnviada(notificacionEnviada);
        return response;
    }
}
