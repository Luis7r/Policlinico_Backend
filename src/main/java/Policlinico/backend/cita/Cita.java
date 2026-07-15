package Policlinico.backend.cita;

import Policlinico.backend.disponibilidad.Disponibilidad;
import Policlinico.backend.paciente.Paciente;
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

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codCita")
    private Integer codCita;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "numDoc", referencedColumnName = "numDoc")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "codDis", referencedColumnName = "codDis")
    private Disponibilidad disponibilidad;

    @Convert(converter = EstadoCitaConverter.class)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.REGISTRADA;

    @Column(name = "consultorio", length = 50)
    private String consultorio;

    public Integer getCodCita() {
        return codCita;
    }

    public void setCodCita(Integer codCita) {
        this.codCita = codCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }
}
