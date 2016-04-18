var userValido = false;
var passValido = false;

$(document).ready(function(){
	if (getCookie("nombre_usuario")!="") {
		return window.location.replace("home.html");
	}
  ocultarMensajes();
  var inputUsuario = $("input[name=usuario]");
  var inputPass = $("input[name=pass]");
  inputUsuario.focus();
  inputUsuario.focusin(function(){
	quitarError("usuario");
  });
  inputPass.focusin(function(){
	quitarError("pass");
  });
  inputUsuario.focusout(validarUser);
  inputPass.focusout(validarPass);
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
function quitarError (campo){
	var jquery;
	switch(campo){
		case "usuario":
			jquery = $("#error_usuario");
			break;
		case "pass":
			jquery = $("#error_pass");
			break;
	}
	jquery.hide();
	if (jquery.parent().parent().hasClass("has-error")){
		jquery.parent().parent().removeClass("has-error");
	}
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
		if(!passValido) {
			//$("#error_pass").effect('shake', {distance:2},{ times:1 }, 500);
			$("#error_pass").show().parent().parent().addClass("has-error").fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
		}
		if(!userValido) {
			//$("#error_usuario").effect('shake', {distance:2},{ times:1 }, 500);
			$("#error_usuario").show().parent().parent().addClass("has-error").fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
		}
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
		  $("#myModal").find(".modal-body").html(jsonData.msg);
		  $("#myModal").modal();
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
