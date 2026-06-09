package Policlinico.backend.encargado;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EncargadoCitasRequest {

    @NotBlank
    @Size(max = 12)
    private String numDoc;

    @NotBlank
    @Size(max = 45)
    private String nombre;

    @NotBlank
    @Size(max = 45)
    private String apellido;

    @NotBlank
    @Email
    @Size(max = 150)
    private String correo;

    @NotBlank
    @Size(min = 8, max = 72)
    private String clave;

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
