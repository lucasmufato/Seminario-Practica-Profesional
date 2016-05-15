var idViaje = getUrlVars()["id"];
var postulantes = [];

var loadData = function() {

	var sendData = {
		action: "ver_calificar_pendientes",
		id: idViaje
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			postulantes = jsonData.postulantes;
			if (postulantes) {
				cargarPostulantes();
				cargarMisCalificaciones();
			}
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;// si no es conductor de este viaje, acceso denegado
		}
	}
	simular();
	
	sendAjax(sendData,onsuccess);
}

var initUI = function(){
	loadData();
	$('[data-toggle="tooltip"]').tooltip(); 
	
}

window.onload=initUI;



var simular = function(){
	postulantes = [{
		estado: "1", //1: sin_calificar, 2: calificado,
		nombre_usuario: "Carolo4",
		foto:"img/home/administracion_usuarios.png",
		participo: "",
		valoracion: "",
		comentario: "",
		participo_recibido: "",
		valoracion_recibida: "",
		comentario_recibido: ""
	},{
		estado: "2", //1: sin_calificar, 2: calificado,
		nombre_usuario: "KarinaK100",
		foto:"img/home/administracion_usuarios.png",
		participo: "s",
		valoracion: "4",
		comentario: "viajo todo bien",
		participo_recibido: "",
		valoracion_recibida: "",
		comentario_recibido: ""
	},{
		estado: "2", //1: sin_calificar, 2: calificado,
		nombre_usuario: "RodolfoU",
		foto:"img/home/administracion_usuarios.png",
		participo: "n",
		valoracion: "0",
		comentario: "no viajo!",
		participo_recibido: "",
		valoracion_recibida: "",
		comentario_recibido: ""
	},{
		estado: "1", //1: sin_calificar, 2: calificado,
		nombre_usuario: "Lolo",
		foto:"img/home/administracion_usuarios.png",
		participo: "",
		valoracion: "",
		comentario: "",
		participo_recibido: "n",	
		valoracion_recibida: "0",
		comentario_recibido: "no viajo!"
	},{
		estado: "2", //1: sin_calificar, 2: calificado,
		nombre_usuario: "Pepa",
		foto:"img/home/administracion_usuarios.png",
		participo: "s",
		valoracion: "5",
		comentario: "Genial",
		participo_recibido: "s",
		valoracion_recibida: "3",
		comentario_recibido: "Ok"
	}];
	if (postulantes.length) {
		cargarPostulantes();
		//cargarMisCalificaciones();
	}
}
var cargarMisCalificaciones = function(elem, htmlMisCalificaciones) {
	var template = $("#misCalificaciones-template").html();
	
	//postulantes.forEach(function(elem){
		elem.cantidad_estrellas = mostrarEstrellas(elem.valoracion_recibida);
		elem.participacion = mostrarParticipacion(elem.participo_recibido);
		elem.estado = 3;
		elem.es_mc = elem.estado;
		//if (elem.es_mc = elem.estado == 3){
		elem.color_panel = colorPanel(elem.estado);
		console.log("ssssssssssssssssss"+elem.nombre_usuario);
		console.log(elem.cantidad_estrellas+elem.nombre_usuario);
		console.log(elem.comentario_recibido+elem.nombre_usuario);
		htmlMisCalificaciones += Mustache.render(template, elem);
		//}
	//});
	if (htmlMisCalificaciones != "") {
		var html = "<div class=' col-md-6'>"
			+htmlMisCalificaciones+
		"</div>"
	}else{
		var html = "<div class='row col-xs-12 col-sm-12 col-md-12 col-lg-12' >"
			+htmlMisCalificaciones+
			"</div>";
	}
	$("#panel-calif").html(html);
	return htmlMisCalificaciones;
}

var cargarPostulantes = function(){
	var template = $("#postulante-template").html();
	var htmlPendientes = "";
	var htmlNoPendientes = "";
	var htmlMisCalificaciones = "";
	postulantes.forEach(function(elem){
		elem.estado_string = estadoString(elem.estado);
		elem.color_panel = colorPanel(elem.estado);
		elem.cantidad_estrellas = mostrarEstrellas(elem.valoracion);
		elem.participacion = mostrarParticipacion(elem.participo);
		console.log("hdasjkh"+elem.nombre_usuario);	
		if (elem.es_pendiente = elem.estado == 1){
			htmlPendientes += Mustache.render(template, elem);
		} else {
			if (elem.es_listo = elem.estado == 2){
				htmlNoPendientes += Mustache.render(template, elem);
			} 
		}
		if (elem.comentario_recibido != "") {
			console.log("hdasjkh"+elem.nombre_usuario);
			htmlMisCalificaciones+= cargarMisCalificaciones(elem, htmlMisCalificaciones);
		}
	});
	if (htmlPendientes != "" && htmlNoPendientes != ""){
		var html = "<div class=' col-md-6'>"
			+htmlPendientes+
		"</div>"+
		"<div class=' col-md-6'>"
			+htmlNoPendientes+
		"</div>"
	}else{
		var html = "<div class='row col-xs-12 col-sm-12 col-md-12 col-lg-12' >"
			+htmlPendientes+htmlNoPendientes+
			"</div>";
	}
	$("#panel-postulantes").html(html);

}

var calificarPendiente = function(nombre_usuario){
	console.log(nombre_usuario);
	console.log($("#confirmacion_"+nombre_usuario).val());
	console.log($("input:radio[name=estrellas_"+nombre_usuario+"]:checked").val());
	console.log($("#comments_"+nombre_usuario).val());
	completos(nombre_usuario);
	var sendData = {
		action: "calificar_pendiente",
		id: idViaje,
		nombre_usuario: nombre_usuario,
		confirmacion: $("#confirmacion_"+nombre_usuario).val(),
		valoracion: $("input:radio[name=estrellas_"+nombre_usuario+"]:checked").val(),
		comentario: $("#comments_"+nombre_usuario).val()
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			postulantes = jsonData.postulantes;
			cargarPostulantes();
		} else {
			modalMessage("error",jsonData.msg,"Calificar");
		}
	}	
	sendAjax(sendData,onsuccess);
}

var verViaje = function(){
	window.open("detalle_viaje.html?id="+idViaje,"_blank");
}

var sendAjax = function(sendData,callback){
	console.log("mando: ",sendData);
	/*
	$.ajax({
		url: '/viaje', 
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
	*/
}

var estadoString = function (caracter) {
	switch (caracter) {
		case '1': return "No Calificado";
		case '2': return "Calificado";
		case '3': return "Mi Calificación";
		case null: return "No especificado";
		default: return "Desconocido";
	}
}

var colorPanel = function(caracter){
	switch (caracter) {
		case '1': return "info";
		case '2': return "success";
		case '3': return "warning";
		case null: return "default";
		default: return "default";
	}
}

var mostrarEstrellas = function (caracter) {
	switch(caracter){
		case '1': return "★"; 
		case '2': return "★★"; 
		case '3': return "★★★"; 
		case '4': return "★★★★"; 
		case '5': return "★★★★★";
		case null: return "";
		default: return "";
	}
}

var mostrarParticipacion = function (caracter) {
	switch(caracter) {
		case 's': return "Participó en el viaje";
		case 'n': return "No participó en el viaje";
	}
}

var completos = function(nombre_usuario){
	var confirmacion = $("#confirmacion_"+nombre_usuario).val();
	var valoracion = $("input:radio[name=estrellas_"+nombre_usuario+"]:checked").val();
	var comentario = $("#comments_"+nombre_usuario).val();
	if (valoracion == undefined || comentario == ""){
		var msg = "Falta completar información";
		modalMessage("error", msg, "Error");
		return false;
	}
	return true;
}


var modalMessage = function (modalName,textMsg,titleMsg) {
	$('#'+modalName+'-message').text(textMsg);
	if (titleMsg){
		$('#modal-'+modalName+" .dialog-title").text(titleMsg);
	}
	$('#modal-'+modalName).modal('show');
}
var closeModal = function (name) {
	$('#modal-' + name).modal('hide');
}

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}