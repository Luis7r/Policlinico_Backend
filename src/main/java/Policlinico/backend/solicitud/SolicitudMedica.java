package Policlinico.backend.solicitud;

import Policlinico.backend.medico.Medico;
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
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "solicitud_medica")
public class SolicitudMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSolicitud")
    private Integer idSolicitud;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "codMed", referencedColumnName = "codMed")
    private Medico medico;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false)
    private LocalTime horaFin;

    @Convert(converter = EstadoSolicitudMedicaConverter.class)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitudMedica estado = EstadoSolicitudMedica.PENDIENTE;

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public EstadoSolicitudMedica getEstado() {
        return estado != null ? estado : EstadoSolicitudMedica.PENDIENTE;
    }

    public void setEstado(EstadoSolicitudMedica estado) {
        this.estado = estado;
    }
}
