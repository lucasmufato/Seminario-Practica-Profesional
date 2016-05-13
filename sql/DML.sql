
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (NULL, "super_usuario", "Super Usuario", "Este usuario tiene todos los privilegios", "A");
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (NULL, "cliente", "Cliente", "Este usuario es cliente", "A");


INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL, NULL, "administrador", "adminpass0", "admin@localhost", "Usuario administrador", "A","U");
-- UPDATE USUARIO SET id_usuario=0 WHERE nombre_usuario="administrador";

-- CREO UNOS USUARIOS PARA HACER PRUEBAS EN EL SISTEMA Y QUE ANDEN PARA TODOS
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,NULL,"prueba_conductor","conductor", "conductor@mail.com","usuario para hacer viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,NULL,"prueba_pasajero1","pasajero1", "pasajero1@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,NULL,"prueba_pasajero2","pasajero2", "pasajero2@mail.com","usuario para participar en viajes","A","C");

-- este metodo le da al user administrador el rol super_usuario
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (1, 1, CURRENT_DATE);

-- LES ASIGNO A LOS USUARIO Q CREO PARA LAS PRUEBAS EL ROL DE CLIENTE
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (2, 2, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (3, 2, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (4, 2, CURRENT_DATE);
-- LOS CREO COMO CLIENTES A ESTOS MISMOS USUARIOS
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) VALUES (2,0,0,"no tiene","no tiene");
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) VALUES (3,0,0,"no tiene","no tiene");
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) VALUES (4,0,0,"no tiene","no tiene");

INSERT INTO PERMISO (id_permiso, nombre_permiso, funcionalidad,descripcion,estado ) VALUES (null, "administrar_usuarios","administrar usuarios","permite ABM de usuarios","A");
INSERT INTO PERMISO_ROL (id_permiso, id_rol, fecha_modificacion) VALUES(1,1,CURRENT_DATE);

INSERT INTO LOCALIDAD_CLASIFICACION(codigo, nombre_clase) VALUES
	("A", "Ciudad/Estado/Región"),
	("H", "Flujo de agua/Lago"),
	("L", "Parque/Area"),
	("P", "Ciudad/Pueblo"),
	("R", "Calle/Camino/Ruta"),
	("S", "Espacio/Edificio/Granja"),
	("T", "Montaña/Cerro"),
	("U", "Bajo el agua"),
	("V", "Bosque/Arbustos");
