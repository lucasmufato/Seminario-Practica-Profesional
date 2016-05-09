
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (NULL, "super_usuario", "Super Usuario", "Este usuario tiene todos los privilegios", "A");
-- UPDATE ROL SET id_rol=0 WHERE nombre_rol="super_usuario";

INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL, NULL, "administrador", "adminpass0", "admin@localhost", "Usuario administrador", "A","U");
-- UPDATE USUARIO SET id_usuario=0 WHERE nombre_usuario="administrador";

-- INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (0, 0, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (1, 1, CURRENT_DATE);

INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (NULL, "cliente", "Cliente", "Este usuario es cliente", "A");

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
