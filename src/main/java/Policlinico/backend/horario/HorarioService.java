package Policlinico.backend.horario;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.encargado.EncargadoCitas;
import Policlinico.backend.encargado.EncargadoCitasService;
import Policlinico.backend.medico.Medico;
import Policlinico.backend.medico.MedicoService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final MedicoService medicoService;
    private final EncargadoCitasService encargadoCitasService;

    public HorarioService(
            HorarioRepository horarioRepository,
            MedicoService medicoService,
            EncargadoCitasService encargadoCitasService) {
        this.horarioRepository = horarioRepository;
        this.medicoService = medicoService;
        this.encargadoCitasService = encargadoCitasService;
    }

    public List<Horario> listar(LocalDate fecha, String codMed, String codEncargado) {
        if (fecha != null) {
            return horarioRepository.findByFecha(fecha);
        }
        if (codMed != null && !codMed.isBlank()) {
            return horarioRepository.findByMedico_CodMed(CodigoIdentidad.medico(codMed));
        }
        if (codEncargado != null && !codEncargado.isBlank()) {
            return horarioRepository.findByEncargadoCitas_CodEncargado(CodigoIdentidad.encargado(codEncargado));
        }
        return horarioRepository.findAll();
    }

    public Horario buscar(Integer codHor) {
        return horarioRepository.findById(codHor)
                .orElseThrow(() -> new IllegalArgumentException("No existe el horario"));
    }

    public Horario guardar(HorarioRequest request) {
        Medico medico = medicoService.buscar(request.getCodMed());
        EncargadoCitas encargadoCitas = encargadoCitasService.buscar(request.getCodEncargado());

        Horario horario = new Horario();
        horario.setFecha(request.getFecha());
        horario.setMedico(medico);
        horario.setEncargadoCitas(encargadoCitas);
        return horarioRepository.save(horario);
    }

    public void eliminar(Integer codHor) {
        if (!horarioRepository.existsById(codHor)) {
            throw new IllegalArgumentException("No existe el horario");
        }
        horarioRepository.deleteById(codHor);
    }
}
