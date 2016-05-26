permisosData={};
permisosData.permisosp=[];
permisosData.usuariop={};
permisosData.rolesp=[];

$(document).ready(function(){
	esconderFuncionalidades();
	getPermisosUsuario();
});

function esconderFuncionalidades(){
  $("#panel-admin").hide();
  $("#panel-cliente").hide();
}

function getPermisosUsuario() {
	var sendData = {
		action: 'get_permisos'
	};
	var callback = function (jsonData){
		if (jsonData.result){
			permisosData.permisosp = jsonData.permisos;
			permisosData.usuariop = jsonData.usuario;
			permisosData.rolesp = jsonData.roles;
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
	//$("#link-mi-perfil").attr("href","/perfil.html?usuario="+permisosData.usuariop.nombre_usuario);
	//$("#link-mis-viajes").attr("href","/mis_viajes.html?usuario="+permisosData.usuariop.nombre_usuario);
	//$("#link-mis-vehiculos").attr("href","/mis_vehiculos.html?usuario="+permisosData.usuariop.nombre_usuario);.hide();
}
function mostrarFunciones(){
	//console.log("Permisos que me traje: ",permisosData.permisosp);
	//console.log("roles que me traje: ",permisosData.rolesp);
	//console.log("usuario que me traje: ",permisosData.usuariop);
	$("#dropdown-usuario").html(permisosData.usuariop.nombre_usuario+" ");
	if (permisosData.rolesp){
		for (var i=0; i<permisosData.rolesp.length;i++){
			var rol = permisosData.rolesp[i].nombre_rol.toLowerCase();
			if (rol == "cliente"){
				  $("#panel-cliente").show();
				  $("#link-mi-perfil-admin").hide();
				  makeListDropdown(rol);
			}
			if (rol == "super_usuario"){
				  $("#panel-admin").show();
				  makeListDropdown(rol);
			}
		}
	}
	/*
	if (permisosData.permisosp){
		var permiso=0;
		for (permiso in permisosData.permisosp){
			var nombrePermiso=permisosData.permisosp[permiso]["nombre_permiso"];
			var estadoPermiso=permisosData.permisosp[permiso]["estado"];
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
	var html = 	"<li><a href='/perfil.html'>Mi perfil</a></li>"
	
	if (rol == "cliente"){
		html += "<li><a href='/mis_viajes.html'>Mis viajes</a></li>"+
				"<li><a href='/mis_vehiculos.html'>Mis vehículos</a></li>";
	} else if (rol == "super_usuario"){
		html += "<li><a href='/abm.html'>Administrar usuarios</a></li>";
	}
	
	html += "<li role='separator' class='divider'></li>"+
			"<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>";
	$("#dropdown-menu").html(html);
}
