
$(document).ready(function(){
  console.log("entro a home.js");
  //pregunto si esta logueado
  var nombreUsuario = getCookie("nombre_usuario");
  if (nombreUsuario != ""){
  	console.log("nombre usuario:", nombreUsuario);
	getRolesUsuario(nombreUsuario);
  }
})

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
	console.log("en mostrar resultados, data:",jsonData);
	//desde aca sigo mañana
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