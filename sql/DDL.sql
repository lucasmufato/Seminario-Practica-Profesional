CREATE TABLE PERSONA (
	id_persona INTEGER AUTO_INCREMENT,
	nombres VARCHAR(60) NOT NULL,
	apellidos VARCHAR(60) NOT NULL,
	tipo_doc SMALLINT NOT NULL,
	nro_doc BIGINT NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	sexo CHAR(1) NOT NULL,
	domicilio VARCHAR(120),
	telefono VARCHAR(16),
	descripcion TEXT,
	estado CHAR(1) NOT NULL,
	
	UNIQUE (tipo_doc,nro_doc),
	PRIMARY KEY (id_persona)
);

CREATE TABLE USUARIO (
	id_usuario INTEGER NOT NULL AUTO_INCREMENT,
	id_persona INTEGER,
	nombre_usuario VARCHAR(30) NOT NULL,
	password VARCHAR(32) NOT NULL,
	email VARCHAR(40) NOT NULL,
	descripcion TEXT,
	estado CHAR(1) NOT NULL, -- PODRIA SER DEL TIPO: A=ACTIVO, B=BAJA, S=SUSPENDIDO, ETC
	tipo CHAR(1), -- ESTE SE USA AUTOMATICO PARA LA HERENCIA 

	PRIMARY KEY (id_usuario),
	UNIQUE (nombre_usuario),
	UNIQUE (email),
	FOREIGN KEY (id_persona) REFERENCES PERSONA (id_persona)
);

CREATE TABLE CLIENTE(
	id_usuario Integer NOT NULL,
	puntos Integer,
	reputacion Integer,
	foto_registro VARCHAR(120),
	foto VARCHAR(120),

	PRIMARY KEY(id_usuario),
	FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario)

);

CREATE TABLE ROL (
	id_rol INTEGER NOT NULL AUTO_INCREMENT,
	nombre_rol VARCHAR(30) NOT NULL,
	nombre_amigable VARCHAR(30) NOT NULL,
	descripcion TEXT,
	estado CHAR(1) NOT NULL,

	PRIMARY KEY (id_rol),
	UNIQUE (nombre_rol)
);

CREATE TABLE USUARIO_ROL (
	id_usuario INTEGER NOT NULL,
	id_rol INTEGER NOT NULL,
	fecha_modificacion DATE NOT NULL,
	
	PRIMARY KEY (id_usuario, id_rol),
	FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario),
	FOREIGN KEY (id_rol) REFERENCES ROL (id_rol)
);


CREATE TABLE PERMISO (
	id_permiso INTEGER AUTO_INCREMENT,
	nombre_permiso VARCHAR (50) NOT NULL,
	funcionalidad VARCHAR(200),
	descripcion TEXT,
	estado CHAR(1) NOT NULL,

	PRIMARY KEY (id_permiso),
	UNIQUE (nombre_permiso)
);

CREATE TABLE PERMISO_ROL (
	id_permiso INTEGER NOT NULL,
	id_rol INTEGER NOT NULL,
	fecha_modificacion DATE NOT NULL,

	PRIMARY KEY(id_permiso,id_rol),
	FOREIGN KEY (id_permiso) REFERENCES PERMISO(id_permiso),
	FOREIGN KEY (id_rol) REFERENCES ROL (id_rol)

);

CREATE TABLE LOCALIDADES (
	id INTEGER NOT NULL,
	nombre VARCHAR (200),
	nombre_ascii VARCHAR (200),
	nombres_alternativos VARCHAR(10000),
	lat FLOAT,
	lng FLOAT,
	clasificacion CHAR(1),
	clase_punto VARCHAR(10),
	pais  CHAR(2),
	paises_alternativos VARCHAR(200),
	admin1 VARCHAR(20),
	admin2 VARCHAR(80),
	admin3 VARCHAR(20),
	admin4 VARCHAR(20),
	poblacion BIGINT,
	elevacion INTEGER,
	dem INTEGER,
	zona_horaria VARCHAR(40),
	fecha_modificacion DATE,


	PRIMARY KEY (id)
);

CREATE TABLE LOCALIDADES_CLASIFICACION (
	codigo CHAR(1),
	nombre_clase VARCHAR(50),

	PRIMARY KEY (codigo)
);
