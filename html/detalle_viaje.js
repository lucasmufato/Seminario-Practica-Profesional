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
	cargarViaje();
}

var cargarViaje = function(){
	
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

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}
