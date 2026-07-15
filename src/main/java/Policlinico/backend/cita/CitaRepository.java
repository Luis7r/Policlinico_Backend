package Policlinico.backend.cita;

import java.util.List;
import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByPaciente_NumDoc(String numDoc);

    List<Cita> findByDisponibilidad_Horario_Medico_CodMedAndEstadoIn(String codMed, Collection<EstadoCita> estados);

    List<Cita> findByDisponibilidad_Horario_Medico_CodMedAndEstado(String codMed, EstadoCita estado);

    @Query("""
            SELECT COUNT(c)
            FROM Cita c
            WHERE c.paciente.numDoc = :numDoc
              AND c.estado IN :estados
              AND c.disponibilidad.horario.fecha = :fecha
              AND (:codCitaExcluir IS NULL OR c.codCita <> :codCitaExcluir)
              AND c.disponibilidad.horaInicio < :horaFin
              AND c.disponibilidad.horaFin > :horaInicio
            """)
    long contarCrucesHorarioPaciente(
            @Param("numDoc") String numDoc,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("estados") Collection<EstadoCita> estados,
            @Param("codCitaExcluir") Integer codCitaExcluir);
}
