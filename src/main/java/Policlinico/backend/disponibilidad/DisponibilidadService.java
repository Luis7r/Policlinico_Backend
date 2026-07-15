package Policlinico.backend.disponibilidad;

import Policlinico.backend.horario.Horario;
import Policlinico.backend.horario.HorarioRequest;
import Policlinico.backend.horario.HorarioService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
            LocalDate fechaInicio,
            LocalDate fechaFin,
            boolean incluirPasadas,
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

        resultado = filtrarPorRango(resultado, fechaInicio, fechaFin);

        return incluirPasadas ? resultado : filtrarDisponibilidades(resultado, fecha);
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

        List<Disponibilidad> creadas = guardarBloques(
                horario,
                request.getHoraInicio(),
                request.getHoraFin(),
                duracionMinutos(request.getDuracionMinutos()));

        if (creadas.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se generaron bloques nuevos. Revise el rango o duplicados existentes");
        }

        return creadas;
    }

    public List<Disponibilidad> guardarMasivo(DisponibilidadMasivaRequest request) {
        validarRangoFechas(request.getFechaInicio(), request.getFechaFin());

        List<Disponibilidad> creadas = new ArrayList<>();
        Map<LocalDate, DisponibilidadDiaRequest> dias = mapearDias(request.getDias());
        int duracionMinutos = duracionMinutos(request.getDuracionMinutos());

        for (LocalDate fecha = request.getFechaInicio(); !fecha.isAfter(request.getFechaFin()); fecha = fecha.plusDays(1)) {
            LocalTime horaInicio = request.getMismaHora() ? request.getHoraInicio() : obtenerDia(dias, fecha).getHoraInicio();
            LocalTime horaFin = request.getMismaHora() ? request.getHoraFin() : obtenerDia(dias, fecha).getHoraFin();

            validarHoras(horaInicio, horaFin);

            HorarioRequest horarioRequest = new HorarioRequest();
            horarioRequest.setFecha(fecha);
            horarioRequest.setCodMed(request.getCodMed());
            horarioRequest.setCodEncargado(request.getCodEncargado());
            horarioRequest.setConsultorio(request.getConsultorio());

            Horario horario = horarioService.guardar(horarioRequest);
            creadas.addAll(guardarBloques(horario, horaInicio, horaFin, duracionMinutos));
        }

        if (creadas.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se generaron disponibilidades. Revise las fechas, horas o duplicados existentes");
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

    private List<Disponibilidad> guardarBloques(
            Horario horario,
            LocalTime horaInicio,
            LocalTime horaFin,
            int duracionMinutos) {
        validarHoras(horaInicio, horaFin);

        LocalTime inicio = horaInicio;
        List<Disponibilidad> creadas = new ArrayList<>();

        while (!inicio.plusMinutes(duracionMinutos).isAfter(horaFin)) {
            LocalTime bloqueFin = inicio.plusMinutes(duracionMinutos);

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

        return creadas;
    }

    private void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser menor o igual a la fecha de fin");
        }
    }

    private List<Disponibilidad> filtrarPorRango(
            List<Disponibilidad> disponibilidades,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        if (fechaInicio == null && fechaFin == null) {
            return disponibilidades;
        }
        LocalDate inicio = fechaInicio != null ? fechaInicio : fechaFin;
        LocalDate fin = fechaFin != null ? fechaFin : fechaInicio;
        validarRangoFechas(inicio, fin);
        return disponibilidades.stream()
                .filter(disponibilidad -> {
                    LocalDate fecha = disponibilidad.getHorario().getFecha();
                    return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
                })
                .toList();
    }

    private void validarHoras(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null || horaFin == null || !horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor a la hora de fin");
        }
    }

    private int duracionMinutos(Integer duracionMinutos) {
        if (duracionMinutos == null) {
            return 30;
        }
        if (duracionMinutos < 5 || duracionMinutos > 240) {
            throw new IllegalArgumentException("La duracion de la cita debe estar entre 5 y 240 minutos");
        }
        return duracionMinutos;
    }

    private Map<LocalDate, DisponibilidadDiaRequest> mapearDias(List<DisponibilidadDiaRequest> dias) {
        if (dias == null) {
            return Map.of();
        }
        return dias.stream()
                .collect(Collectors.toMap(DisponibilidadDiaRequest::getFecha, Function.identity(), (actual, repetido) -> actual));
    }

    private DisponibilidadDiaRequest obtenerDia(Map<LocalDate, DisponibilidadDiaRequest> dias, LocalDate fecha) {
        DisponibilidadDiaRequest dia = dias.get(fecha);
        if (dia == null) {
            throw new IllegalArgumentException("Debe indicar el rango horario para la fecha " + fecha);
        }
        return dia;
    }
}
