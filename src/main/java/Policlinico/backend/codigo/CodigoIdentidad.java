package Policlinico.backend.codigo;

public final class CodigoIdentidad {

    private CodigoIdentidad() {
    }

    public static String documentoBase(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El numero de documento es obligatorio");
        }
        String valor = documento.trim().toUpperCase();
        if (valor.length() > 1 && (valor.charAt(0) == 'P' || valor.charAt(0) == 'M' || valor.charAt(0) == 'E')) {
            return valor.substring(1);
        }
        return valor;
    }

    public static String paciente(String documento) {
        return "P" + documentoBase(documento);
    }

    public static String medico(String documento) {
        return "M" + documentoBase(documento);
    }

    public static String encargado(String documento) {
        return "E" + documentoBase(documento);
    }
}
