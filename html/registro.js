ui = {}; // use esto para nombrar funciones sin hilación alguna, recordar sacarlo

loadData = function() {
	$.ajax({
		url: '/registro',
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if(jsonData.result){
				$('.loadingScreen').fadeOut();
			} else if (jsonData.redirect != undefined) {
				window.location = jsonData.redirect;
			}
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover');
	$('input, select, textarea').addClass('form-control');
	$('label').addClass('control-label');
	/*-----------*/
	
	$("#img_usuario, #img_registro").hide();
	
	loadData();
};

$(document).ready(function(){
	ui.setNewForm();
	setearEventos();
});

function setearEventos(){
  $('#formUsuario input[name=nombre_usuario]').focusout(ui.validarNombreUsuario);
  $('#formUsuario input[name=password], #formUsuario input[name=repetirPassword]').focusout(ui.validarPass);
  $('#formUsuario input[name=email]').focusout(ui.validarMail);
  $('form input[required]').focusout(ui.validarCampoObligatorio);
  $("#formCliente input[type='file']").change(function(){
		readURL(this);
	});
}
 
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
		var sendData = {
		  action: 'usuario_existe',
		  usuario: inputUsuario.val(),
		};
		var onsuccess = function(jsonData){
			if (jsonData.result){
				customAlert(inputUsuario, labelDelInput(inputUsuario)+": Usuario existente");
			}
		}
		sendAjax(sendData,onsuccess);
	}
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
	$("#form"+form+" input").focus();
	$("#form"+form+" input").last().blur();
	if ($(".panel-error").has("div").length == 0){
		if (!ui.setNewForm(form)){ // si no hay mas formularios envio datos;
			ui.cargarForm();
		}
	}else{
		// Oculto el form actual y muestro el que tiene el error.
		$("#form"+form).hide();
		$(".panel-error").has("div").closest(".panel").show();
	}
}

ui.cargarForm = function () {
	
	var sendData = {};
	sendData.action = 'new';
	sendData.persona={};
	sendData.usuario={};
	sendData.cliente={};
	
	// cargo persona
	sendData.persona.apellidos = $('#formPersona input[name=apellidos]').val() || null;
	sendData.persona.nombres= $('#formPersona input[name=nombres]').val() || null;
	sendData.persona.tipo_doc= $('#formPersona select[name=tipo_doc]').val() || null;
	sendData.persona.nro_doc= $('#formPersona input[name=nro_doc]').val() || null;
	sendData.persona.fecha_nacimiento= $('#formPersona input[name=fecha_nacimiento]').val() || null;
	sendData.persona.sexo= $('#formPersona select[name=sexo]').val() || null;
	sendData.persona.domicilio= $('#formPersona input[name=domicilio]').val() || null;
	sendData.persona.telefono= $('#formPersona input[name=telefono]').val() || null;

	// cargo Usuario
	sendData.usuario.id_persona = null;
	sendData.usuario.nombre_usuario= $('#formUsuario input[name=nombre_usuario]').val() || null;
	sendData.usuario.password = $('#formUsuario input[name=password]').val() || null;
	sendData.usuario.email = $('#formUsuario input[name=email]').val() || null;
	
	// cargo Cliente
	sendData.cliente.foto_registro = $("#img_registro").attr("src");
	sendData.cliente.foto_usuario = $("#img_usuario").attr("src");
	
	console.log("mando: ",sendData);
	
	var onSuccess = function(jsonData){
		if (jsonData.result) {
			//podria limpiar formularios en esta linea
			ui.setNewForm();
			successMessage(jsonData.msg);
		} else {
			errorMessage (jsonData.msg);
		}
	}
	
	sendAjax(sendData,onSuccess);
}

ingresarLogin = function(){
	var parametro = "usuario";
	var valor = $("#formUsuario input[name='nombre_usuario']").val();
	
	window.location = "login.html?"+parametro+"="+valor;
}

ui.setNewForm = function (actualForm) {
  //cambio de un form al siguiente, si es el ultimo envio datos.
  if (actualForm==undefined){
    ui.activateForm("Usuario");
	$("#formUsuario input[required]").first().focus();
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

sendAjax = function (sendData,callback) {
	$.ajax({
		url: '/registro',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			callback(jsonData);
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

//MODALS

errorMessage = function (textMsg) {
	$('#errorMessage').text(textMsg);
	$('#modalError').modal('show');
}
successMessage = function (textMsg) {
	$('#successMessage').text(textMsg);
	$('#modalSuccess').modal('show');
}
closeModal = function (name) {
	$('#modal' + name).modal('hide');
}

//img
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $(input).closest(".form-group").find("img").attr('src', e.target.result).show();
		}

        reader.readAsDataURL(input.files[0]);
    }
}