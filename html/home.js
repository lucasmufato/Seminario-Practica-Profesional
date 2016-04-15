

$(document).ready(function(){
  console.log("entro a home.js");
  // escondo funcionalidades
  esconderFuncionalidades();
  //pregunto si esta logueado
  var nombreUsuario = getCookie("nombre_usuario");
  if (nombreUsuario == ""){
	//no esta logueado, vuelve al index
    window.location.replace("index.html");
  }else{
  	console.log("nombre usuario:", nombreUsuario);
	getRolesUsuario(nombreUsuario);
  }
})

function esconderFuncionalidades(){
    $("#administracion_usuarios").hide();
  $("#gestion_viajes").hide();
  $("#gestion_vehiculos").hide();
  $("#gestion_puntos").hide();
}

function getRolesUsuario (nombreUsuario) {
	var sendData = {
		entity: 'usuario',
		action: 'login',
		nombre_usuario: nombreUsuario,
	};
	$.ajax({
		url: '/users',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;// esta linea no se que hace, ante la duda la dejo.
			if (jsonData.result) {
				mostrarFunciones(jsonData);
			} else {
				window.alert ("Ocurrio un error");
			}
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

function mostrarFunciones(jsonData){
	console.log("Permisos que me traje: ",jsonData.result);
	if (jsonData.result){
		var permiso=0;
		for (permiso in jsonData.result){
			var nombrePermiso=jsonData.result[permiso]["nombre_permiso"];
			var estadoPermiso=jsonData.result[permiso]["estado"];
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