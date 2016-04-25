data={};
data.permisos=[];

initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover');
	$('input, select, textarea').addClass('form-control');
	$('label').addClass('control-label');
	/*-----------*/

	getPermisosUsuario();
	$('.loadingScreen').fadeOut(); 
};

$(document).ready(function(){
console.log("ready");
	esconderFuncionalidades();
	initUI();
});

function esconderFuncionalidades(){
  $("#administracion_usuarios").hide();
  $("#gestion_viajes").hide();
  $("#gestion_vehiculos").hide();
  $("#gestion_puntos").hide();
}

function getPermisosUsuario() {
	console.log("en get permisos usuario");
	var sendData = {
		action: 'get_permisos'
	};
	var callback = function (jsonData){
		if (jsonData.result){
			data.permisos = jsonData.permisos;
			mostrarFunciones();
		}else if (jsonData.redirect != undefined){
			window.location = jsonData.redirect;
		}

	}
	sendAjax(sendData,callback);
}

function mostrarFunciones(){
	console.log("Permisos que me traje: ",data.permisos);
	if (data.permisos){
		var permiso=0;
		for (permiso in data.permisos){
			var nombrePermiso=data.permisos[permiso]["nombre_permiso"];
			var estadoPermiso=data.permisos[permiso]["estado"];
			if (nombrePermiso && estadoPermiso=="A"){
				if (nombrePermiso == "administrar_usuarios"){
					$("#administracion_usuarios").show();
				}else if (nombrePermiso=="gestion_viajes"){
					$("#gestion_viajes").show();
				}else if (nombrePermiso=="gestion_vehiculos"){
					$("#gestion_vehiculos").show();
				}else if (nombrePermiso=="gestion_puntos"){
					$("#gestion_puntos").show();
				}
			}
		}
	}
}

function getCookie(nombreCookie) {
    var nombre = nombreCookie + "=";
    var propiedadesCookies = document.cookie.split(';');
    for(var i=0; i<propiedadesCookies.length; i++) {
        var c = propiedadesCookies[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(nombre) == 0) return c.substring(nombre.length,c.length);
    }
    return "";
}

var sendAjax = function(sendData,callback){
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
