ui = {};

data={};
data.usuario=[];
data.persona=[];
data.cliente=[];

$(document).ready(function(){
  ui.setNewForm();
});

ui.validarUsuario = function(){
	var nombreUsuario =  $('#formUsuario input[name=nombre_usuario]');
  var pass1 = $('#formUsuario input[name=password]');
  var pass2 = $('#formUsuario input[name=repetirPassword]');
  var mail = $('#formUsuario input[name=email]');

  
	return ui.validarNombreUsuario(nombreUsuario) & ui.validarPass(pass1,pass2) & ui.validarMail(mail);
  };


ui.validarNombreUsuario = function(nombreUsuario){
  console.log(nombreUsuario.val());
	if (nombreUsuario.val().length<6){
		customAlert(nombreUsuario);
    ui.sendMsgError("Usuario: Mínimo 6 caracteres","Usuario");
		return false;
	}
	return true;
}

ui.validarPass = function(pass1,pass2){
	if (pass1.val().length<6){
		customAlert(pass1);
    ui.sendMsgError("Contraseña: Mínimo 6 caracteres","Usuario");
		return false;
	}else if (pass1.val() != pass2.val()){
	
    customAlert(pass1);
    customAlert(pass2);
    ui.sendMsgError("Contraseñas no coinciden entre sí","Usuario");
		return false;
  }
	return true;
}

ui.validarMail = function(mail){
  var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  if (!regex.test(mail.val())){
		customAlert(mail);
    ui.sendMsgError("Mail no válido","Usuario");
		return false;
	}
	return true;
}

ui.validar = function(form){
  //Limpio mensajes de error
  ui.sendMsgError("",form);
  if (ui.validarVaciosForm(form)){
    if (form=="Usuario"){
      if (!ui.validarUsuario()) return false;
    }
    ui.setNewForm(form);
    return true;
  }
  return false;
}

ui.validarVaciosForm = function(form){
	var bandera = true;
	var campos = [];
	
	switch (form) {
		case 'Persona':
			campos.push($('#formPersona input[name=apellidos]'));
			campos.push($('#formPersona input[name=nombres]'));
			campos.push($('#formPersona select[name=tipo_doc]'));
			campos.push($('#formPersona input[name=nro_doc]'));
			campos.push($('#formPersona input[name=fecha_nacimiento]'));
			campos.push($('#formPersona select[name=sexo]'));
			campos.push($('#formPersona input[name=domicilio]'));
			campos.push($('#formPersona input[name=foto]'));
			campos.push($('#formPersona input[name=telefono]'));
			campos.push($('#formPersona textarea[name=descripcion]'));
			campos.push($('#formPersona select[name=estado]'));
			campos.push($('#formPersona input[name=foto_registro]'));
			break;
		case 'Usuario':
			campos.push($('#formUsuario input[name=nombre_usuario]'));
			campos.push($('#formUsuario input[name=password]'));
			campos.push($('#formUsuario input[name=repetirPassword]'));
			campos.push($('#formUsuario input[name=email]'));
			campos.push($('#formUsuario textarea[name=descripcion]'));
			campos.push($('#formUsuario select[name=estado]'));
			break;
	}
	
	for(var i=0; i<campos.length; i++){
		if (campos[i].attr("required")){
			if (campos[i].val() == ""){
				customAlert(campos[i]);
				bandera=false;
			}
		}
	}
  
  if (!bandera){
    ui.sendMsgError("Completar Campos Vacios",form);
  } 
	return bandera;
}
/*
ui.sendPersonaForm = function () {
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
    ui.activateTab("Usuario");
  }else if (actualForm=="Usuario"){
    ui.activateTab("Persona");
  } else if (actualForm=="Persona"){
    ui.activateTab("Cliente");
  } else if (actualForm="Cliente"){
    //sendData();
  }

};

ui.activateTab = function(form){
  var formSelector = '#'+'form'+form;
	ui.hideForms();
	$(formSelector).show();
}

ui.hideForms = function () {
	//$('.formSection').hide();
  $('#formCliente').hide();
  $('#formUsuario').hide();
  $('#formPersona').hide();
} 

ui.sendMsgError = function(msg,form){
  var formSelector = '#'+'form'+form;
  if(msg==""){
    $(formSelector +" .panel-error").html("");
  }else{
    var html='<div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12 alert alert-danger\" role=\"alert\"><span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span><span class=\"sr-only\">Error:</span>'+msg+'</div>';
    $(formSelector +" .panel-error").append(html).focus();
  }
}


function customAlert(input){
   input.parent().parent().addClass("has-error");
 input.fadeIn(100).fadeOut(100).fadeIn(100).fadeOut(100).fadeIn(100);
   input.focus(function(){
     input.unbind('focus');//para el IE
     input.parent().parent().removeClass("has-error"); 
   });
}