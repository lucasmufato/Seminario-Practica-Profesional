var selectedId = null;

var viajes = [];

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
	$("#panel-resultados").hide();
});

var showViajes = function(){
	$("#panel-resultados").show();
	if (viajes.length){
		/*
		jsonData.viajes.forEach(function (elem){
			html += Mustache.render(template, elem);
		});
		*/
		autogeneratePages();
		changePage(1);
	}else{
		var html = 	"<div class='jumbotron'>"
						+"<h4 class='text-center text-primary'>No hay resultados</h4>"
					+"</div>"
		$("#busqueda-paginacion").hide();
		$("#viajes-busqueda").html(html);
	}

}
/////paginacion////
var current_page = 1;
var records_per_page = 3;

function autogeneratePages(){
	//Ir a pagina anterior
	var prevPage = "<li id='previous-page'>"+
					  "<a href='javascript:prevPage()' aria-label='Previous'>"+
						"<span aria-hidden='true'>&laquo;</span>"+
					  "</a>"+
					"</li>";
	
	//paginas de resultado
	var html="";
	for (i=1; i<=numPages();i++){
		html += "<li><a href='javascript:changePage("+i+")'>"+i+"</a></li>";
	}
	
	// Siguiente pagina
	var nextPage = "<li id='next-page'>"+
					  "<a href='javascript:nextPage()' aria-label='Next'>"+
						"<span aria-hidden='true'>&raquo;</span>"+
					  "</a>"+
					"</li>";
									
	$("#busqueda-paginacion").html(prevPage+html+nextPage);
}

function changePage(page){
	current_page = page;

    var btn_prev = $("#previous-page");
    var btn_next = $("#next-page");
    var listing_table = $("#viajes-busqueda");
 
    // Validate page
    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    var html = "";
	var template = $("#viaje-template").html();
    for (var i = (page-1) * records_per_page; i < (page * records_per_page); i++) {
		if (viajes[i]){
		    html += Mustache.render(template, viajes[i]);
		}
    }
    listing_table.html(html);
	
	$( "#busqueda-paginacion").find(".active").removeClass("active");
	$( "#busqueda-paginacion li:eq("+page+")" ).addClass("active");

    if (page == 1) {
        btn_prev.addClass("disabled");
    } else {
        btn_prev.removeClass("disabled");
    }

    if (page == numPages()) {
        btn_next.addClass("disabled");
    } else {
        btn_next.removeClass("disabled");
    }
}
function numPages(){
    return Math.ceil(viajes.length / records_per_page);
}
function prevPage(){
    if (current_page > 1) {
        current_page--;
        changePage(current_page);
    }
}

function nextPage(){
    if (current_page < numPages()) {
        current_page++;
        changePage(current_page);
    }
}

///////////fin-paginacion//////////

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

var sendForm = function (sendData, onsuccess) {
	
	//SIMULO DATA RECIBIDA:
	var jsonData = {
		"result" : "true",
		"viajes" : [{
			"id" : 3,
			"origen" : "Luján",
			"destino" : "Pilar",
			"fecha_inicio" : "2016-05-17",
			//"hora" : "20:30", //podria ir incluido en fecha
			//"estado" : "2",
			"conductor" : "Carlos Ruiz",
			//"reputacion" : "3",
			"precio": "200",
			"foto":"upload/foto.jpg"
		},{
			"id" : 10,
			"origen" : "Jauregui",
			"destino" : "La quiaca",
			"fecha_inicio" : "2016-03-12",
			//"hora" : "12:30", //podria ir incluido en fecha
			//"estado" : "1",
			"conductor" : "Lisandro Pedrera",
			//"reputacion" : "1",
			"precio": "200",
			"foto":""
		},{
			"id" : 123,
			"origen" : "Navarro",
			"destino" : "Navarro",
			"fecha_inicio" : "2016-05-17",
			//"hora" : "15:23", 
			//"estado" : "4",
			"conductor" : "Maria Cardenas",
			//"reputacion" : 3,
			"precio": "456",
			"foto":"img/home/administracion_usuarios.png"
		},{
			"id" : 2,
			"origen" : "San Luis",
			"destino" : "Rosario",
			"fecha_inicio" : "2012-12-27",
			//"hora" : "20:30", //podria ir incluido en fecha
			//"estado" : "3",
			"conductor" : "Renata Lopez",
			//"reputacion" : "5",
			"precio": "290",
			"foto":"upload/foto.jpg"
		}]
	};
	viajes = jsonData.viajes;
	onsuccess();
	
	/*
	console.log("Data a enviar: ",sendData);
	$.ajax({
		url: '/viaje',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if (jsonData.result) {
				viajes = jsonData.viajes;
				onsuccess ();
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
/*
aux.td = function (text,className){
	var td = document.createElement('TD');
	td.appendChild(document.createTextNode(text));
	if (className != undefined) {td.className = className;}
	return td;
}
*/
/*
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
*/
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