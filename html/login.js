var userValido = false;
var passValido = false;

$(document).ready(function(){
	if (getCookie()!="") {
		return window.location.replace("home.html");
	}

  ocultarMensajes();
  var inputUsuario = $("input[name=usuario]");
  var inputPass = $("input[name=pass]");
  inputUsuario.focus();
  inputUsuario.focusin(function(){
    $("#error_usuario").hide();
  });
    inputPass.focusin(function(){
    $("#error_pass").hide();
  });
  inputUsuario.focusout(validarUser);
  inputPass.focusout(validarPass);
  $("#ingresar").mouseover(function(){
	validarUser();
	validarPass();
  });
});

$(document).keypress(function(e) {
    if(e.which == 13) {//enter
		validarUser();
		validarPass();
    }
});

function ocultarMensajes(){
  $(".msg_error").hide();
}

var validarUser = function(){
  var ingresado = $("input[name=usuario]").val();
  userValido=ingresado.length>=6;
  if (!userValido){
    $("#error_usuario").show();
  }
};
var validarPass = function(){
  var ingresado = $("input[name=pass]").val();
  passValido=ingresado.length>=6;
  if (!passValido){
    $("#error_pass").show();
  }
};
function validarDatos(){
	if (userValido && passValido) {
		enviarForm();
	}else {
		if(!passValido) $("#error_pass").effect('shake', {distance:10},{ times:3 }, 600);
		if(!userValido) $("#error_usuario").effect('shake', {distance:10},{ times:3 }, 600);
	}
	return false;
}

function enviarForm(){
    var sendData = {
      accion: 'login',
      usuario: $("input[name=usuario]").val(),
      pass: $("input[name=pass]").val(),
    };
    $.ajax({
      url: '/login',
      method: 'POST',
      data: sendData,
      dataType: 'json',
      success: function (jsonData) {
        DEBUGresponse = jsonData;
        if (jsonData.result) {
          window.location.replace("home.html");
        } else {
          window.alert ("No existe usuario para la contraseña ingresada");
        }
      },
      error: function (er1, err2, err3) {
        document.body.innerHTML = er1.responseText;
        window.alert (err3);
      }
    });
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
