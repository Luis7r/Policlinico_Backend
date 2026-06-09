package Policlinico.backend.especialidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codEspe")
    private Integer codEspe;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    public Integer getCodEspe() {
        return codEspe;
    }

    public void setCodEspe(Integer codEspe) {
        this.codEspe = codEspe;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
