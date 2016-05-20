var vehiculos = [];
var clientes = [];

var loadData = function() {

	var sendData = {
		action: "ver_mis_vehiculos"
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			vehiculos = jsonData.vehiculos;
			if (postulantes) {
				cargarVehiculos();
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
	clientes = [{
		id: "48", 
		nombre_usuario: "Lucho85"
	},{
		id: "34", 
		nombre_usuario: "Lore92"
	}, {
		id: "15",
		nombre_usuario: "Pepa15"
	},{
		id: "2",
		nombre_usuario: "Lolo91"
	}];
	vehiculos = [{
		id: "1",
		marca: "ford",
		modelo: "focus",
		color: "azul",
		anio: "2010",
		patente: "ifg 999",
		seguro: "si",
		aire: "si",
		cantidad_asientos: "4",
		cliente_vinculado: ["48"],
		foto_vehiculo: "http://www.coches.com/fotos_historicas/ford/Focus/med_ford_focus-2014_r3.jpg.pagespeed.ce.JW0wSPihZv.jpg",
		vehiculo_verificado: "s"
	},{
		id: "11",
		marca: "ford",
		modelo: "focus",
		color: "gris",
		anio: "2010",
		patente: "ifg 111",
		seguro: "si",
		aire: "si",
		cantidad_asientos: "4",
		cliente_vinculado: ["48","15"],
		foto_vehiculo: "https://www.16valvulas.com.ar/wp-content/uploads/2007/10/ford-focus-2008.jpg",
		vehiculo_verificado: "n"
	},{
		id: "21",
		marca: "ford",
		modelo: "focus",
		color: "gris",
		anio: "2010",
		patente: "ifg 222",
		seguro: "si",
		aire: "si",
		cantidad_asientos: "4",
		cliente_vinculado: ["48","34"],
		foto_vehiculo: "https://www.16valvulas.com.ar/wp-content/uploads/2007/10/ford-focus-2008.jpg",
		vehiculo_verificado: "n"
	},{
		id: "31",
		marca: "ford",
		modelo: "focus",
		color: "gris",
		anio: "2010",
		patente: "ifg 444",
		seguro: "si",
		aire: "si",
		cantidad_asientos: "4",
		cliente_vinculado: ["48","15","34"],
		foto_vehiculo: "https://www.16valvulas.com.ar/wp-content/uploads/2007/10/ford-focus-2008.jpg",
		vehiculo_verificado: "s"
	}];
	if (vehiculos.length) {
		cargarVehiculos();
	}
}

var clientePorId = function(id){
	var l;
	clientes.forEach(function(elem){
		if (elem.id == id){
			l = elem;
		}
	});
	return l.nombre_usuario;
}

var cargarAsociados = function(elem) {
	elem.cliente_vinculado.forEach(function(cli){
		console.log(clientePorId(cli));
		html += $("#clientes_asociados").append('<li>'+clientePorId(cli)+'</li>');
	});
}

var cargarVehiculos = function(){
	var template = $("#vehiculo-template").html();
	var htmlVehiculosVerificados = "";
	var htmlVehiculosNoVerificados = "";
	vehiculos.forEach(function(elem){
		elem.color = panelColor(elem.vehiculo_verificado);
		if (elem.es_verificado = elem.vehiculo_verificado == "s"){
			htmlVehiculosVerificados += Mustache.render(template, elem);
		}else{
			htmlVehiculosNoVerificados += Mustache.render(template, elem);
		}
	});
	if (htmlVehiculosVerificados != "" && htmlVehiculosNoVerificados != ""){
		var html = "<div class=' col-md-6'>"
			+htmlVehiculosVerificados+
		"</div>"+
		"<div class=' col-md-6'>"
			+htmlVehiculosNoVerificados+
		"</div>"
	}else{
		var html = "<div class='row col-xs-12 col-sm-12 col-md-12 col-lg-12' >"
			+htmlVehiculosVerificados+htmlVehiculosNoVerificados+
			"</div>";
	}
	$("#panel-vehiculos").html(html);

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
		case '1': return "Verificado";
		case '2': return "No verificado";
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


var panelColor = function (caracter) {
	switch(caracter) {
		case 's': return "info";
		case 'n': return "danger";
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