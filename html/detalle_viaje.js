var data = {};
data.viaje = {};
data.viaje.id = getUrlVars()["id"];
data.conductor = {};
data.vehiculo = {};
data.comentarios = {};
data.usuario_logueado = {};

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


data.loadData = function() {
	var sendData = {
		action: "ver_viaje",
		"id": data.viaje.id
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			data.viaje = jsonData.viaje;
			data.conductor = jsonData.conductor;
			data.vehiculo = jsonData.vehiculo;
			data.comentarios = jsonData.comentarios;
			data.usuario_logueado = jsonData.usuario_logueado;
			cargarViaje();
			cargarVehiculo();
			cargarComentarios();
			cargarConductor();
			configurarUi();
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;
		}
	}
	
	simular(sendData);
	
	sendAjax(sendData,onsuccess);
}

initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover');
	$('input, select, textarea').addClass('form-control');
	$('label').addClass('control-label');
	/*-----------*/
	data.loadData();
};

$(document).ready(function(){

});

var simular = function(json){
	console.log("id: ",json.id);
	data.viaje = {
		id: 35,
		nombre_amigable: "Un alto viaje",
		estado: "2",
		tipo: "ida",
		id_viaje_complemento: "4",
		origen: "Lujan",
		destino: "Moreno",
		fecha: "12/09/2016",
		hora: "20:30",
		precio: "30",
		recorrido: ["Lujan", "Rodriguez","Moreno"]
	};
	data.conductor = {
		nombre_usuario: "Juanc23",
		reputacion: "3",
		foto: "upload/foto.jpg",
		foto_registro: "upload/foto_reg.jpg"
	};
	data.vehiculo = {
		marca: "Ford",
		modelo: "Falcon",
		anio: "2012",
		patente: "LKJ 890",
		aire: "S",
		seguro: "N",
		verificado: "S",
		foto: "upload/auto.jpg"
	};
	data.usuario_logueado = {
		es_conductor: false
	};	
	cargarViaje();
	cargarConductor();
	cargarVehiculo();
	configurarUi();
}

var configurarUi = function(){

	var esConductor = data.usuario_logueado.es_conductor;
	if (esConductor){
		$("#btnModificar").show();
		$("#btnParticipar").hide();
	}else{
		$("#btnModificar").hide();
	}
	
	var estado = data.viaje.estado;
	if (estado != 2){ //si esta en "no iniciado"
		$("#btnParticipar").hide();
		$("#btnSuscribirse").hide();
		$("#btnModificar").hide();
	} 
}

var cargarVehiculo = function(){
	$("#panel-foto-vehiculo a").attr('href',data.vehiculo.foto);
	$("#panel-foto-vehiculo img").attr('src',data.vehiculo.foto);
	$("#marca").text(data.vehiculo.marca);
	$("#modelo").text(data.vehiculo.modelo);
	$("#anio").text(data.vehiculo.anio);
	$("#patente").text(data.vehiculo.patente);
	$("#aire").html(generarEmoticon(data.vehiculo.aire));
	$("#seguro").html(generarEmoticon(data.vehiculo.seguro));
	$("#verificado").html(generarEmoticon(data.vehiculo.verificado));

}

var cargarViaje = function(){
	
	$("#tipo").text(data.viaje.tipo);
	setearViajeComplemento(data.viaje.id_viaje_complemento);

	$("#estado").text(estadoString (data.viaje.estado));
	$("#origen").text(data.viaje.origen);
	$("#destino").text(data.viaje.destino);
	$("#fecha").text(data.viaje.fecha);
	$("#hora").text(data.viaje.hora);
	$("#precio").text("$"+data.viaje.precio);
	data.viaje.recorrido.forEach(function(elem){
		$("#recorrido").append('<li>'+elem+'</li>');
	});

}

var cargarConductor = function(){
	$("#panel-foto-conductor a").attr('href',data.conductor.foto);
	$("#panel-foto-conductor img").attr('src',data.conductor.foto);
	$("#panel-foto-registro a").attr('href',data.conductor.foto_registro);
	$("#panel-foto-registro img").attr('src',data.conductor.foto_registro);
	$("#reputacion").text(reputacionStars(data.conductor.reputacion));;
	$("#nombreConductor").text(data.conductor.nombre_usuario);
	$("#nombreConductor").attr('href',"perfil.html?=nombre_usuario="+data.conductor.nombre_usuario);
}

function setearViajeComplemento(idComp){
	if (idComp){
		var link = "detalle_viaje(jas).html?id=" + idComp;
		$("#tipo").append(" <a href='"+link+"'><small>(complemento)</small></a>")
	}
}

var mostrarVentanaParticipar = function(){
	$("select[name=origenPasajero], select[name=destinoPasajero]").empty();
	data.viaje.recorrido.forEach(function(elem){
		var option = document.createElement("OPTION");
		option.text = elem;
		option.value = elem;
		$("select[name=origenPasajero], select[name=destinoPasajero]").append(option);
	});
	$('#modalParticipar').modal('show');
}

var participarViaje = function(){
	var sendJson = {
		action: "participar",
		id_viaje: data.viaje.id,
		origen:  $("select[name=origenPasajero]").val(),
		destino:  $("select[name=destinoPasajero]").val()
	}
	var onsuccess = function(jsonData){
		closeModal('Participar');
		if (jsonData.result){
			$('#modalSuccess').modal('show');
		}else{
			errorMessage(jsonData.msg);
		}
	}
	sendAjax(sendJson,onsuccess);
}

estadoString = function (caracter) {
	switch (caracter) {
		case '1': return "Terminado";
		case '2': return "No iniciado";
		case '3': return "Iniciado";
		case '4': return "Cancelado";
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

var generarEmoticon = function(caracter){
	var span = document.createElement("SPAN");
	if (caracter=="S"){
		span.className = "glyphicon glyphicon-ok text-success";
		return span;
	}else{
		span.className = "glyphicon glyphicon-remove text-danger";
		return span;
	}
}

//MODALS

errorMessage = function (textMsg) {
	$('#errorMessage').text(textMsg);
	$('#modalError').modal('show');
}
closeModal = function (name) {
	$('#modal' + name).modal('hide');
}

// funciones robadas

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
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