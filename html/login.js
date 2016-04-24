var userValido = false;
var passValido = false;


initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover');
	$('input, select, textarea').addClass('form-control');
	$('label').addClass('control-label');
	$('.saveButton').addClass('btn btn-success glyphicon glyphicon-ok');
	/*-----------*/
	

};

$(document).ready(function(){
	if (getCookie("nombre_usuario")!="") {
		return window.location.replace("home.html");
	}else{
		ocultarMensajes();
		$('.loadingScreen').fadeOut();
	}
  var inputUsuario = $("input[name=usuario]");
  var inputPass = $("input[name=pass]");
  inputUsuario.val(getUrlVars()["usuario"]);
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
	if (jquery.parent().parent().parent().hasClass("has-error")){
		jquery.parent().parent().parent().removeClass("has-error");
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
			$("#error_pass").show().parent().parent().parent().addClass("has-error").fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
		}
		if(!userValido) {
			//$("#error_usuario").effect('shake', {distance:2},{ times:1 }, 500);
			$("#error_usuario").show().parent().parent().parent().addClass("has-error").fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
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

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}