var usuario_perfil = getUrlVars()["usuario"];
var data = {}

data.persona = {
/*
	apellidos: "Garcia",
	nombres: "Lucho",
	tipo_doc: "1" ,
	nro_doc: "36071223",
	fecha_nacimiento: "23/04/1992", 
	sexo: "1",
	domicilio: "25 de mayo, 1168",
	telefono: "425563"
	*/
}
data.usuario = {
	/*
	nombre_usuario : "Lucho85",
	mail= "usu@hotmail.com"
	*/
};
data.cliente = {
	/*
	reputacion: 3,
	foto: upload/foto.jpg,
	foto_registro: sarasa.png
	*/
}
data.usuario_logueado = {
	/*
	es_perfil_propio: true
	*/
};
data.sponsor= {};
data.super_usuario = {};

var sendAjax = function(sendData,callback){
	console.log("mando: ",sendData);
	$.ajax({
		url: '/perfil', 
		dataType: 'json',
		method: 'POST',
		data: sendData,
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
var loadData = function() {
	
	var sendData = {
		usuario_perfil: usuario_perfil
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			console.log("Me traje: ",jsonData);
			$('.loadingScreen').fadeOut();	
			data.usuario_logueado = jsonData.usuario_logueado;
			data.cliente = jsonData.cliente;
			data.sponsor = jsonData.sponsor;
			data.super_usuario = jsonData.super_usuario; // El admin no tiene datos propios asi que esto quedara vacio.
			data.persona = jsonData.persona;
			data.usuario = jsonData.usuario;
			cargarPerfil();
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;
		}
	}
	$.ajax({
		url: '/perfil', 
		dataType: 'json',
		method: 'GET',
		data: sendData,
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			onsuccess(jsonData);
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

var initUI = function(){
	loadData();
	$('[data-toggle="tooltip"]').tooltip(); 
}

window.onload=initUI;

var cargarPerfil = function(){
	//Limpio UI
	$("#panel-perfil").empty();

	// TRADUZCO DATA
	if (data.cliente){
		data.cliente.foto_revisada = data.cliente.foto || "img/perfil/default.png";
		data.cliente.foto_registro_revisada = data.cliente.foto_registro || "img/perfil/sin_registro.jpg";
		data.cliente.reputacion_stars =  reputacionStars(data.cliente.reputacion);
	}
	data.persona.tipo_doc_string = tipoDocString(data.persona.tipo_doc);
	data.persona.sexo_string = sexoString(data.persona.sexo);
	
	// GENERO HTML DINAMICO
	var template = $("#perfil-template").html();
	$("#panel-perfil").append(Mustache.render(template,data));
	
	setearEventos();
}

function setearEventos(){
	$("input[type='file']").change(function(){
		readURL(this);
	});
	$('#foto_perfil').load(function() {
		var imageSrc = $(this).attr("src");
		if (imageSrc != data.cliente.foto){
			enviarFoto("perfil",imageSrc);
		}
	});
	$('#foto_registro').load(function() {
		var imageSrc = $(this).attr("src");
		if (imageSrc != data.cliente.foto_registro){
			enviarFoto("registro",imageSrc);
		}
	});
}

var enviarFoto = function(atributo, src){
	var sendData = {
		nombre_usuario : data.usuario.nombre_usuario,
		action : "modificar_imagen"
	}
	if (atributo == "registro"){
		sendData.foto_registro = src;
	} else if (atributo == "perfil"){
		sendData.foto = src;
	}
	var onsuccess = function(jsonData){
		if (jsonData.result){
			loadData();
		} 
	}
	sendAjax(sendData,onsuccess);
}

var activarModificar = function(){
	$("#table-perfil input").attr("disabled",false);
}

var tipoDocString = function(caracter){
	switch (caracter) {
		case 1: return "DNI";
		case 2: return "LE";
		case 3: return "LC";
		case '': return "No especificado";
		case null: return "No especificado";
		default: return "Desconocido";
	}
}
var sexoString = function(caracter){
	switch (caracter) {
		case 'O': return "Otro";
		case 'F': return "Femenino";
		case 'M': return "Masculino";
		case '': return "No especificado";
		case null: return "No especificado";
		default: return "Desconocido";
	}
}
var reputacionStars = function(caracter){
	var stars = "";
	while (caracter > 0){
		stars += "★";
		caracter--;
	}
	return stars;
}

//img
var imagenValida = function(file){
	var maxTam = 1500000; // tamano maximo 1.5MB
	if (file.size >= maxTam){
		modalMessage("error", "Archivo es demasiado grande", "Modificar Imagen");
		return false;
	}
    if (file.type.indexOf("image") == -1){
		modalMessage("error", "Debe seleccionar una imagen", "Modificar Imagen");
		return false;
	}	
	return true;
}

function readURL(input) {
	var id = $(input).attr("name");
    if (input.files && input.files[0] && imagenValida(input.files[0])) {
		var reader = new FileReader();

		reader.onload = function (e) {
			$("#"+id).attr('src', e.target.result).show();
		}

		reader.readAsDataURL(input.files[0]);
    }
}

// modal
var modalMessage = function (modalName,textMsg,titleMsg) {
	$('#'+modalName+'-message').text(textMsg);
	if (titleMsg){
		$('#modal-'+modalName +" .dialog-title").text(titleMsg);
	}
	$('#modal-'+modalName).modal('show');
}
var closeModal = function (name) {
	$('#modal-' + name).modal('hide');
}

// funciones robadas

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}

