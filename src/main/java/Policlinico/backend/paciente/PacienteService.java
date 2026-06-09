package Policlinico.backend.paciente;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.encargado.EncargadoCitasRepository;
import Policlinico.backend.medico.MedicoRepository;
import Policlinico.backend.paciente.dto.RegistroPacienteRequest;
import Policlinico.backend.paciente.dto.RegistroPacienteResponse;
import Policlinico.backend.usuario.Rol;
import Policlinico.backend.usuario.Usuario;
import Policlinico.backend.usuario.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EncargadoCitasRepository encargadoCitasRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscar(String numDoc) {
        return pacienteRepository.findById(CodigoIdentidad.paciente(numDoc));
    }

    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public RegistroPacienteResponse registrar(RegistroPacienteRequest request) {
        String documento = CodigoIdentidad.documentoBase(request.getNumDoc());
        String codPaciente = CodigoIdentidad.paciente(documento);

        if (pacienteRepository.existsById(codPaciente)) {
            throw new IllegalArgumentException("Ya existe un paciente con ese numero de documento");
        }
        if (medicoRepository.existsById(CodigoIdentidad.medico(documento)) || medicoRepository.existsById(documento)) {
            throw new IllegalArgumentException("Ese documento ya esta registrado como medico");
        }
        if (encargadoCitasRepository.existsById(CodigoIdentidad.encargado(documento))
                || encargadoCitasRepository.existsByNumDoc(documento)) {
            throw new IllegalArgumentException("Ese documento ya esta registrado como encargado de citas");
        }
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }

        Paciente paciente = new Paciente(
                codPaciente,
                request.getTipoDoc(),
                request.getNombre(),
                request.getApellido(),
                request.getSexo(),
                request.getDireccion()
        );
        pacienteRepository.save(paciente);

        Usuario usuario = new Usuario();
        usuario.setPaciente(paciente);
        usuario.setRol(Rol.PACIENTE);
        usuario.setCorreo(request.getCorreo());
        usuario.setClaveHash(passwordEncoder.encode(request.getClave()));
        usuarioRepository.save(usuario);

        RegistroPacienteResponse response = new RegistroPacienteResponse();
        response.setNumDoc(paciente.getNumDoc());
        response.setTipoDoc(paciente.getTipoDoc());
        response.setNombre(paciente.getNombre());
        response.setApellido(paciente.getApellido());
        response.setSexo(paciente.getSexo());
        response.setDireccion(paciente.getDireccion());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol().name());
        return response;
    }

    public void eliminar(String numDoc) {
        pacienteRepository.deleteById(CodigoIdentidad.paciente(numDoc));
    }
}
