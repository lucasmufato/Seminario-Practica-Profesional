var selectedId = null;

initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover');
	$('input, select, textarea').addClass('form-control');
	$('label').addClass('control-label');
	/*-----------*/
	$('.loadingScreen').fadeOut(); 
};

$(document).ready(function(){
	initUI();
	aux.clearSelectedRow($('#resultadosviaje tbody')[0]);
});

var showViajes = function(jsonData){

	var tbody = $('#resultadosviaje tbody')[0];
	var tr;
	
	tbody.innerHTML = '';

	if (jsonData.viajes){
		jsonData.viajes.forEach(function (elem){
			tr = document.createElement ('TR');

			tr.appendChild (aux.td (elem.origen));
			tr.appendChild (aux.td (elem.destino));
			tr.appendChild (aux.td (elem.fecha_inicio));
			tr.appendChild (aux.td (elem.hora));
			tr.appendChild (aux.td (aux.estadoString(elem.estado)));
			tr.appendChild (aux.td (elem.conductor));
			tr.appendChild (aux.td (aux.reputacionStars(elem.reputacion),"reputacion"));

			var thistr = tr;
			tr.onclick = function () {
				aux.clearSelectedRow (tbody);
				selectedId = elem.id;
				aux.enableButtons();
				$(thistr).addClass('info');
			}
			tbody.appendChild(tr);
		});
	}else{
		var td;

		tr = document.createElement ('TR');
		td = document.createElement ('TD');
		td.setAttribute ('colspan', 4);
		td.textContent = "No se hay resultados para la busqueda";
		td.className = "warning";
	
		tbody.appendChild (tr);
		tr.appendChild (td);
	}

}

var buscarViaje = function(){
	var sendData = {};
	
	sendData.origen = $('#formBusqueda input[name=origen]').val().toLowerCase();
	sendData.destino = $('#formBusqueda input[name=destino]').val().toLowerCase();
	sendData.fecha_desde = $('#formBusqueda input[name=fechadesde]').val();
	sendData.fecha_hasta = $('#formBusqueda input[name=fechahasta]').val();
	sendData.conductor = $('#formBusqueda input[name=conductor]').val().toLowerCase();
	sendData.asientos = $('#formBusqueda select[name=asientos]').val();
	sendData.estadoViaje = $('#formBusqueda select[name=estadoviaje]').val();
		
	sendForm(sendData, showViajes);

}

var verViajeDetallado = function(){
	var linkViaje = "detalle_viaje(jas).html?id="+selectedId;
	console.log(linkViaje);
	window.location = linkViaje;
}

var sendForm = function (sendData, onsuccess) {
	console.log("Data a enviar: ",sendData);
	
	//SIMULO DATA RECIBIDA:
	var jsonData = {
		"result" : "true",
		"viajes" : [{
			"id" : 3,
			"origen" : "Luján",
			"destino" : "Pilar",
			"fecha_inicio" : "2016-05-17",
			"hora" : "20:30", //podria ir incluido en fecha
			"estado" : "2",
			"conductor" : "Carlos Ruiz",
			"reputacion" : 3 // yo lo sacaria.
		},{
			"id" : 10,
			"origen" : "Jauregui",
			"destino" : "La quiaca",
			"fecha_inicio" : "2016-03-12",
			"hora" : "12:30", //podria ir incluido en fecha
			"estado" : "1",
			"conductor" : "Lisandro Pedrera",
			"reputacion" : 1 // yo lo sacaria.
		},{
			"id" : 123,
			"origen" : "Navarro",
			"destino" : "Navarro",
			"fecha_inicio" : "2016-05-17",
			"hora" : "15:23", //podria ir incluido en fecha
			"estado" : "4",
			"conductor" : "Maria Cardenas",
			"reputacion" : 3 // yo lo sacaria.
		},{
			"id" : 2,
			"origen" : "San Luis",
			"destino" : "Rosario",
			"fecha_inicio" : "2012-12-27",
			"hora" : "20:30", //podria ir incluido en fecha
			"estado" : "3",
			"conductor" : "Renata Lopez",
			"reputacion" : 5 // yo lo sacaria.
		}]
	};
	onsuccess(jsonData);
	
	/*
	$.ajax({
		url: '/viaje',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if (jsonData.result) {
				onsuccess (jsonData);
			} else {
				errorMessage (jsonData.msg);
			}
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
	*/
}

aux = {};
aux.td = function (text,className){
	var td = document.createElement('TD');
	td.appendChild(document.createTextNode(text));
	if (className != undefined) {td.className = className;}
	return td;
}

aux.clearSelectedRow = function (tbody) {
	debugTBODY = tbody;
	var i = 0;

	while (i < tbody.childNodes.length) {
		$(tbody.childNodes[i]).removeClass('info');
		i++;
	}
	aux.disableButtons ();
	selectedId = null;
}

aux.disableButtons = function () {
	$('#verViajeButton').prop('disabled', true);
}

aux.enableButtons = function () {
	$('#verViajeButton').prop('disabled', false);
}

aux.estadoString = function (caracter) {
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

aux.reputacionStars = function(caracter){
	var stars = "";
	while (caracter > 0){
		stars += "★";
		caracter--;
	}
	return stars;
}

//MODALS

errorMessage = function (textMsg) {
	$('#errorMessage').text(textMsg);
	$('#modalError').modal('show');
}
closeModal = function (name) {
	$('#modal' + name).modal('hide');
}