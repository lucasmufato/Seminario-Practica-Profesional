

$(document).ready(function(){
  console.log("entro a scriptLogin.js");
  //pregunto si esta logueado
  var nombreUsuario = getCookie("nombre_usuario");
  if (nombreUsuario != ""){
	getRolesUsuarios(nombreUsuario);
  }
})

function getRolesUsuario (nombreUsuario) {
	console.log("en getRolesUsuario, data:", nombreUsuario);
	var sendData = {
		entity: 'usuario',
		action: 'login',
		nombre_usuario: nombreUsuario,
	};
	$.ajax({
		url: '/users',
		action: 'login',
		method: 'POST',
		data: sendData,
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if (jsonData.result) {
				mostrarResultados(jsonData);
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

function mostrarResultados(jsonData){
	console.log("en mostrar resultados, data:",jsonData);
	//aca irian los if

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