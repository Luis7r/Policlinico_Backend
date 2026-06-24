package Policlinico.backend.disponibilidad;

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

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository,
                                 HorarioService horarioService) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.horarioService = horarioService;
    }

    public List<Disponibilidad> listar(
            EstadoDisponibilidad estado,
            LocalDate fecha,
            String codMed) {

        List<Disponibilidad> resultado;

        if (estado != null &&
                fecha != null &&
                codMed != null &&
                !codMed.isBlank()) {

            resultado = disponibilidadRepository
                    .findByEstadoAndHorario_FechaAndHorario_Medico_CodMed(
                            estado,
                            fecha,
                            codMed);

        } else if (estado != null &&
                codMed != null &&
                !codMed.isBlank()) {

            resultado = disponibilidadRepository
                    .findByEstadoAndHorario_Medico_CodMed(
                            estado,
                            codMed);

        } else if (estado != null) {

            resultado = disponibilidadRepository.findByEstado(estado);

        } else if (fecha != null) {

            resultado = disponibilidadRepository.findByHorario_Fecha(fecha);

        } else if (codMed != null && !codMed.isBlank()) {

            resultado = disponibilidadRepository.findByHorario_Medico_CodMed(codMed);

        } else {

            resultado = disponibilidadRepository.findAll();
        }

        return filtrarDisponibilidades(resultado, fecha);
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
        disponibilidad.setEstado(
                request.getEstado() != null
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
                    horario.getCodHor(),
                    inicio,
                    bloqueFin)) {

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
            throw new IllegalArgumentException(
                    "No se generaron bloques nuevos. Revise el rango o duplicados existentes");
        }

        return creadas;
    }

    public Disponibilidad cambiarEstado(Integer codDis,
                                        EstadoDisponibilidad estado) {

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

    /**
     * Reglas:
     *
     * - Si la fecha consultada es anterior a hoy -> lista vacía.
     * - Si la fecha es hoy -> solo horas posteriores a la hora actual.
     * - Si la fecha es futura -> mostrar todas.
     * - Si no se envía fecha -> ocultar todas las disponibilidades pasadas.
     */
    private List<Disponibilidad> filtrarDisponibilidades(
            List<Disponibilidad> disponibilidades,
            LocalDate fechaConsultada) {

        LocalDate hoy = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        // Si consultan explícitamente una fecha pasada
        if (fechaConsultada != null && fechaConsultada.isBefore(hoy)) {
            return List.of();
        }

        return disponibilidades.stream()
                .filter(d -> {

                    LocalDate fecha = d.getHorario().getFecha();

                    // Fechas futuras
                    if (fecha.isAfter(hoy)) {
                        return true;
                    }

                    // Fechas pasadas
                    if (fecha.isBefore(hoy)) {
                        return false;
                    }

                    // Fecha de hoy
                    return d.getHoraInicio().isAfter(horaActual);

                })
                .toList();
    }

}