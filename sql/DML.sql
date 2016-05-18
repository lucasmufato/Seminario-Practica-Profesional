
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (NULL, "super_usuario", "Super Usuario", "Este usuario tiene todos los privilegios", "A");
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (NULL, "cliente", "Cliente", "Este usuario es cliente", "A");

INSERT INTO PERSONA (id_persona, nombres, apellidos, tipo_doc, nro_doc, fecha_nacimiento, sexo, domicilio, telefono, descripcion, estado) VALUES (NULL, "raul", "admin", "1", "33111311", "1992-09-25", "O", "Lavalle, 123", "011-15-422123", NULL, "A");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL, 1, "administrador", "adminpass0", "admin@localhost", "Usuario administrador", "A","U");

-- UPDATE USUARIO SET id_usuario=0 WHERE nombre_usuario="administrador";

-- CREO UNOS USUARIOS PARA HACER PRUEBAS EN EL SISTEMA Y QUE ANDEN PARA TODOS
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,1,"prueba_conductor","conductor", "conductor@mail.com","usuario para hacer viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,1,"prueba_pasajero1","pasajero1", "pasajero1@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,1,"prueba_pasajero2","pasajero2", "pasajero2@mail.com","usuario para participar en viajes","A","C");

-- este metodo le da al user administrador el rol super_usuario
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (1, 1, CURRENT_DATE);

-- LES ASIGNO A LOS USUARIO Q CREO PARA LAS PRUEBAS EL ROL DE CLIENTE
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (2, 2, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (3, 2, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (4, 2, CURRENT_DATE);
-- LOS CREO COMO CLIENTES A ESTOS MISMOS USUARIOS
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) VALUES (2,0,0,NULL,NULL);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) VALUES (3,0,0,NULL,NULL);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) VALUES (4,0,0,NULL,NULL);

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
	
INSERT INTO ESTADO_VIAJE(id_estado_viaje, nombre_estado) VALUES
	("2", "Finalizado"),
	("0", "No iniciado"),
	("1", "Iniciado"),
	("3", "Cancelado");
	
-- Para crear viajes:
INSERT INTO PERSONA (id_persona, nombres, apellidos, tipo_doc, nro_doc, fecha_nacimiento, sexo, domicilio, telefono, descripcion, estado) 
VALUES (NULL, "Juan", "Cardona", "1", "36072559", "1992-09-23", "M", "25 de mayo, 1168", "2323-15-609065", "Hayq ue ponerle personas a los usuarios o se rompe el pobrecito perfil, sepan entender", "A");
-- USUARIO Nro 5:
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) 
VALUES (NULL,2,"juan23","juan23", "juan23@hotmail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (5, 2, CURRENT_DATE);

INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) 
VALUES (5,0,3,null,null);
INSERT INTO VEHICULO (id_vehiculo, anio, marca, modelo, patente, verificado, estado, fecha_verificacion, color, cantidad_asientos, aire_acondicionado, seguro, foto) 
VALUES (NULL, '1992', 'Ford', 'Focus', 'abc123', 'N', 'A', NULL, '000000', '5', 'N', 'N', NULL);
INSERT INTO VEHICULO (id_vehiculo, anio, marca, modelo, patente, verificado, estado, fecha_verificacion, color, cantidad_asientos, aire_acondicionado, seguro, foto)  
VALUES (NULL, '1980', 'Ford', 'Falcon', 'abd124', 'S', 'A', '2016-05-18', 'FF1122', '4', 'S', 'S', NULL);
INSERT INTO MANEJA (id_cliente, id_vehiculo, fecha_inicio, fecha_fin) 
VALUES ('5', '1', '2016-05-16', NULL), ('5', '2', '2016-05-01', NULL);
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio) 
VALUES (NULL, 'otro viaje', '1', '1', '2016-05-18 00:00:00', '2016-02-18 00:00:00', NULL, NULL, '1', '5', '2016-05-16', NULL, '50');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio) 
VALUES (NULL, 'viaje', '2', '2', '2016-04-12 00:00:00', '2016-03-10 00:00:00','2016-04-13 00:00:00', NULL, '2', '5', '2016-05-01', NULL, '25');
-- USUARIO Nro 6:
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) 
VALUES (NULL,2,"usuario","usuario", "usuario@usuario.us",NULL,"A","C");
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto) 
VALUES (6,0,5,null,null);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (6, 2, CURRENT_DATE);

INSERT INTO VEHICULO (id_vehiculo, anio, marca, modelo, patente, verificado, estado, fecha_verificacion, color, cantidad_asientos, aire_acondicionado, seguro, foto) 
VALUES (NULL, '1972', 'Chevrolet', 'Chevrolet', 'aaa111', 'N', 'A', NULL, 'FFFFFF', '2', 'N', 'N', NULL);
INSERT INTO VEHICULO (id_vehiculo, anio, marca, modelo, patente, verificado, estado, fecha_verificacion, color, cantidad_asientos, aire_acondicionado, seguro, foto)  
VALUES (NULL, '2010', 'Volkwagen', 'Gol', 'xxx111', 'N', 'A', '2016-05-10', 'FF1122', '4', 'S', 'S', NULL);
INSERT INTO MANEJA (id_cliente, id_vehiculo, fecha_inicio, fecha_fin) 
VALUES ('6', '3', '2015-12-16', NULL), ('6', '4', '2016-05-01', NULL);
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio) 
VALUES (NULL, 'Mi primer viaje', '1', '0', '2016-05-15 00:00:00', '2016-05-10 00:00:00', NULL, NULL, '3', '6', '2015-12-16', NULL, '250');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio) 
VALUES (NULL, 'viaje re loco', '2', '3', '2016-04-18 00:00:00', '2016-04-10 00:00:00',NULL, '2016-04-13 00:00:00', '4', '6', '2016-05-01', NULL, '125');
-- localidades de viajes
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente) 
VALUES ('1', '3430988', '0', '10'), ('1', '3433781', '0', '10'), ('1', '3429980', '0', '0');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente) 
VALUES ('2', '3430988', '0', '10'), ('2', '3433781', '0', '0');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente) 
VALUES ('3', '3430987', '0', '10'), ('3', '3433780', '0', '10'), ('3', '3429979', '0', '0');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente) 
VALUES ('4', '3430977', '0', '5'), ('4', '3430978', '0', '5'), ('4', '3430979', '0', '5');

