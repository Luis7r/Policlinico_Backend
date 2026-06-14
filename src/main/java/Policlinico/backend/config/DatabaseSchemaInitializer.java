package Policlinico.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSchemaInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSchemaInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        ejecutar("SET FOREIGN_KEY_CHECKS=0");
        ejecutar("""
                CREATE TABLE IF NOT EXISTS encargado_citas (
                    codEncargado VARCHAR(12) NOT NULL,
                    numDoc VARCHAR(12) NOT NULL,
                    nombre VARCHAR(45) NOT NULL,
                    apellido VARCHAR(45) NOT NULL,
                    PRIMARY KEY (codEncargado),
                    UNIQUE KEY uk_encargado_num_doc (numDoc)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);
        ejecutar("""
                CREATE TABLE IF NOT EXISTS solicitud_medica (
                    idSolicitud INT NOT NULL AUTO_INCREMENT,
                    codMed VARCHAR(12) NOT NULL,
                    fecha DATE NOT NULL,
                    horaInicio TIME NOT NULL,
                    horaFin TIME NOT NULL,
                    PRIMARY KEY (idSolicitud),
                    CONSTRAINT fk_solicitud_medico
                        FOREIGN KEY (codMed) REFERENCES medico (codMed)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);
        ejecutar("ALTER TABLE paciente MODIFY numDoc VARCHAR(12) NOT NULL");
        ejecutar("ALTER TABLE medico MODIFY codMed VARCHAR(12) NOT NULL");
        ejecutar("ALTER TABLE encargado_citas MODIFY codEncargado VARCHAR(12) NOT NULL");
        ejecutar("ALTER TABLE horario MODIFY dniMed VARCHAR(12)");
        ejecutar("ALTER TABLE horario MODIFY codEncargado VARCHAR(12)");
        ejecutar("ALTER TABLE cita MODIFY numDoc VARCHAR(12)");
        ejecutar("ALTER TABLE usuarios MODIFY codPac VARCHAR(12)");
        agregarColumnaSiNoExiste("usuarios", "codMed", "VARCHAR(12) NULL");
        agregarColumnaSiNoExiste("usuarios", "codEncargado", "VARCHAR(12) NULL");
        agregarColumnaSiNoExiste("horario", "codEncargado", "VARCHAR(12) NULL");
        ejecutar("ALTER TABLE usuarios MODIFY rol ENUM('PACIENTE','MEDICO','ENCARGADO_CITAS','ADMIN')");
        ejecutar("ALTER TABLE usuarios MODIFY correo VARCHAR(150) NOT NULL");
        ejecutar("ALTER TABLE usuarios MODIFY clave_hash VARCHAR(100) NOT NULL");
        ejecutar("ALTER TABLE disponibilidad MODIFY estado ENUM('disponible','reservado','no_disponible') DEFAULT 'disponible'");
        ejecutar("ALTER TABLE cita MODIFY estado ENUM('pendiente','reprogramado','cancelado','atendido') DEFAULT 'pendiente'");
        ejecutar("SET FOREIGN_KEY_CHECKS=1");
    }

    private void ejecutar(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (RuntimeException ex) {
            LOGGER.warn("No se pudo ejecutar ajuste de esquema [{}]: {}", sql, ex.getMessage());
        }
    }

    private void agregarColumnaSiNoExiste(String tabla, String columna, String definicion) {
        Integer total = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND column_name = ?
                """, Integer.class, tabla, columna);
        if (total == null || total == 0) {
            ejecutar("ALTER TABLE " + tabla + " ADD COLUMN " + columna + " " + definicion);
        }
    }
}
