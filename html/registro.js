ui = {};

data={};
data.usuario=[];
data.persona=[];
data.cliente=[];

$(document).ready(function(){
  ui.setNewForm();
  $('#formUsuario input[name=nombre_usuario]').focusout(ui.validarNombreUsuario);
  $('#formUsuario input[name=password], #formUsuario input[name=repetirPassword]').focusout(ui.validarPass);
  $('#formUsuario input[name=email]').focusout(ui.validarMail);
  $('form input[required]').focusout(ui.validarCampoObligatorio);
  });

function labelDelInput(input){
	return label = $('label[for="'+input.attr('name')+'"]').text().split(" (*)")[0];
}
  
ui.validarCampoObligatorio = function(){
	var input = $(this);
	if (input.val().length==0){
		customAlert(input, labelDelInput(input)+": Completar campo obligatorio");
	}
}
  
ui.validarNombreUsuario = function(){
	var inputUsuario = $(this);
	var valor = inputUsuario.val();
	if (valor.length < 6){
		if (valor.length > 0){
			customAlert(inputUsuario, labelDelInput(inputUsuario)+": Mínimo 6 caracteres");
		}
	} else{
		usuarioExiste(inputUsuario.val(),function(existe){
			if (!existe){
				console.log("no existe usuario");
				customAlert(inputUsuario, labelDelInput(inputUsuario)+": Usuario existente");
			} else{
				console.log("existe usuario");
			}
		});
	}
}

function usuarioExiste(nombreUsuario,callback){
	var sendData = {
      accion: 'validar_usuario',
      usuario: nombreUsuario,
    };
	$.ajax({
      url: '/registro',
      method: 'POST',
      data: sendData,
      dataType: 'json',
      success: function (jsonData) {
        DEBUGresponse = jsonData;
        callback(jsonData.result);
      },
      error: function (er1, err2, err3) {
        document.body.innerHTML = er1.responseText;
        window.alert (err3);
      }
    });
}

ui.validarPass = function(){
	var inputPass = $(this);
	var label = labelDelInput(inputPass);
	if (inputPass.val().length > 0){
		if (inputPass.attr("name") == "password"){
			if (inputPass.val().length<6){
				customAlert(inputPass,label+": Mínimo 6 caracteres");
			}
		}else if (inputPass.attr("name") == "repetirPassword"){
			if (inputPass.val() != $('#formUsuario input[name=password]').val()){
				customAlert(inputPass, label+": no coinciden contraseñas");
			}
		}
	}
}

ui.validarMail = function(){
  var inputMail = $(this);
  var label = labelDelInput(inputMail);
  var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  if (inputMail.val().length>0 && !regex.test(inputMail.val())){
	customAlert(inputMail,label+": Mail no válido");
  }
}

ui.validar = function(form){
  //Parche fierisimo, para no pase al siguiente formulario habiendo
  //errores piso todos los inputs y 
  //pregunto si existe algun elemento en el panel de alarmas
	var ultimoElemento; // para sacar de foco el ultimo elemento 
	$("#form"+form+" input").each(function () {
		ultimoElemento = $(this).focus();
	});
	ultimoElemento.focusout();
	if ($(".panel-error").has("div").length == 0){
		console.log("no hay errores");
		if (ui.setNewForm(form)){ // si no hay mas formularios envio datos;
			//ui.sendForm();
		}
	}else{
		console.log("hay errores");
	}
}

/*	
ui.sendForm = function () {
	if (!ui.validarVaciosForm()) return false;
	
	var sendData = {};
	sendData.entity = 'persona';
	sendData.id_persona = $('#formPersona input[name=id]').val();
	sendData.action = 'new';
	sendData.apellidos = $('#formPersona input[name=apellidos]').val() || null;
	sendData.nombres= $('#formPersona input[name=nombres]').val() || null;
	sendData.tipo_doc= $('#formPersona select[name=tipo_doc]').val() || null;
	sendData.nro_doc= $('#formPersona input[name=nro_doc]').val() || null;
	sendData.fecha_nacimiento= $('#formPersona input[name=fecha_nacimiento]').val() || null;
	sendData.sexo= $('#formPersona select[name=sexo]').val() || null;
	sendData.domicilio= $('#formPersona input[name=domicilio]').val() || null;
	sendData.telefono= $('#formPersona input[name=telefono]').val() || null;

	//aux.sendForm(sendData, data.loadData);
}
*/
ui.setNewForm = function (actualForm) {
  //cambio de un form al siguiente, si es el ultimo envio datos.
  if (actualForm==undefined){
    ui.activateForm("Usuario");
  }else if (actualForm=="Usuario"){
    ui.activateForm("Persona");
  } else if (actualForm=="Persona"){
    ui.activateForm("Cliente");
  } else if (actualForm="Cliente"){
    return false;
  }
	return true;
};

ui.activateForm = function(form){
  var formSelector = '#'+'form'+form;
	ui.hideForms();
	$(formSelector).show();
	//$(formSelector + " input[required]").first().focus();
}

ui.hideForms = function () {
  $('#formCliente').hide();
  $('#formUsuario').hide();
  $('#formPersona').hide();
} 

function customAlert(input,msg){
   if (msg != undefined){
	ui.sendMsgError(msg, input);
   }
   input.parent().parent().addClass("has-error");
   input.fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
   input.focus(function(){
     input.unbind('focus');//para el IE
     input.parent().parent().removeClass("has-error"); 
	 ui.deleteMsgError(input)
   });
}

ui.sendMsgError = function(msg, input){
  var id = "error-"+input.attr("name");
  var html='<div id=\"'+id+'\" class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12 alert alert-danger\" role=\"alert\"><span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span><span class=\"sr-only\">Error:</span>'+msg+'</div>';
  input.closest(".panel-body").find(".panel-error").append(html).focus();
}

ui.deleteMsgError = function(input){
	var idGenerado = "#"+"error-"+input.attr("name");
	$(idGenerado).remove();
}