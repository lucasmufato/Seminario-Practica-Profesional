data={};
data.permisos=[];
data.usuario={};
data.roles=[];

$(document).ready(function(){
	esconderFuncionalidades();
	getPermisosUsuario();
});

function esconderFuncionalidades(){
  $("#panel-admin").hide();
  $("#panel-cliente").hide();
}

function getPermisosUsuario() {
	console.log("en get permisos usuario");
	var sendData = {
		action: 'get_permisos'
	};
	var callback = function (jsonData){
		if (jsonData.result){
			data.permisos = jsonData.permisos;
			data.usuario = jsonData.usuario;
			data.roles = jsonData.roles;
			cargarBotones();
			mostrarFunciones();
		}else if (jsonData.redirect != undefined){
			window.location = jsonData.redirect;
		}

	}
	send(sendData,callback);
}
function send(sendData,callback){
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
var cargarBotones = function(){
	// Si hacemos que xx.html sin parametros sea la pagina del usuario logueado esto ya no seria necesario
	//$("#link-mi-perfil").attr("href","perfil.html?usuario="+data.usuario.nombre_usuario);
	//$("#link-mis-viajes").attr("href","mis_viajes.html?usuario="+data.usuario.nombre_usuario);
	//$("#link-mis-vehiculos").attr("href","xxx.html?usuario="+data.usuario.nombre_usuario);
}
function mostrarFunciones(){
	console.log("Permisos que me traje: ",data.permisos);
		console.log("roles que me traje: ",data.roles);
	console.log("usuario que me traje: ",data.usuario);
	$("#dropdown-usuario").html(data.usuario.nombre_usuario+" ");
	if (data.roles){
		for (var i=0; i<data.roles.length;i++){
			var rol = data.roles[i].nombre_rol.toLowerCase();
			if (rol == "cliente"){
				  $("#panel-cliente").show();
				  makeListDropdown(rol);
			}
			if (rol == "super_usuario"){
				  $("#panel-admin").show();
				  $("#link-mi-perfil").show();
				  makeListDropdown(rol);
			}
		}
	}
	/*
	if (data.permisos){
		var permiso=0;
		for (permiso in data.permisos){
			var nombrePermiso=data.permisos[permiso]["nombre_permiso"];
			var estadoPermiso=data.permisos[permiso]["estado"];
			if (nombrePermiso && estadoPermiso=="A"){
				if (nombrePermiso == "administrar_usuarios"){
					$("#panel-admin").show();
				}
			}
		}
	}
	*/
}

var makeListDropdown = function(rol){
	var html = 	"<li><a href='perfil.html'>Mi perfil</a></li>"
	
	if (rol == "cliente"){
		html += "<li><a href='mis_viajes.html'>Mis viajes</a></li>"+
				"<li><a href='mis_vehiculos.html'>Mis vehículos</a></li>";
	} else if (rol == "super_usuario"){
		html += "<li><a href='abm.html'>Administrar usuarios</a></li>";
	}
	
	html += "<li role='separator' class='divider'></li>"+
			"<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>";
	$("#dropdown-menu").html(html);
}
