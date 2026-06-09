package Policlinico.backend.encargado;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.medico.MedicoRepository;
import Policlinico.backend.paciente.PacienteRepository;
import Policlinico.backend.usuario.Rol;
import Policlinico.backend.usuario.Usuario;
import Policlinico.backend.usuario.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EncargadoCitasService {

    private final EncargadoCitasRepository encargadoCitasRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EncargadoCitasService(
            EncargadoCitasRepository encargadoCitasRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.encargadoCitasRepository = encargadoCitasRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EncargadoCitas> listar() {
        return encargadoCitasRepository.findAll();
    }

    public EncargadoCitas buscar(String codEncargado) {
        return encargadoCitasRepository.findById(codEncargado)
                .or(() -> encargadoCitasRepository.findById(CodigoIdentidad.encargado(codEncargado)))
                .orElseThrow(() -> new IllegalArgumentException("No existe el encargado de citas"));
    }

    @Transactional
    public EncargadoCitas registrar(EncargadoCitasRequest request) {
        String documento = CodigoIdentidad.documentoBase(request.getNumDoc());
        String codEncargado = CodigoIdentidad.encargado(documento);
        validarDocumentoLibre(documento, codEncargado, request.getCorreo());

        EncargadoCitas encargado = new EncargadoCitas();
        encargado.setCodEncargado(codEncargado);
        encargado.setNumDoc(documento);
        encargado.setNombre(request.getNombre());
        encargado.setApellido(request.getApellido());
        encargadoCitasRepository.save(encargado);

        Usuario usuario = new Usuario();
        usuario.setEncargadoCitas(encargado);
        usuario.setRol(Rol.ENCARGADO_CITAS);
        usuario.setCorreo(request.getCorreo());
        usuario.setClaveHash(passwordEncoder.encode(request.getClave()));
        usuarioRepository.save(usuario);

        return encargado;
    }

    private void validarDocumentoLibre(String documento, String codEncargado, String correo) {
        if (encargadoCitasRepository.existsById(codEncargado) || encargadoCitasRepository.existsByNumDoc(documento)) {
            throw new IllegalArgumentException("Ya existe un encargado con ese documento");
        }
        if (pacienteRepository.existsById(CodigoIdentidad.paciente(documento)) || pacienteRepository.existsById(documento)) {
            throw new IllegalArgumentException("Ese documento ya esta registrado como paciente");
        }
        if (medicoRepository.existsById(CodigoIdentidad.medico(documento)) || medicoRepository.existsById(documento)) {
            throw new IllegalArgumentException("Ese documento ya esta registrado como medico");
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }
    }
}
