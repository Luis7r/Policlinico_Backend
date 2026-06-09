package Policlinico.backend.auth;

import Policlinico.backend.encargado.EncargadoCitas;
import Policlinico.backend.medico.Medico;
import Policlinico.backend.auth.dto.LoginRequest;
import Policlinico.backend.auth.dto.LoginResponse;
import Policlinico.backend.paciente.Paciente;
import Policlinico.backend.usuario.Usuario;
import Policlinico.backend.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo().trim())
                .orElseThrow(() -> new IllegalArgumentException("Correo o clave incorrectos"));

        if (!passwordEncoder.matches(request.getClave(), usuario.getClaveHash())) {
            throw new IllegalArgumentException("Correo o clave incorrectos");
        }

        LoginResponse response = new LoginResponse();
        response.setIdUser(usuario.getIdUser());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol().name());

        Paciente paciente = usuario.getPaciente();
        if (paciente != null) {
            response.setCodigo(paciente.getNumDoc());
            response.setNumDoc(paciente.getNumDoc());
            response.setNombreCompleto(paciente.getNombre() + " " + paciente.getApellido());
        }
        Medico medico = usuario.getMedico();
        if (medico != null) {
            response.setCodigo(medico.getCodMed());
            response.setNumDoc(medico.getCodMed().substring(1));
            response.setNombreCompleto(medico.getNombre() + " " + medico.getApellido());
        }
        EncargadoCitas encargado = usuario.getEncargadoCitas();
        if (encargado != null) {
            response.setCodigo(encargado.getCodEncargado());
            response.setNumDoc(encargado.getNumDoc());
            response.setNombreCompleto(encargado.getNombre() + " " + encargado.getApellido());
        }
        return response;
    }
}
