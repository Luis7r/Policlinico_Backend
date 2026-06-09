package Policlinico.backend.medico;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.encargado.EncargadoCitasRepository;
import Policlinico.backend.especialidad.Especialidad;
import Policlinico.backend.especialidad.EspecialidadService;
import Policlinico.backend.paciente.PacienteRepository;
import Policlinico.backend.usuario.Rol;
import Policlinico.backend.usuario.Usuario;
import Policlinico.backend.usuario.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadService especialidadService;
    private final PacienteRepository pacienteRepository;
    private final EncargadoCitasRepository encargadoCitasRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(
            MedicoRepository medicoRepository,
            EspecialidadService especialidadService,
            PacienteRepository pacienteRepository,
            EncargadoCitasRepository encargadoCitasRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.especialidadService = especialidadService;
        this.pacienteRepository = pacienteRepository;
        this.encargadoCitasRepository = encargadoCitasRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Medico> listar(Integer codEspe) {
        if (codEspe != null) {
            return medicoRepository.findByEspecialidad_CodEspe(codEspe);
        }
        return medicoRepository.findAll();
    }

    public Medico buscar(String codMed) {
        return medicoRepository.findById(CodigoIdentidad.medico(codMed))
                .orElseThrow(() -> new IllegalArgumentException("No existe el medico"));
    }

    @Transactional
    public Medico guardar(MedicoRequest request) {
        String documento = CodigoIdentidad.documentoBase(request.getNumDoc());
        String codMed = request.getCodMed() != null && !request.getCodMed().isBlank()
                ? CodigoIdentidad.medico(request.getCodMed())
                : CodigoIdentidad.medico(documento);
        validarDocumentoLibre(documento, codMed, request.getCorreo());

        if (medicoRepository.existsById(codMed)) {
            throw new IllegalArgumentException("Ya existe un medico con ese codigo");
        }
        Especialidad especialidad = especialidadService.buscar(request.getCodEspe());

        Medico medico = new Medico();
        medico.setCodMed(codMed);
        medico.setNombre(request.getNombre());
        medico.setApellido(request.getApellido());
        medico.setEspecialidad(especialidad);
        medicoRepository.save(medico);

        Usuario usuario = new Usuario();
        usuario.setMedico(medico);
        usuario.setRol(Rol.MEDICO);
        usuario.setCorreo(request.getCorreo());
        usuario.setClaveHash(passwordEncoder.encode(request.getClave()));
        usuarioRepository.save(usuario);

        return medico;
    }

    public void eliminar(String codMed) {
        String codigo = CodigoIdentidad.medico(codMed);
        if (!medicoRepository.existsById(codigo)) {
            throw new IllegalArgumentException("No existe el medico");
        }
        medicoRepository.deleteById(codigo);
    }

    private void validarDocumentoLibre(String documento, String codMed, String correo) {
        if (pacienteRepository.existsById(CodigoIdentidad.paciente(documento)) || pacienteRepository.existsById(documento)) {
            throw new IllegalArgumentException("Ese documento ya esta registrado como paciente");
        }
        if (encargadoCitasRepository.existsById(CodigoIdentidad.encargado(documento))
                || encargadoCitasRepository.existsByNumDoc(documento)) {
            throw new IllegalArgumentException("Ese documento ya esta registrado como encargado de citas");
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }
        if (usuarioRepository.existsByMedico_CodMed(codMed)) {
            throw new IllegalArgumentException("Ya existe un usuario para ese medico");
        }
    }
}
