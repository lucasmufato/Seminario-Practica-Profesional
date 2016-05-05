var data = {};
data.viaje = {};
data.conductor = {};
data.vehiculo = {};
data.comentarios = {};

data.loadData = function() {
	var sendData = {
		"id": getUrlVars()["id"]
	}
	simular(sendData);
	/*
	$.ajax({
		url: '', //COMPLETAR
		dataType: 'json',
		method: 'POST',
		data: sendData,
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if(jsonData.result){
				$('.loadingScreen').fadeOut();
				data.viaje = jsonData.viaje;
				data.conductor = jsonData.conductor;
				data.vehiculo = jsonData.vehiculo;
				data.comentarios = jsonData.comentarios;
				cargarViaje();
				cargarVehiculo();
				cargarComentarios();
				cargarConductor();
			} else if (jsonData.redirect != undefined) {
				window.location = jsonData.redirect;
			}
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
	*/
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
	console.log(json.id);
	data.viaje = {
		tipo: "ida",
		origen: "Lujan",
		destino: "Moreno",
		fecha: "12/09/2016",
		hora: "20:30",
		precio: "30",
		puntos_intermedios: ["Rodriguez","Otro lugar"]
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
	cargarViaje();
	cargarConductor();
	cargarVehiculo();
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

var cargarConductor = function(){
	
	$("#tipo").text(data.viaje.tipo);
	$("#origen").text(data.viaje.origen);
	$("#destino").text(data.viaje.destino);
	$("#fecha").text(data.viaje.fecha);
	$("#hora").text(data.viaje.hora);
	$("#precio").text("$"+data.viaje.precio);
	data.viaje.puntos_intermedios.forEach(function(elem){
		$("#puntosIntermedios").append('<li>'+elem+'</li>');
	});

}

var cargarViaje = function(){
	$("#panel-foto-conductor a").attr('href',data.conductor.foto);
	$("#panel-foto-conductor img").attr('src',data.conductor.foto);
	$("#panel-foto-registro a").attr('href',data.conductor.foto_registro);
	$("#panel-foto-registro img").attr('src',data.conductor.foto_registro);
	$("#reputacion").text(reputacionStars(data.conductor.reputacion));;
	$("#nombreConductor").text(data.conductor.nombre_usuario);
	$("#nombreConductor").attr('href',"perfil.html?=nombre_usuario="+data.conductor.nombre_usuario);
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

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}
