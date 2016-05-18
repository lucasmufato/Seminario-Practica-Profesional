var data = {};
data.viaje = {};
data.viaje.id = getUrlVars()["id"];
data.conductor = {};
data.vehiculo = {};
data.localidades = [];
data.comentarios = {};
data.usuario_logueado = {};

var sendAjax = function(sendData,callback){
	console.log("mando: ",sendData);
	$.ajax({
		url: '/viajes', 
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
}


data.loadData = function() {
	var sendData = {
		entity: "viaje",
		action: "detalle",
		"id_viaje": data.viaje.id
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			data.viaje = jsonData.viaje;
			data.conductor = jsonData.conductor;
			data.vehiculo = jsonData.vehiculo;
			data.localidades = jsonData.localidades;
			data.comentarios = jsonData.comentarios;
			data.usuario_logueado = jsonData.usuario_logueado;
			configurarUi();
			cargarViaje();
			cargarVehiculo();
			//cargarComentarios();
			cargarConductor();
			cargarRutaEnMapa();
			
			
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;
		}
	}
	
	//simular(sendData);
	
	sendAjax(sendData,onsuccess);
}

initUI = function() {
	data.loadData();	
	loadMap();

};
window.onload=initUI;

////carga de mapa///////

function loadMap() {
  var script = document.createElement("script");
  script.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyCu2P6zmQmOyESf872DSdZgYam9PMJnzwg&callback=initMap";
  document.body.appendChild(script);
}

mapData = {
	marcadores: []
}
initMap = function() {
	mapData.map = new google.maps.Map(document.getElementById('mapa'), {
		center: {lat: -34.5774135, lng: -59.0909557},
		drawingControl: false,
		zoom: 16
	});

	mapData.directionsService = new google.maps.DirectionsService();
	mapData.directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});
	mapData.directionsDisplay.setMap (mapData.map);
	cargarRutaEnMapa();
}

var cargarRutaEnMapa = function(){
	if(data.viaje.recorrido == undefined) {
		// Esto puede suceder si el mapa se carga antes que los datos del viaje
		return null;
	}
	mapData.marcadores = [];
	var i=1; //para que label de los marcadores muestre secuencia
	data.viaje.recorrido.forEach(function(id){
		var item = localidadPorId(id);
		
		mapData.marcadores.push(new google.maps.Marker({
				position: {
					lat: parseFloat(item.lat),
					lng: parseFloat(item.lng)
				},
				map: mapData.map,
				title: item.nombre,
				label: i.toString()
			}));
		i++;
	});

	pedirRuta();
}

var pedirRuta = function () {
	var origen = mapData.marcadores[0];
	var destino = mapData.marcadores[mapData.marcadores.length-1];

	var solicitud = {
		origin: origen.position,
		destination: destino.position,
		travelMode:google.maps.TravelMode.DRIVING,
		waypoints: listarPuntosIntermedios()
	};

  mapData.directionsService.route(solicitud, function(result, status) {
	console.log ("Se ha recibido respuesta");
	DEBUGresult=result;
    if (status == google.maps.DirectionsStatus.OK) {
      mapData.directionsDisplay.setDirections(result);
    }
  });
}

var listarPuntosIntermedios = function () {
	var puntos = [];
	for (i=1; i<mapData.marcadores.length-1; i++) {
		puntos.push({location:mapData.marcadores[i].position});
	}
	return puntos;
}

////fin carga mapa///

var simular = function(json){
	console.log("id: ",json.id);
	data.viaje = {
		id: data.viaje.id ,
		nombre_amigable: "Un alto viaje",
		estado: "no_iniciado",
		tipo: "ida",
		id_viaje_complemento: "4",
		origen: "324",
		destino: "880",
		fecha_inicio: "12/09/2016",
		hora: "20:30",
		precio: "30",
		participantes: "1",
		recorrido: ["324", "112","880"]
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
	data.localidades = [{
		id: "324",
		nombre: "Lujan",
		lat: "-34.5703",
		lng: "-59.105" 
	},{
		id: "112",
		nombre: "Rodriguez",
		lat: "-34.6084",
		lng: "-58.9525" 
	},{
		id: "880",
		nombre: "Moreno",
		lat: "-34.634",
		lng: "-58.7914" 
	}];
	data.usuario_logueado = {
		es_conductor: false,
		es_pasajero: false,
		es_seguidor: false,
		ha_calificado: false
	};		
	configurarUi();
	cargarViaje();
	cargarConductor();
	cargarVehiculo();
	cargarRutaEnMapa();
}

var configurarUi = function(){

	var esConductor = data.usuario_logueado.es_conductor;
	var esPasajero = data.usuario_logueado.es_pasajero;
	var esSeguidor = data.usuario_logueado.es_seguidor;
	var haCalificado = data.usuario_logueado.ha_calificado;

	var estado = estadoString(data.viaje.estado);
	
	if (esPasajero || esConductor){
		$("#botonera-cliente").hide();
		if (esConductor){
			$("#botonera-conductor").show();
			$("#botonera-pasajero").hide();
			
			//Boton finalizar solo si esta en iniciado
			if (estado == "Iniciado"){
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
	if (estado=="Cancelado"){
		$("#botonera-conductor").hide();
		$("#botonera-pasajero").hide();
		$("#botonera-cliente").hide();
		$("#btnCalificar").hide();
	} else if(estado == "Iniciado"){
		
	} else if(estado == "No iniciado"){ 
		$("#btnCalificar").hide();
	} else if(estado == "Finalizado"){
		$("#botonera-conductor").hide();
		$("#botonera-pasajero").hide();
		$("#botonera-cliente").hide();
	}
}

var cargarVehiculo = function(){
	$("#panel-foto-vehiculo a").attr('href',data.vehiculo.foto || "/img/perfil/sin_imagen.jpg");
	$("#panel-foto-vehiculo img").attr('src',data.vehiculo.foto || "/img/perfil/sin_imagen.jpg");
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
	$("#origen").text(localidadNombre (data.viaje.origen));
	$("#destino").text(localidadNombre (data.viaje.destino));
	$("#fecha").text(data.viaje.fecha_inicio);
	$("#hora").text(data.viaje.hora);
	$("#precio").text("$"+data.viaje.precio);
	data.viaje.recorrido.forEach(function(elem){
		$("#recorrido").append('<li>'+localidadNombre(elem)+'</li>');
	});

}

var cargarConductor = function(){
	$("#panel-foto-conductor a").attr('href',data.conductor.foto || "/img/perfil/default.png");
	$("#panel-foto-conductor img").attr('src',data.conductor.foto || "/img/perfil/default.png");
	$("#panel-foto-registro a").attr('href',data.conductor.foto_registro || "/img/perfil/sin_registro.jpg");
	$("#panel-foto-registro img").attr('src',data.conductor.foto_registro || "/img/perfil/sin_registro.jpg");
	$("#reputacion").text(reputacionStars(data.conductor.reputacion));;
	$("#nombreConductor").text(data.conductor.nombre_usuario);
	$("#nombreConductor").attr('href',"/perfil.html?usuario="+data.conductor.nombre_usuario);
}

function setearViajeComplemento(idComp){
	if (idComp){
		var link = "/detalle_viaje.html?id=" + idComp;
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
		var texto = (i+1)+" - "+localidadNombre(recorrido[i]);
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
	window.open("/modificar_viaje.html?id="+data.viaje.id,"_blank");
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
				window.open("/home.html","_blank");
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
	window.open("/listado_postulantes.html?id="+data.viaje.id,"_blank");
}

var calificar = function(){
	console.log("calificar");
	window.open("/calificar.html?id="+data.viaje.id,"_blank");
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
var localidadPorId = function(id){
	var l;
	data.localidades.forEach(function(elem){
		if (elem.id == id){
			l = elem;
		}
	});
	return l;
}
var localidadNombre = function(id){
	var nombre = "";
	data.localidades.forEach(function(elem){
		if (elem.id == id){
			nombre = elem.nombre;
		}
	});
	return nombre;
}

var estadoString = function (caracter) {
	switch (caracter) {
		case 'finalizado': return "Finalizado";
		case 'no_iniciado': return "No iniciado";
		case 'iniciado': return "Iniciado";
		case 'cancelado': return "Cancelado";
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
	if (titleMsg){
		$('#modal-'+modalName +" .dialog-title").text(titleMsg);
	}
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