data={};
data.permisos=[];

$(document).ready(function(){
	esconderFuncionalidades();
	getPermisosUsuario();
});

function esconderFuncionalidades(){
  $("#panel-admin").hide();
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
function mostrarFunciones(){
	console.log("Permisos que me traje: ",data.permisos);
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
}
/*
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
*/
