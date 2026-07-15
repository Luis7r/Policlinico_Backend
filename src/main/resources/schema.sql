CREATE TABLE IF NOT EXISTS paciente (
    numDoc VARCHAR(12) NOT NULL,
    tipoDoc ENUM('DNI', 'CE', 'PASAPORTE') NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    sexo ENUM('M', 'F') NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    PRIMARY KEY (numDoc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS especialidad (
    codEspe INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NULL DEFAULT 60.00,
    PRIMARY KEY (codEspe),
    UNIQUE KEY uk_especialidad_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS medico (
    codMed VARCHAR(12) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    codEspe INT NOT NULL,
    PRIMARY KEY (codMed),
    CONSTRAINT fk_medico_especialidad
        FOREIGN KEY (codEspe) REFERENCES especialidad (codEspe)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS encargado_citas (
    codEncargado VARCHAR(12) NOT NULL,
    numDoc VARCHAR(12) NOT NULL,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    PRIMARY KEY (codEncargado),
    UNIQUE KEY uk_encargado_num_doc (numDoc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS horario (
    codHor INT NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    dniMed VARCHAR(12) NOT NULL,
    codEncargado VARCHAR(12) NULL,
    consultorio VARCHAR(50) NULL,
    PRIMARY KEY (codHor),
    CONSTRAINT fk_horario_medico
        FOREIGN KEY (dniMed) REFERENCES medico (codMed),
    CONSTRAINT fk_horario_encargado
        FOREIGN KEY (codEncargado) REFERENCES encargado_citas (codEncargado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS solicitud_medica (
    idSolicitud INT NOT NULL AUTO_INCREMENT,
    codMed VARCHAR(12) NOT NULL,
    fecha DATE NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    estado ENUM('pendientes', 'aceptado', 'rechazado') NOT NULL DEFAULT 'pendientes',
    PRIMARY KEY (idSolicitud),
    CONSTRAINT fk_solicitud_medico
        FOREIGN KEY (codMed) REFERENCES medico (codMed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS disponibilidad (
    codDis INT NOT NULL AUTO_INCREMENT,
    codHor INT NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    estado ENUM('disponible', 'reservado', 'no_disponible') NOT NULL DEFAULT 'disponible',
    PRIMARY KEY (codDis),
    UNIQUE KEY uk_disponibilidad_bloque (codHor, horaInicio, horaFin),
    CONSTRAINT fk_disponibilidad_horario
        FOREIGN KEY (codHor) REFERENCES horario (codHor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
    idUser INT NOT NULL AUTO_INCREMENT,
    codPac VARCHAR(12) NULL,
    codMed VARCHAR(12) NULL,
    codEncargado VARCHAR(12) NULL,
    rol ENUM('PACIENTE', 'MEDICO', 'ENCARGADO_CITAS', 'ADMIN') NOT NULL,
    correo VARCHAR(150) NOT NULL,
    clave_hash VARCHAR(100) NOT NULL,
    PRIMARY KEY (idUser),
    UNIQUE KEY uk_usuarios_correo (correo),
    UNIQUE KEY uk_usuarios_paciente (codPac),
    UNIQUE KEY uk_usuarios_medico (codMed),
    UNIQUE KEY uk_usuarios_encargado (codEncargado),
    CONSTRAINT fk_usuarios_paciente
        FOREIGN KEY (codPac) REFERENCES paciente (numDoc),
    CONSTRAINT fk_usuarios_medico
        FOREIGN KEY (codMed) REFERENCES medico (codMed),
    CONSTRAINT fk_usuarios_encargado
        FOREIGN KEY (codEncargado) REFERENCES encargado_citas (codEncargado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cita (
    codCita INT NOT NULL AUTO_INCREMENT,
    numDoc VARCHAR(12) NOT NULL,
    codDis INT NOT NULL,
    estado ENUM('pendiente', 'reprogramado', 'cancelado', 'atendido', 'ausente') NOT NULL DEFAULT 'pendiente',
    consultorio VARCHAR(50) NULL,
    PRIMARY KEY (codCita),
    CONSTRAINT fk_cita_paciente
        FOREIGN KEY (numDoc) REFERENCES paciente (numDoc),
    CONSTRAINT fk_cita_disponibilidad
        FOREIGN KEY (codDis) REFERENCES disponibilidad (codDis)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE usuarios MODIFY correo VARCHAR(150) NOT NULL;
ALTER TABLE usuarios MODIFY clave_hash VARCHAR(100) NOT NULL;
