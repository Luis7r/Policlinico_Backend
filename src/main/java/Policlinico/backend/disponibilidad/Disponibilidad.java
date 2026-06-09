package Policlinico.backend.disponibilidad;

import Policlinico.backend.horario.Horario;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codDis")
    private Integer codDis;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "codHor", referencedColumnName = "codHor")
    private Horario horario;

    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false)
    private LocalTime horaFin;

    @Convert(converter = EstadoDisponibilidadConverter.class)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoDisponibilidad estado = EstadoDisponibilidad.DISPONIBLE;

    public Integer getCodDis() {
        return codDis;
    }

    public void setCodDis(Integer codDis) {
        this.codDis = codDis;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public EstadoDisponibilidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoDisponibilidad estado) {
        this.estado = estado;
    }
}
