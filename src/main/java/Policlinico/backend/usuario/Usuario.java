package Policlinico.backend.usuario;

import Policlinico.backend.encargado.EncargadoCitas;
import Policlinico.backend.medico.Medico;
import Policlinico.backend.paciente.Paciente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser")
    private Integer idUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codPac", referencedColumnName = "numDoc")
    private Paciente paciente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codMed", referencedColumnName = "codMed")
    private Medico medico;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codEncargado", referencedColumnName = "codEncargado")
    private EncargadoCitas encargadoCitas;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "clave_hash", nullable = false, length = 100)
    private String claveHash;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClaveHash() {
        return claveHash;
    }

    public void setClaveHash(String claveHash) {
        this.claveHash = claveHash;
    }
}
