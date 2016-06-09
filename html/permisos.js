permisosData={};
permisosData.permisosp=[];
permisosData.usuariop={};
permisosData.rolesp=[];
/*
$( document ).ready(function(){
	console.log("cargando permisos desde document ready");
	//permisosData.esconderFuncionalidadesPermisos();
	//permisosData.getPermisosUsuario();
 });
*/
permisosData.iniciarScriptPermisos = function(){
	permisosData.getPermisosUsuario();
}

permisosData.getPermisosUsuario = function() {
	var sendData = {
		action: 'get_permisos'
	};
	var callback = function (jsonData){
		if (jsonData.result){
			permisosData.permisosp = jsonData.permisos;
			permisosData.usuariop = jsonData.usuario;
			permisosData.rolesp = jsonData.roles;
			permisosData.mostrarFunciones();
		}else if (jsonData.redirect != undefined){
			window.location = jsonData.redirect;
		}
	}
	permisosData.send(sendData,callback);
}

permisosData.send = function(sendData,callback){
	$.ajax({
		url: '/users',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			callback(jsonData);
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

permisosData.mostrarFunciones = function(){
	//console.log("Permisos que me traje: ",permisosData.permisosp);
	//console.log("roles que me traje: ",permisosData.rolesp);
	//console.log("usuario que me traje: ",permisosData.usuariop);
	permisosData.cargarSidebarPermisos();
	$("#dropdown-usuario").html(permisosData.usuariop.nombre_usuario+" ");
	if (permisosData.rolesp){
		for (var i=0; i<permisosData.rolesp.length;i++){
			var rol = permisosData.rolesp[i].nombre_rol.toLowerCase();
			if (rol == "cliente"){
				  permisosData.makeListDropdown(rol);
			}
			if (rol == "super_usuario"){
				  permisosData.makeListDropdown(rol);
			}
		}
	}

}

permisosData.cargarSidebarPermisos = function(){
	$.getScript( "/js/mustache.js", function() {
		$.ajax({
			url: '/navegacion.html',
			type: 'GET',
			success: function(data) {
				var dom = $(data);
				var template;
				dom.filter('script').each(function(){
					if (this.id == "botonera-sidebar-template"){
						template = this.innerHTML;
					}
				});
				var permisosFlags = {};
				console.log(permisosData.permisosp);
				if (permisosData.permisosp){
					var permiso=0;
					for (permiso in permisosData.permisosp){
						var nombrePermiso=permisosData.permisosp[permiso]["nombre_permiso"];
						var estadoPermiso=permisosData.permisosp[permiso]["estado"];
						if (nombrePermiso && estadoPermiso=="A"){
							permisosFlags[nombrePermiso] = true;
						}
					}
				}
				var html = Mustache.render(template,permisosFlags);
				$("#botonera-sidebar").html(html);
			}
		});
	});
}

permisosData.makeListDropdown = function(rol){
	var html = 	"<li><a href='/perfil.html'>Mi perfil</a></li>"

	if (rol == "cliente"){
		html += "<li><a href='/mis_viajes.html'>Mis viajes</a></li>"+
				"<li><a href='/mis_vehiculos.html'>Mis vehículos</a></li>";
	} else if (rol == "super_usuario"){
		html += "<li><a href='/abm.html'>Administrar usuarios</a></li>"
			+"<li><a href='/reportes/viajes.html'>Reporte de viajes</a></li>"
			+"<li><a href='/reportes/comisiones.html'>Reporte de comisiones</a></li>";
	}

	html += "<li role='separator' class='divider'></li>"+
			"<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>";
	$("#dropdown-menu").html(html);
}
