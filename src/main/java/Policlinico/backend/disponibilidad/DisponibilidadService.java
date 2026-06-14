package Policlinico.backend.disponibilidad;

import Policlinico.backend.codigo.CodigoIdentidad;
import Policlinico.backend.horario.Horario;
import Policlinico.backend.horario.HorarioService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<Disponibilidad> listar(
        EstadoDisponibilidad estado,
        LocalDate fecha,
        String codMed) {

    if (estado != null &&
        fecha != null &&
        codMed != null &&
        !codMed.isBlank()) {

        return disponibilidadRepository
                .findByEstadoAndHorario_FechaAndHorario_Medico_CodMed(
                        estado,
                        fecha,
                        codMed);
    }

    if (estado != null &&
        codMed != null &&
        !codMed.isBlank()) {

        return disponibilidadRepository
                .findByEstadoAndHorario_Medico_CodMed(
                        estado,
                        codMed);
    }

    if (estado != null) {
        return disponibilidadRepository.findByEstado(estado);
    }

    if (fecha != null) {
        return disponibilidadRepository.findByHorario_Fecha(fecha);
    }

    if (codMed != null && !codMed.isBlank()) {
        return disponibilidadRepository.findByHorario_Medico_CodMed(codMed);
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

    public List<Disponibilidad> guardarRango(DisponibilidadRangoRequest request) {
        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor a la hora de fin");
        }

        Horario horario = horarioService.buscar(request.getCodHor());
        LocalTime inicio = request.getHoraInicio();
        LocalTime fin = request.getHoraFin();
        List<Disponibilidad> creadas = new ArrayList<>();

        while (!inicio.plusMinutes(30).isAfter(fin)) {
            LocalTime bloqueFin = inicio.plusMinutes(30);
            if (!disponibilidadRepository.existsByHorario_CodHorAndHoraInicioAndHoraFin(
                    horario.getCodHor(), inicio, bloqueFin)) {
                Disponibilidad disponibilidad = new Disponibilidad();
                disponibilidad.setHorario(horario);
                disponibilidad.setHoraInicio(inicio);
                disponibilidad.setHoraFin(bloqueFin);
                disponibilidad.setEstado(EstadoDisponibilidad.DISPONIBLE);
                creadas.add(disponibilidadRepository.save(disponibilidad));
            }
            inicio = bloqueFin;
        }

        if (creadas.isEmpty()) {
            throw new IllegalArgumentException("No se generaron bloques nuevos. Revise el rango o duplicados existentes");
        }

        return creadas;
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
