package Policlinico.backend.horario;

import Policlinico.backend.encargado.EncargadoCitas;
import Policlinico.backend.medico.Medico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codHor")
    private Integer codHor;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "dniMed", referencedColumnName = "codMed")
    private Medico medico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codEncargado", referencedColumnName = "codEncargado")
    private EncargadoCitas encargadoCitas;

    @Column(name = "consultorio", length = 50)
    private String consultorio;

    public Integer getCodHor() {
        return codHor;
    }

    public void setCodHor(Integer codHor) {
        this.codHor = codHor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public EncargadoCitas getEncargadoCitas() {
        return encargadoCitas;
    }

    public void setEncargadoCitas(EncargadoCitas encargadoCitas) {
        this.encargadoCitas = encargadoCitas;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }
}
