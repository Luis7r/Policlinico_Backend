package Policlinico.backend.disponibilidad;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.horario.Horario;
import Policlinico.backend.horario.HorarioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final HorarioService horarioService;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository, HorarioService horarioService) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.horarioService = horarioService;
    }

    public List<Disponibilidad> listar(EstadoDisponibilidad estado, LocalDate fecha, String codMed) {
        if (estado != null) {
            return disponibilidadRepository.findByEstado(estado);
        }
        if (fecha != null) {
            return disponibilidadRepository.findByHorario_Fecha(fecha);
        }
        if (codMed != null && !codMed.isBlank()) {
            return disponibilidadRepository.findByHorario_Medico_CodMed(CodigoIdentidad.medico(codMed));
        }
        return disponibilidadRepository.findAll();
    }

    public Disponibilidad buscar(Integer codDis) {
        return disponibilidadRepository.findById(codDis)
                .orElseThrow(() -> new IllegalArgumentException("No existe la disponibilidad"));
    }

    public Disponibilidad guardar(DisponibilidadRequest request) {
        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor a la hora de fin");
        }

        Horario horario = horarioService.buscar(request.getCodHor());

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setHorario(horario);
        disponibilidad.setHoraInicio(request.getHoraInicio());
        disponibilidad.setHoraFin(request.getHoraFin());
        disponibilidad.setEstado(request.getEstado() != null
                ? request.getEstado()
                : EstadoDisponibilidad.DISPONIBLE);
        return disponibilidadRepository.save(disponibilidad);
    }

    public Disponibilidad cambiarEstado(Integer codDis, EstadoDisponibilidad estado) {
        Disponibilidad disponibilidad = buscar(codDis);
        disponibilidad.setEstado(estado);
        return disponibilidadRepository.save(disponibilidad);
    }

    public void eliminar(Integer codDis) {
        if (!disponibilidadRepository.existsById(codDis)) {
            throw new IllegalArgumentException("No existe la disponibilidad");
        }
        disponibilidadRepository.deleteById(codDis);
    }
}
