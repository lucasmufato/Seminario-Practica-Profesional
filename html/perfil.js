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
	//
	// Mañana esto lo paso a mustache y desaparece
	//
	if (data.cliente){
		$("#reputacion").text(reputacionStars(data.cliente.reputacion));
		$("#foto_perfil").attr("src",data.cliente.foto);
		$("#foto_registro").attr("src",data.cliente.foto_registro);
	}else if (data.super_usuario){
		console.log("hola");
		$("#reputacion").parent().hide();
		$("#foto_perfil").parent().hide();
		$("#foto_registro").parent().parent().hide();
	}
	
	$("#nombre_usuario").text(data.usuario.nombre_usuario);
	$("#apellidos").text(data.persona.apellidos);
	$("#nombres").text(data.persona.nombres);
	$("#tipo_doc").text(tipoDocString(data.persona.tipo_doc));
	$("#nro_doc").text(data.persona.nro_doc);
	$("#table-perfil input[name='domicilio']").val(data.persona.domicilio);
	$("#table-perfil input[name='domicilio']").attr("value",data.persona.domicilio);
	$("#table-perfil input[name='mail']").val(data.usuario.mail);
	$("#table-perfil input[name='mail']").attr("value",data.usuario.mail);
	$("#table-perfil input[name='telefono']").val(data.persona.telefono);
	$("#table-perfil input[name='telefono']").attr("value",data.persona.telefono);
	$("#sexo").text(sexoString(data.persona.sexo));
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

// funciones robadas

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}

