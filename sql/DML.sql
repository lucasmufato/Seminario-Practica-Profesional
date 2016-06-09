SET NAMES utf8;
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (1, "super_usuario", "Super Usuario", "Este usuario tiene todos los privilegios", "A");
INSERT INTO ROL (id_rol, nombre_rol, nombre_amigable, descripcion, estado) VALUES (2, "cliente", "Cliente", "Este usuario es cliente", "A");

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
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo) VALUES (2,default,default,NULL,NULL,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo) VALUES (3,default,default,NULL,NULL,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo) VALUES (4,default,default,NULL,NULL,100000);


-- agrego permisos de funcional
INSERT INTO PERMISO (id_permiso, nombre_permiso, funcionalidad,descripcion,estado ) VALUES
	(1, "administrar_usuarios","Administrar usuarios","Permite Alta/Baja/Modificacion de usuarios","A"),
	(2, "generar_reportes", "Generar reportes", "Permite generar reportes", "A"),

	(3, "loguearse","loguearse","permite ingresar al sistema","A"),
	(4, "acceder_perfil", "Acceder a perfil de usuario", "Permite acceder a perfil de usuario", "A"),
	(5, "modificar_perfil", "Modificar datos de perfil", "Permite modificar perfil de usuario", "A"),
	(6, "desactivar_cuenta", "Desactivar cuenta de usuario", "Permite desactivar la cuenta de un usuario", "A"),

	(7, "crear_viajes","Crear viajes","Permite crear viajes","A"),
	(8, "buscar_viajes","Buscar viajes","Permite buscar viajes","A"),
	(9, "eliminar_viajes", "Eliminar viajes", "Permite eliminar un viaje", "A"),
	(10, "acceder_mis_viajes", "Acceder a 'Mis viajes'", "Permite acceder a listado 'Mis viajes'", "A"),
	(11, "ver_detalle_viajes", "Ver detalle de viajes", "Permite ver detalles de un viaje", "A"),
	(12, "participar_viajes","Participar viajes","Permite participar en viajes","A"),
	(13, "cancelar_viajes","Cancelar viaje","Permite cancelar un viaje","A"),

	(14, "calificar","Calificar","Permite calificar usuarios","A"),

	(15, "crear_vehiculos","Crear vehiculos","Permite crear vehiculos","A"),
	(16, "ver_vehiculos", "Ver vehiculos", "Permite ver informacion de un vehiculo", "A"),
	(17, "modificar_vehiculos", "Modificar vehiculos", "Permite modificar datos de un vehiculo", "A"),
	(18, "asignar_conductores","Asignar conductores","Permite asignar conductores a un vehiculo","A"),
	(19, "desasignar_conductores","Desasignar conductores","Permite desasignar conductores a un vehiculo","A"),

	(20, "cargar_saldo", "Cargar saldo", "Permite cargar saldo", "A"),
	(21, "acceder_mis_vehiculos", "Acceder a 'Mis vehiculos'", "Permite acceder a listado 'Mis vehiculos'", "A");

INSERT INTO PERMISO_ROL (id_permiso, id_rol, fecha_modificacion) VALUES
	(1,1,CURRENT_DATE),
	(2,1,CURRENT_DATE),
	(3,2,CURRENT_DATE),
	(4,2,CURRENT_DATE),
	(4,1,CURRENT_DATE),
	(5,2,CURRENT_DATE),
	(6,2,CURRENT_DATE),
	(7,2,CURRENT_DATE),
	(8,2,CURRENT_DATE),
	(9,2,CURRENT_DATE),
	(10,2,CURRENT_DATE),
	(11,2,CURRENT_DATE),
	(12,2,CURRENT_DATE),
	(13,2,CURRENT_DATE),
	(14,2,CURRENT_DATE),
	(15,2,CURRENT_DATE),
	(16,2,CURRENT_DATE),
	(17,2,CURRENT_DATE),
	(18,2,CURRENT_DATE),
	(19,2,CURRENT_DATE),
	(20,2,CURRENT_DATE),
	(21,2,CURRENT_DATE);

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
	("0", "Iniciado"),
	("1", "No iniciado"),
	("2", "Finalizado"),
	("3", "Cancelado");
	
INSERT INTO ESTADO_COMISION(id_estado_comision, nombre_estado) VALUES
	("0", "Pendiente"),
	("1", "Pagado"),
	("2", "Vencido"),
	("3", "Informativa"),
	("4", "Desestimada");

-- Para crear viajes:
INSERT INTO PERSONA (id_persona, nombres, apellidos, tipo_doc, nro_doc, fecha_nacimiento, sexo, domicilio, telefono, descripcion, estado)
VALUES (NULL, "Juan", "Cardona", "1", "36072559", "1992-09-23", "M", "25 de mayo, 1168", "2323-15-609065", "Hayq ue ponerle personas a los usuarios o se rompe el pobrecito perfil, sepan entender", "A");
-- USUARIO Nro 5:
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo)
VALUES (NULL,2,"juan23","juan23", "juan23@hotmail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (5, 2, CURRENT_DATE);

INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo)
VALUES (5,default,default,null,null,100000);
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
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo)
VALUES (6,default,default,null,null,100000);
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

INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('1', '3430988', '0', '10','1'), ('1', '3433781', '0', '10','2'), ('1', '3429980', '0', '0','3');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('2', '3430988', '0', '10','1'), ('2', '3433781', '0', '0','2');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('3', '3430987', '0', '10','1'), ('3', '3433780', '0', '10','2'), ('3', '3429979', '0', '0','3');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('4', '3430977', '0', '5','1'), ('4', '3430978', '0', '5','2'), ('4', '3430979', '0', '0','3');

INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,1,"prueba_pasajero7","pasajero7", "pasajero7@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,1,"prueba_pasajero8","pasajero8", "pasajero8@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (NULL,1,"prueba_pasajero9","pasajero9", "pasajero9@mail.com","usuario para participar en viajes","A","C");

INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (7, 2, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (8, 2, CURRENT_DATE);
INSERT INTO USUARIO_ROL (id_usuario, id_rol, fecha_modificacion) VALUES (9, 2, CURRENT_DATE);

INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo) VALUES (7,default,default,NULL,NULL,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo) VALUES (8,default,default,NULL,NULL,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo) VALUES (9,default,default,NULL,NULL,100000);

-- fede
INSERT INTO TIPO_SANCION (id_tipo_sancion,descripcion,dias_sancion) VALUES (1,"Descuento de Puntos por Cancelación de viaje con Pasajeros",0);
INSERT INTO TIPO_SANCION (id_tipo_sancion,descripcion,dias_sancion) VALUES (2,"Descuento de Puntos por Cancelación tardía de participación en viaje",0);
INSERT INTO TIPO_SANCION (id_tipo_sancion,descripcion,dias_sancion) VALUES (3,"Sanción por Cancelación de viaje con Pasajeros",0);
INSERT INTO TIPO_SANCION (id_tipo_sancion,descripcion,dias_sancion) VALUES (4,"Sanción por Cancelación tardía de participación en viaje",0);
-- completarlas

INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(1,0,20,4,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(2,20,50,6,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(3,50,100,8,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(4,100,250,15,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(5,250,500,25,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(6,500,1000,40,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(7,1000,2000,80,CURRENT_DATE,NULL);
INSERT INTO comision (ID_COMISION,LIMITE_INFERIOR,LIMITE_SUPERIOR,PRECIO,FECHA_INICIO,FECHA_FIN) VALUES(8,2000,10000,150,CURRENT_DATE,NULL);

INSERT INTO TIPO_MOV_SALDO(id_tipo_mov_saldo,descripcion) VALUES (1,"Cobro de comisión por viaje");
INSERT INTO TIPO_MOV_SALDO(id_tipo_mov_saldo,descripcion) VALUES (2,"Acreditación de saldo por pago");

INSERT INTO TIPO_MOV_PUNTOS(id_tipo_mov_puntos,descripcion) VALUES (1,"Puntos como consecuencia de calificación");
INSERT INTO TIPO_MOV_PUNTOS(id_tipo_mov_puntos,descripcion) VALUES (2,"Deducción de puntos por canje de beneficio");
INSERT INTO TIPO_MOV_PUNTOS(id_tipo_mov_puntos,descripcion) VALUES (3,"Sanción");



-- dml para reportes de juan
/*
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (5, 'pruebaReporte1', '3', '0', '2016-06-07 23:00:00', '2016-06-07 22:00:00', NULL, NULL, '1', '5', '2016-05-16', NULL, '50');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (6, 'pruebaReporte2', '6', '0', '2016-06-07 23:10:00', '2016-06-07 22:00:00', NULL, NULL, '3', '6', '2015-12-16', NULL, '40');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (7, 'pruebaReporte3', '1', '0', '2016-06-07 23:02:00', '2016-06-07 22:02:00', NULL, NULL, '2', '5', '2016-05-01', NULL, '40000');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (8, 'pruebaReporte4', '3', '0', '2016-06-07 23:02:00', '2016-06-07 22:02:00', NULL, NULL, '4', '6', '2016-05-01', NULL, '5');

INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (10,1,"prueba_parareporte1","parareporte1", "reporte1@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (11,1,"prueba_parareporte2","parareporte2", "reporte2@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (12,1,"prueba_parareporte3","parareporte3", "reporte3@mail.com","usuario para participar en viajes","A","C");
INSERT INTO USUARIO (id_usuario, id_persona, nombre_usuario, password, email, descripcion, estado,tipo) VALUES (13,1,"prueba_parareporte4","parareporte3", "reporte4@mail.com","usuario para participar en viajes","A","C");

INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo)
VALUES (10,default,default,null,null,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo)
VALUES (11,default,default,null,null,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo)
VALUES (12,default,default,null,null,100000);
INSERT INTO CLIENTE (id_usuario,puntos,reputacion, foto_registro,foto,saldo)
VALUES (13,default,default,null,null,100000);

INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('5', '3430988', '0', '10','1'), ('5', '3433781', '2', '10','2'), ('5', '3429980', '0', '0','3');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('6', '3430988', '0', '10','1'), ('6', '3433781', '2', '0','2');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('7', '3430987', '0', '10','1'), ('7', '3433780', '1', '10','2'), ('7', '3429979', '0', '0','3');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('8', '3430977', '0', '5','1'), ('8', '3430978', '1', '5','2'), ('8', '3430979', '0', '0','3');


INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(1,4,'0',1,null,1, '2016-06-07 00:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(2,8,'1',3,null,2,'2016-06-08 05:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(3,6,'1',2,null,3,'2016-06-06 10:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(4,4,'1',1,null,4,'2016-06-07 13:55:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(5,15,'4',4,null,5,'2016-06-07 17:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(6,25,'1',5,null,6, '2016-06-07 22:15:20');

INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('1','5','11', '10','0',null,'1','3430988', '3429980',2);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('2','5','12', '50','0',null,'2','3430988','3433781',1);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('3','6','13', '20','0',null,'3','3430988','3433781',3);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('4','6','10', '13','0',null,'4','3430988','3433781',1);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('5','7','11', '150','1',null,'5','3430987','3433780',2);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('6','8','13', '250','0',null,'6','3430977','3430979',1);

-- segunda parte dml juan

INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('9', '0', '20', '5', '2016-06-08');
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='1';
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('10', '20', '50', '7', '2016-06-08');
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='2';
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('11', '50', '100', '9', '2016-06-08');
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='3';
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('12', '100', '250', '16', '2016-06-08');
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('13', '250', '500', '26', '2016-06-08');
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('14', '500', '1000', '41', '2016-06-08');
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('15', '1000', '2000', '81', '2016-06-08');
INSERT INTO `seminario`.`comision` (`id_comision`, `limite_inferior`, `limite_superior`, `precio`, `fecha_inicio`) VALUES ('16', '2000', '10000', '151', '2016-06-08');
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='4';
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='5';
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='6';
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='8';
UPDATE `seminario`.`comision` SET `fecha_fin`='2016-06-08' WHERE `id_comision`='7';

INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (9, 'pruebaReporte1', '3', '0', '2016-06-07 23:00:00', '2016-12-07 22:00:00', NULL, NULL, '1', '5', '2016-05-16', NULL, '50');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (10, 'pruebaReporte2', '6', '0', '2016-06-07 23:10:00', '2016-12-07 22:00:00', NULL, NULL, '3', '6', '2015-12-16', NULL, '40');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (11, 'pruebaReporte3', '1', '0', '2016-06-07 23:02:00', '2016-12-07 22:02:00', NULL, NULL, '2', '5', '2016-05-01', NULL, '40000');
INSERT INTO VIAJE (id_viaje, nombre_amigable, asientos_disponibles, estado, fecha_inicio, fecha_alta, fecha_finalizacion, fecha_cancelacion, id_vehiculo, id_cliente, fecha_inicio_maneja, viaje_complementario, precio)
VALUES (12, 'pruebaReporte4', '3', '0', '2016-06-07 23:02:00', '2016-12-07 22:02:00', NULL, NULL, '4', '6', '2016-05-01', NULL, '5');

INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('9', '3430988', '0', '10','1'), ('9', '3433781', '0', '10','2'), ('9', '3429980', '0', '0','3');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('10', '3430988', '0', '10','1'), ('10', '3433781', '0', '0','2');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('11', '3430987', '0', '10','1'), ('11', '3433780', '0', '10','2'), ('11', '3429979', '0', '0','3');
INSERT INTO LOCALIDAD_VIAJE (id_viaje, id_localidad, cantidad_pasajeroS, kms_a_localidad_siguiente,ordinal)
VALUES ('12', '3430977', '0', '5','1'), ('12', '3430978', '0', '5','2'), ('12', '3430979', '0', '0','3');


INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(7,5,'0',9,null,7, '2016-12-07 00:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(8,9,'1',11,null,8,'2016-12-08 05:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(9,7,'1',10,null,9,'2016-12-06 10:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(10,5,'1',11,null,10,'2016-12-07 13:55:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(11,16,'4',12,null,11,'2016-12-07 17:05:26');
INSERT INTO comision_cobrada (id_comision_cobrada,monto,estado,id_comision,id_movimiento_saldo,id_pasajero_viaje,fecha)
VALUES(12,26,'1',13,null,12, '2016-12-07 22:15:20');

INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('7','9','11', '10','0',null,'7','3430988', '3429980',2);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('8','9','12', '50','0',null,'8','3430988','3433781',1);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('9','10','13', '20','0',null,'9','3430988','3433781',3);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('10','10','10', '13','0',null,'10','3430988','3433781',1);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('11','11','11', '150','1',null,'11','3430987','3433780',2);
INSERT INTO pasajero_viaje (id_pasajero_viaje,id_viaje,id_cliente, kilometros,estado,id_calificacion, id_comision_cobrada,id_localidad_subida,id_localidad_bajada,nro_asientos)
VALUES('12','12','13', '250','0',null,'12','3430977','3430979',1);
*/