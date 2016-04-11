

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
  $("#mod_adm_us").hide();
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
					$("#mod_adm_us").show();
				}else if (nombrePermiso=="otro permiso"){
					//etc.
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