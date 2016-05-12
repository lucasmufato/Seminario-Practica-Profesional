var idViaje = getUrlVars()["id"];
var postulantes = [];

var loadData = function() {

	var sendData = {
		action: "ver_postulantes",
		"id": idViaje
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			postulantes = jsonData.postulantes;
			if (postulantes) cargarPostulantes();
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
		estado: "1", //1: postulado, 2: aceptado, 3: rechazado
		nombre_usuario: "Carolo4",
		foto:"img/home/administracion_usuarios.png",
		origen: "San Andres de Giles, Buenos Aires, Argentina",
		destino: "Carmen de areco, Buenos Aires, Argentina",
		apellido: "Perez",
		nombres: "Carolo",
		telefono: "421154",
		mail: "carolo4@gmail.com"
	},{
		estado: "2", //1: postulado, 2: aceptado, 3: rechazado
		nombre_usuario: "KarinaK100",
		foto:"upload/foto.jpg",
		origen: "Navarro, Buenos Aires, Argentina",
		destino: "Rosario, Santa Fe, Argentina",
		apellido: "Krenz",
		nombres: "Karina",
		telefono: "497673",
		mail: "Karina234123@outlook.com"
	},{
		estado: "3",
		nombre_usuario: "RodolfoU",
		foto:"upload/foto.jpg",
		origen: "Rosario, Santa Fe, Argentina",
		destino: "Montevideo, Uruguay",
		apellido: "Uber",
		nombres: "Rodolfo",
		telefono: "3434 15421154",
		mail: "rodolfoU@hotmail.com"
	}];
	if (postulantes.length) cargarPostulantes();
}

var cargarPostulantes = function(){
	var template = $("#postulante-template").html();
	var htmlPendientes = "";
	var htmlNoPendientes = "";
	postulantes.forEach(function(elem){
		elem.estado_string = estadoString(elem.estado);
		elem.color_panel = colorPanel(elem.estado);
		if (elem.es_pendiente = elem.estado == 1){
			htmlPendientes += Mustache.render(template, elem);
		}else{
			htmlNoPendientes += Mustache.render(template, elem);
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

var aceptarPostulante = function(nombre_usuario){
	console.log(nombre_usuario);
	var sendData = {
		action: "aceptar_postulante",
		"id": idViaje,
		"nombre_usuario": nombre_usuario
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			postulantes = jsonData.postulantes;
			cargarPostulantes();
		} else {
			modalMessage("error",jsonData.msg,"Aceptar postulante");
		}
	}	
	sendAjax(sendData,onsuccess);
}
var rechazarPostulante = function(nombre_usuario){
	var sendData = {
		action: "rechazar_postulante",
		"id": idViaje,
		"nombre_usuario": nombre_usuario
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			postulantes = jsonData.postulantes;
			cargarPostulantes();
		} else {
			modalMessage("error",jsonData.msg,"Rechazar postulante");
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
		case '1': return "Postulado";
		case '2': return "Aceptado";
		case '3': return "Rechazado";
		case null: return "No especificado";
		default: return "Desconocido";
	}
}

var colorPanel = function(caracter){
	switch (caracter) {
		case '1': return "info";
		case '2': return "success";
		case '3': return "danger";
		case null: return "default";
		default: return "default";
	}
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