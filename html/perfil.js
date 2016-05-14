var usuario_perfil = getUrlVars()["usuario"];
var data = {}

data.cliente = {
/*
	nombre_usuario : "Lucho85",
	apellidos: "Garcia",
	nombres: "Lucho",
	tipo_doc: "1" ,
	nro_doc: "36071223",
	fecha_nacimiento: "23/04/1992", 
	sexo: "1",
	domicilio: "25 de mayo, 1168",
	telefono: "425563"
	*/
};

data.usuario_logueado = {
/*
	es_perfil_propio: true
	*/
};

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
			$('.loadingScreen').fadeOut();	
			data.usuario_logueado = jsonData.usuario_logueado;
			data.cliente = jsonData.cliente;
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
	console.log("Cargando Perfil");
	$("#reputacion").text(reputacionStars(data.cliente.reputacion));
	$("#foto_perfil").attr("src",data.cliente.foto);
	$("#nombre_usuario").text(data.cliente.nombre_usuario);
	$("#apellidos").text(data.cliente.apellidos);
	$("#nombres").text(data.cliente.nombres);
	$("#tipo_doc").text(tipoDocString(data.cliente.tipo_doc));
	$("#nro_doc").text(data.cliente.nro_doc);
	$("#table-perfil input[name='domicilio']").val(data.cliente.domicilio);
	$("#table-perfil input[name='domicilio']").attr("value",data.cliente.domicilio);
	$("#table-perfil input[name='mail']").val(data.cliente.mail);
	$("#table-perfil input[name='mail']").attr("value",data.cliente.mail);
	$("#table-perfil input[name='telefono']").val(data.cliente.telefono);
	$("#table-perfil input[name='telefono']").attr("value",data.cliente.telefono);
	$("#sexo").text(sexoString(data.cliente.sexo));
	$("#foto_registro").attr("src",data.cliente.foto_registro);
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

