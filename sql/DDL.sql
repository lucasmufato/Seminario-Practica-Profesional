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

CREATE TABLE LOCALIDAD (
	id_localidad INTEGER NOT NULL,
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


	PRIMARY KEY (id_localidad)
);

CREATE TABLE LOCALIDAD_CLASIFICACION (
	codigo CHAR(1),
	nombre_clase VARCHAR(50),

	PRIMARY KEY (codigo)
);

CREATE TABLE COMISION_COBRADA (
	id_comision_cobrada INTEGER AUTO_INCREMENT,
	monto DECIMAL (10, 2) NOT NULL,
	id_comision INTEGER,
	id_movimiento_saldo INTEGER,
	id_pasajero_viaje INTEGER,

	PRIMARY KEY (id_comision_cobrada)
);

CREATE TABLE CALIFICACION (
	id_calificacion INTEGER AUTO_INCREMENT,
	id_pasajero_viaje INTEGER,
	calificacion_para_conductor INTEGER,
	calificacion_para_pasajero INTEGER,
	participo CHAR(1),
	id_movimiento_puntos INTEGER,

	PRIMARY KEY (id_calificacion)
);

CREATE TABLE VEHICULO (
	id_vehiculo INTEGER NOT NULL AUTO_INCREMENT,
	anio INTEGER NOT NULL,
	marca VARCHAR(30) NOT NULL,
	modelo VARCHAR(30) NOT NULL,
	patente VARCHAR(15) NOT NULL,
	verificado CHAR(1) NOT NULL,
	estado CHAR(1) NOT NULL,
	fecha_verificacion DATE,

	PRIMARY KEY (id_vehiculo),
	UNIQUE (patente)
);

CREATE TABLE MANEJA (
	id_cliente INTEGER NOT NULL,
	id_vehiculo INTEGER NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE,
	

	PRIMARY KEY (id_cliente,id_vehiculo,fecha_inicio),
	FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_usuario),
	FOREIGN KEY (id_vehiculo) REFERENCES VEHICULO (id_vehiculo)
);

CREATE TABLE VIAJE (
	id_viaje INTEGER NOT NULL AUTO_INCREMENT,
	nombre_amigable VARCHAR(30),
	asientos_disponibles INTEGER NOT NULL,
	estado CHAR(1) NOT NULL,
	fecha_inicio DATETIME NOT NULL,
	fecha_alta DATETIME NOT NULL,
	fecha_finalizacion DATETIME,
	fecha_cancelacion DATETIME,
	id_vehiculo INTEGER NOT NULL,
    id_cliente INTEGER NOT NULL,
    fecha_inicio_maneja DATE NOT NULL,
    viaje_complementario INTEGER,
	
	PRIMARY KEY (id_viaje),
	FOREIGN KEY (id_vehiculo) REFERENCES MANEJA (id_vehiculo),
    FOREIGN KEY (id_cliente) REFERENCES MANEJA (id_cliente),
    FOREIGN KEY (fecha_inicio_maneja) REFERENCES MANEJA (fecha_inicio),
    FOREIGN KEY (viaje_complementario) REFERENCES VIAJE (id_viaje)
);

CREATE TABLE LOCALIDAD_VIAJE (
	id_viaje INTEGER NOT NULL,
	id_localidad INTEGER NOT NULL,
	cantidad_pasajeros INTEGER NOT NULL,

	PRIMARY KEY (id_viaje, id_localidad),
	FOREIGN KEY (id_viaje) REFERENCES VIAJE (id_viaje),
	FOREIGN KEY (id_localidad) REFERENCES LOCALIDAD (id_localidad)
);

CREATE TABLE PASAJERO_VIAJE (
	id_pasajero_viaje INTEGER AUTO_INCREMENT,
	id_viaje INTEGER NOT NULL,
	id_cliente INTEGER NOT NULL,
	kilometros FLOAT,
	estado CHAR(1) NOT NULL,
	id_calificacion INTEGER NOT NULL,
	id_comision_cobrada INTEGER NOT NULL,
	id_localidad_subida INTEGER NOT NULL,
	id_localidad_bajada INTEGER NOT NULL,
	
	PRIMARY KEY (id_pasajero_viaje),
	FOREIGN KEY (id_viaje) REFERENCES VIAJE (id_viaje),
	FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_usuario),
	FOREIGN KEY (id_comision_cobrada) REFERENCES COMISION_COBRADA (id_comision_cobrada),
	FOREIGN KEY (id_viaje, id_localidad_subida) REFERENCES LOCALIDAD_VIAJE (id_viaje, id_localidad),
	FOREIGN KEY (id_viaje, id_localidad_bajada) REFERENCES LOCALIDAD_VIAJE (id_viaje, id_localidad)
);

