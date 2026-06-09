package Policlinico.backend.encargado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "encargado_citas")
public class EncargadoCitas {

    @Id
    @Column(name = "codEncargado", length = 12)
    private String codEncargado;

    @Column(name = "numDoc", nullable = false, unique = true, length = 12)
    private String numDoc;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    public String getCodEncargado() {
        return codEncargado;
    }

    public void setCodEncargado(String codEncargado) {
        this.codEncargado = codEncargado;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
