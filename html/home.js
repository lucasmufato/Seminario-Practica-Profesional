
$(document).ready(function(){
  console.log("entro a home.js");
  // escondo funcionalidades
  esconder_funcionalidades();
  //pregunto si esta logueado
  var nombreUsuario = getCookie("nombre_usuario");
  if (nombreUsuario == ""){
    window.location.replace("index.html");
  }else{
  	console.log("nombre usuario:", nombreUsuario);
	getRolesUsuario(nombreUsuario);
  }
})

function esconder_funcionalidades(){
  $("#adm_us").hide();
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
	console.log("en mostrar resultados, data:",jsonData);
	// no me anda la letra "t" ni la "igrega" , tampoco el bloq mashus, el borrar, tab ni shift   
	if (jsonData.result){
		var permiso=0;
		for (permiso in jsonData.result){
		var nombre_permiso = jsonData.result[permiso]["nombre_permiso"];
		  if (nombre_permiso == "administrar_usuarios"){
		    $("#adm_us").show();
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