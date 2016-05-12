var nombre_usuario = getUrlVars["usuario"];
var viajes = [];

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
var loadData = function() {

	var sendData = {
		action: "ver_mis_viajes",
		nombre_usuario: nombre_usuario,
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();		
			viajes = jsonData.viajes;
			showViajes();
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;
		}
	}
	simular();
	
	sendAjax(sendData,onsuccess);
	
}

var initUI = function(){
	ocultarPaneles();
	loadData();
	$('[data-toggle="tooltip"]').tooltip(); 
}

var ocultarPaneles = function(){
	$("#panel-mis-viajes").hide();
	$("#formFilter").hide();
}

window.onload=initUI;

var simular = function(){
	viajes =  [{
			"id" : 3,
			"origen" : "Luján",
			"destino" : "Pilar",
			"fecha_inicio" : "2016-05-17",
			"conductor" : "Carlos Ruiz",
			"reputacion" : "3",
			"precio": "200",
			"foto":"upload/foto.jpg"
		},{
			"id" : 10,
			"origen" : "Jauregui",
			"destino" : "La quiaca",
			"fecha_inicio" : "2016-03-12",
			"conductor" : "Lisandro Pedrera",
			"reputacion" : "1",
			"precio": "200",
			"foto":""
		},{
			"id" : 123,
			"origen" : "Navarro",
			"destino" : "Navarro",
			"fecha_inicio" : "2016-05-17",
			"conductor" : "Maria Cardenas",
			"reputacion" : 3,
			"precio": "456",
			"foto":"img/home/administracion_usuarios.png"
		},{
			"id" : 2,
			"origen" : "San Luis",
			"destino" : "Rosario",
			"fecha_inicio" : "2012-12-27",
			"conductor" : "Renata Lopez",
			"reputacion" : "5",
			"precio": "290",
			"foto":"upload/foto.jpg"
		}];
	showViajes();
}

var showViajes = function(){
	$("#panel-mis-viajes").show();
	if (viajes.length){
		autogeneratePages();
		changePage(1);
	}else{
		var html = 	"<div class='jumbotron'>"
						+"<h4 class='text-center text-primary'>Aun no ha realizado viajes</h4>"
					+"</div>"
		$("#busqueda-paginacion").hide();
		$("#mis-viajes").html(html);
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
    var listing_table = $("#mis-viajes");
 
    // Validate page
    if (page < 1) page = 1;
    if (page > numPages()) page = numPages();

    var html = "";
	var template = $("#viaje-template").html();
    for (var i = (page-1) * records_per_page; i < (page * records_per_page); i++) {
		if (viajes[i]){
			viajes[i].reputacion_stars = reputacionStars(viajes[i].reputacion);
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

var toggleFilter = function(){
	if ($("#formFilter").is(":visible")){
		$("#formFilter").slideUp();
	}else{
		$("#formFilter").slideDown();
	}
}


reputacionStars = function(caracter){
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