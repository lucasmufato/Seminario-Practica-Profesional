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
	data.loadData();
};

window.onload=initUI;

initMap = function() {
map = new google.maps.Map(document.getElementById('mapa'), {
	center: {lat: -34.5774135, lng: -59.0909557},
	drawingControl: false,
	zoom: 16
});
}

var simular = function(json){
	console.log("id: ",json.id);
	data.viaje = {
		id: data.viaje.id ,
		nombre_amigable: "Un alto viaje",
		estado: "3",
		tipo: "ida",
		id_viaje_complemento: "4",
		origen: "Lujan",
		destino: "Moreno",
		fecha: "12/09/2016",
		hora: "20:30",
		precio: "30",
		participantes: "1",
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
		es_conductor: false,
		es_pasajero: true,
		es_seguidor: false,
		ha_calificado: false
	};	
	cargarViaje();
	cargarConductor();
	cargarVehiculo();
	configurarUi();
}

var configurarUi = function(){

	var esConductor = data.usuario_logueado.es_conductor;
	var esPasajero = data.usuario_logueado.es_pasajero;
	var esSeguidor = data.usuario_logueado.es_seguidor;
	var haCalificado = data.usuario_logueado.ha_calificado;

	var estado = data.viaje.estado;
	
	/*
	ESTADOS:
		case '1': return "Terminado";
		case '2': return "No iniciado";
		case '3': return "Iniciado";
		case '4': return "Cancelado";
	*/
	if (esPasajero || esConductor){
		$("#botonera-cliente").hide();
		if (esConductor){
			$("#botonera-conductor").show();
			$("#botonera-pasajero").hide();
			
			//Boton finalizar solo si esta en iniciado
			if (estado == "3"){
				$("#btnViajeFinalizado").show();
				$("#btnCalificar").hide();
			}else{
				$("#btnViajeFinalizado").hide();

				//solo califica si hay pasajeros
				if (data.viaje.participantes > 0){
					if (haCalificado){
						$("#btnCalificar").hide();
					}else{
						$("#btnCalificar").show();
					}
				}else{
					$("#btnCalificar").hide();
				}
			}
		}else if (esPasajero){
			$("#botonera-conductor").hide();
			$("#botonera-pasajero").show();
		}
	} else {
		$("#btnCalificar").hide();
		$("#botonera-conductor").hide();
		$("#botonera-pasajero").hide();
		$("#botonera-cliente").show();
		if (esSeguidor){
			$("#btnDejarSeguir").show();
			$("#btnSeguir").hide();
		} else{
			$("#btnDejarSeguir").hide();
			$("#btnSegir").show();		
		}
	}
	if (estado=="4"){//cancelado
		$("#botonera-conductor").hide();
		$("#botonera-pasajero").hide();
		$("#botonera-cliente").hide();
		$("#btnCalificar").hide();
	} else if(estado == "3"){//iniciado
		
	} else if(estado == "2"){ //no_iniciado
		$("#btnCalificar").hide();
	} else if(estado == "1"){ //terminado
		$("#botonera-conductor").hide();
		$("#botonera-pasajero").hide();
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
	$("#nombreConductor").attr('href',"perfil.html?usuario="+data.conductor.nombre_usuario);
}

function setearViajeComplemento(idComp){
	if (idComp){
		var link = "detalle_viaje.html?id=" + idComp;
		$("#tipo").append(" <a href='"+link+"'><small>(complemento)</small></a>")
	}
}

var mostrarVentanaParticipar = function(){
	cargarTramos(data.viaje.recorrido);
	$('#modal-participar').modal('show');
}

var cargarTramos = function(recorrido){
	var queryOrigen = "select[name=origenPasajero]";
	var queryDestino = "select[name=destinoPasajero]";
	$(queryOrigen+","+queryDestino).empty();
	for (var i=0; i<recorrido.length; i++){
		var valor = recorrido[i];
		var texto = (i+1)+" - "+valor;
		var op = createOp(valor,texto);
		if (i == 0){
			//agrego primer elemento solo a origen
			$(queryOrigen).append(op);
		} else if(i==recorrido.length-1){
			//agrego ultimo elemento solo a destino
			$(queryDestino).append(op);
		} else{
			//agrego todos los demas
			$(queryOrigen+","+queryDestino).append(op);
		}
	}
}

var createOp = function(valor,texto){
	var option = document.createElement("OPTION");
	option.text = texto;
	option.value = valor;
	return option;
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
			modalMessage('success',jsonData.msg);//podria incluir datos de conductor como mail o telefono
			data.loadData();
		}else{
			errorMessage(jsonData.msg);
		}
	}
	if (esTramoValido()){
		sendAjax(sendJson,onsuccess);
	};
}

var esTramoValido = function(){
	var origen = $("select[name=origenPasajero]").val();
	var destino = $("select[name=destinoPasajero]").val();
	var indexOrigen = data.viaje.recorrido.indexOf(origen);
	var indexDestino = data.viaje.recorrido.indexOf(destino);
	if (indexOrigen >= indexDestino){
		var msg = "Puntos de subida y bajada son incompatibles con el recorrido de este viaje";
		var panel = "#panel-error-tramo";
		var elemento = "select[name=origenPasajero],select[name=destinoPasajero]";
		customAlert(panel,elemento,msg);
		return false;
	}
	return true;
}

var seguirViaje = function(){
	var sendJson = {
		action: "seguir_viaje",
		id_viaje: data.viaje.id
	}
	var onsuccess = function(jsonData){
		if (jsonData.result){
			modalMessage('success',jsonData.msg);//opcional
			data.loadData();
		}else{
			errorMessage(jsonData.msg);
		}
	}
	sendAjax(sendJson,onsuccess);
}
var dejarDeSeguirViaje = function(){
	var sendJson = {
		action: "dejar_seguir_viaje",
		id_viaje: data.viaje.id
	}
	var onsuccess = function(jsonData){
		if (jsonData.result){
			modalMessage('success',jsonData.msg);//opcional
			data.loadData();
		}else{
			errorMessage(jsonData.msg);
		}
	}
	sendAjax(sendJson,onsuccess);
}

var cancelarParticipacion = function(){
	var modalName='warning';
	var confirmarCancelacion = function(){
		closeModal(modalName);
		var sendJson = {
			action: "cancelar_participacion",
			id_viaje: data.viaje.id,
		}
		var onsuccess = function(jsonData){
			if (jsonData.result){
				data.loadData();
			}else{
				errorMessage(jsonData.msg);
			}
		}
		sendAjax(sendJson,onsuccess);
	}
	var msg = "Al presionar en 'Confirmar Cancelación' usted será sancionado"
		+" y podría perder su cuenta temporalmente."
	var btn = document.createElement("BUTTON");       
	btn.className="btn btn-danger dinamico";
	btn.innerHTML = 'Confirmar cancelación';
	btn.name = "confirmarCancelacion";
	btn.onclick=confirmarCancelacion;
	modalButton(modalName,btn);
	modalMessage(modalName,msg);
}
var modificarViaje = function(){
	window.open("modificar_viaje.html?id="+data.viaje.id,"_blank");
}
var cancelarViaje = function(){
	var modalName='warning';
	var confirmarCancelacion = function(){
		closeModal(modalName);
		var sendJson = {
			action: "cancelar_viaje",
			id_viaje: data.viaje.id
		}
		var onsuccess = function(jsonData){
			if (jsonData.result){
				window.open("home.html","_blank");
			}else{
				errorMessage(jsonData.msg);
			}
		}
		sendAjax(sendJson,onsuccess);
	}
	var msg = "Al presionar en 'Confirmar Cancelación' usted podría recibir una sanción"
		+" en caso de que existieran pasajeros inscriptos al viaje."
	var btn = document.createElement("BUTTON");       
	btn.className="btn btn-danger dinamico";
	btn.innerHTML = 'Confirmar cancelación';
	btn.name = "confirmarCancelacion";
	btn.onclick=confirmarCancelacion;
	modalButton(modalName,btn);
	modalMessage(modalName,msg);
}

var verPostulantes = function(){
	// si hiciera un modal
	/*
	var sendJson = {
		action: "ver_postulantes",
		id_viaje: data.viaje.id
	}
	var onsuccess = function(jsonData){
		if (jsonData.result){
			//a completar
		}else{
			errorMessage(jsonData.msg);
		}
	}
	sendAjax(sendJson,onsuccess);
	*/
	
	//si lo redirijo a listado_postulantes.html
	window.open("listado_postulantes.html?id="+data.viaje.id,"_blank");
}

var calificar = function(){
	console.log("calificar");
	window.open("calificar.html?id="+data.viaje.id,"_blank");
}

var viajeFinalizado = function(){
	console.log("viaje finalizado");
	var sendJson = {
		action: "finalizar_viaje",
		id_viaje: data.viaje.id
	}
	var onsuccess = function(jsonData){
		if (jsonData.result){
			data.loadData();
			calificar();
		}else{
			errorMessage(jsonData.msg);
		}
	}
	sendAjax(sendJson,onsuccess);
}

var customAlert = function(panel,elemento,msg){
	$(panel).append(generateAlert(msg));
	
	$(elemento).change(function(){
		$(panel).empty();
	});
}

var estadoString = function (caracter) {
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

var generateAlert = function(msg){
	return html = '<div class=\"alert alert-danger\" role=\"alert\">'
		+'<span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>'
		+'<span class=\"sr-only\">Error:</span> '
		+msg+'</div>';
}

//MODALS
//eliminar elementos de modal que fueron generados dinamicamente
$(document).on('hide.bs.modal', function (e) {
  $(".dinamico").each(function(){
	$(this).remove();
  });
});

var errorMessage = function (textMsg) {
	$('#error-message').text(textMsg);
	$('#modal-error').modal('show');
}
var modalMessage = function (modalName,textMsg,titleMsg) {
	$('#'+modalName+'-message').text(textMsg);
	$('#modal-'+modalName).modal('show');
}
var modalButton = function(modalName,btn){
	//$('#modal-'+modalName+' button[name='+btn.name+']').remove(); //si ya existe, elimino el elemento antes de crearlo
	$('#modal-'+modalName+' .modal-footer').append(btn);
}
var closeModal = function (name) {
	$('#modal-' + name).modal('hide');
}


// funciones robadas

function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}