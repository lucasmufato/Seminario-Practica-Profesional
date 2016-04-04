CREATE TABLE PERSONA (
	id_persona INTEGER AUTO_INCREMENT,
	nombres VARCHAR(60) NOT NULL,
	apellidos VARCHAR(60) NOT NULL,
	tipo_doc SMALLINT NOT NULL,
	nro_doc BIGINT NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	domicilio VARCHAR(120) NOT NULL,
	telefono VARCHAR(16) NOT NULL,
	descripcion TEXT,
	estado BOOLEAN NOT NULL,

	PRIMARY KEY (id_persona)
);

CREATE TABLE USUARIO (
	id_usuario INTEGER NOT NULL AUTO_INCREMENT,
	id_persona INTEGER,
	nombre_usuario VARCHAR(30) NOT NULL,
	password VARCHAR(32) NOT NULL,
	email VARCHAR(40) NOT NULL,
	descripcion TEXT,
	estado BOOLEAN NOT NULL,

	PRIMARY KEY (id_usuario),
	FOREIGN KEY (id_persona) REFERENCES PERSONA (id_persona)
);

CREATE TABLE ROL (
	id_rol INTEGER NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(30) NOT NULL,
	nombre_amigable VARCHAR(30) NOT NULL,
	descripcion TEXT,
	estado BOOLEAN NOT NULL,

	PRIMARY KEY (id_rol)
);

CREATE TABLE USUARIO_ROL (
	id_usuario INTEGER NOT NULL,
	id_rol INTEGER NOT NULL,

	PRIMARY KEY (id_usuario, id_rol),
	FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario),
	FOREIGN KEY (id_rol) REFERENCES ROL (id_rol)
);

// LAS TABLAS DE ACA ABAJO FUERON MODIFICADAS

CREATE TABLE PERMISO (
	id_permiso INTEGER,
	recurso VARCHAR(200),
	lectura BOOLEAN,
	escritura BOOLEAN,

	PRIMARY KEY (id_permiso)
);

CREATE TABLE PERMISO_ROL (
	id_permiso INTEGER NOT NULL,
	id_rol INTEGER NOT NULL,
	fecha_modificacion DATE,

	PRIMARY KEY(id_permiso,id_rol),
	FOREIGN KEY (id_permiso) REFERENCES PERMISO(id_permiso),
	FOREIGN KEY (id_rol) REFERENCES ROL (id_rol)

);
