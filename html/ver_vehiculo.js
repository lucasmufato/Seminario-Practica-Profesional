//var idVehiculo = getUrlVars()["id"];
var clientes = [];
var vehiculo = {};

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
	vehiculo = {
			id: "1",
			marca: "ford",
			modelo: "focus",
			color: "#303F9F",
			anio: "2010",
			patente: "ifg 999",
			seguro: "S",
			aire: "S",
			cantidad_asientos: "4",
			cliente_vinculado: ["48"],
			foto: "http://www.coches.com/fotos_historicas/ford/Focus/med_ford_focus-2014_r3.jpg.pagespeed.ce.JW0wSPihZv.jpg",
			vehiculo_verificado: "S"
	}
	cargarVehiculo();
};


var sendAjax = function(sendData,callback){
	console.log("mando: ",sendData);
	/*$.ajax({
		url: '/vehiculo', 
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
	});*/
}

var loadData = function() {
	
	var sendData = {
		action: "ver_vehiculo"
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();	
			vehiculo = jsonData.vehiculo;
			cargarVehiculo();
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;
		}
	}

	simular();
	sendAjax(sendData,onsuccess);
	/*$.ajax({
		url: '/vehiculo', 
		dataType: 'json',
		method: 'GET',
		data: sendData,
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			onsuccess(jsonData);
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});*/
}

var initUI = function(){
	loadData();
	$('[data-toggle="tooltip"]').tooltip(); 
}

window.onload=initUI;

var cargarVehiculo = function(){
	//Limpio UI
	$("#panel-vehiculo").empty();

	if (vehiculo){
		vehiculo.foto = vehiculo.foto || "img/vehiculo/vehiculo.png";
	}
	var elemento = vehiculo;
	elemento.seguro = mostrarSiNo(vehiculo.seguro);
	elemento.aire = mostrarSiNo(vehiculo.aire);
	var template = $("#vehiculo-template").html();
	//var template = $("#panel-vehiculo").html();
	$("#panel-vehiculo").append(Mustache.render(template,elemento));
	new jscolor($('.jscolor')[0]);
	setearEventos();
}

function setearEventos(){
	$("input[type='file']").change(function(){
		readURL(this);
	});
	$('#foto_vehiculo').load(function() {
		var imageSrc = $(this).attr("src");
		if (imageSrc != vehiculo.foto){
			enviarFoto("vehiculo",imageSrc);
		}
	});

	$("#tablaVehiculo input[name=anio]").blur(validarCampoObligatorio);
	$("#tablaVehiculo input[name=color]").blur(validarCampoObligatorio);
	$("#tablaVehiculo input[name=seguro]").blur(validarSioNo);
	$("#tablaVehiculo input[name=aire_acondicionado]").blur(validarSioNo);
	$("#tablaVehiculo input[name=cantidad_asientos]").blur(validarAsientos);
}

var enviarFoto = function(atributo, src){
	var sendData = {
		id : vehiculo.id,
		action : "modificar_imagen"
	}
	if (atributo == "vehiculo"){
		sendData.foto = src;
	}
	var onsuccess = function(jsonData){
		if (jsonData.result){
			loadData();
		} 
	}
	sendAjax(sendData,onsuccess);
}
//-----------------------------------------------MODIFICAR--------------------------------------------------
var activarModificar = function(){
	$("#table-vehiculo input").attr("disabled",false);
	generarNuevosBotones();
}

var generarNuevosBotones = function(){
	var btnGuardar = "<button class='btn btn-success' onclick='modificarVehiculo();'>"
		+"<span class='glyphicon glyphicon-check'></span> Guardar"
		+"</button>";
	var btnCancelar = "<button class='btn btn-danger' onclick='cancelarModificar();'>"
		+"<span class='glyphicon glyphicon-remove'></span> Cancelar"
		+"</button>";
	var html = "<div class='btn-group'>"+btnCancelar+btnGuardar+"</div>";
	$("#botonera-modificar-vehiculo").html(html);
}

var cancelarModificar = function(){
	cargarVehiculo();
}

var modificarVehiculo = function(){
	var sendData = {
		action: "modificar_vehiculo",
		id: vehiculo.id,
		vehiculo:{}
	}
	sendData.vehiculo.anio = $("table input[name=anio]").val();
	sendData.vehiculo.color = $("table input[name=color]").val();
	sendData.vehiculo.cantidad_asientos = $("table input[name=cantidad_asientos]").val();
	sendData.vehiculo.seguro = siNoCaracter($("table input[name=seguro]").val());
	sendData.vehiculo.aire = siNoCaracter($("table input[name=aire_acondicionado]").val());

		//SIMULA----------
		vehiculo.anio = sendData.vehiculo.anio;
		vehiculo.color = sendData.vehiculo.color;
		vehiculo.cantidad_asientos = sendData.vehiculo.cantidad_asientos;
		vehiculo.seguro = sendData.vehiculo.seguro;
		vehiculo.aire = sendData.vehiculo.aire;
		cargarVehiculo();
		//-------------

	var onsuccess = function(jsonData){
		if (jsonData.result){
			loadData();
		} else{
			modalMessage("error", jsonData.msg, "Modificar Vehículo");
		}
	}
	sendAjax(sendData,onsuccess);
}

var eliminarVehiculo = function(){
	var modalName='warning';
	var confirmar = function(){
		closeModal(modalName);
		var sendJson = {
			action: "eliminar_vehiculo",
			id: vehiculo.id
		}
		var onsuccess = function(jsonData){
			if (jsonData.result){
				window.location = jsonData.redirect;
			}else{
				modalMessage("error",jsonData.msg,"Eliminar Vehículo");
			}
		}
		sendAjax(sendJson,onsuccess);
	}
	var msg = "¿Esta seguro que desea eliminar el vehículo? Esta acción no puede deshacerse";
	var title= "Eliminar Vehículo";
	var btn = document.createElement("BUTTON");       
	btn.className="btn btn-danger dinamico";
	btn.innerHTML = "<span class='glyphicon glyphicon-tint'></span>"+" Confirmar";
	btn.name = "confirmar";
	btn.onclick=confirmar;
	modalButton(modalName,btn);
	modalMessage(modalName,msg,title);
}

var mostrarSiNo = function(caracter) {
	switch(caracter) {
		case 'S': return "Sí";
		case 'N': return "No";
		case 'Sí': return "Sí";
		case 'No': return "No";
	}
}
//-----------------------------------------------VALIDACIONES------------------------------------------------
var validarCampoObligatorio = function(){
	var input = $(this);
	if (input.val().length==0){
		customAlert(input, "Completar campo obligatorio");
	}else{
		customAlertSuccess(input);
	}
}

var validarSioNo = function(){
	var input = $(this);
	var valor = input.val().toLowerCase();
	if (valor.length > 0){
		if (valor == "sí" || valor == "no" || valor == "si"){
			customAlertSuccess(input);
		}else{
			customAlert(input,"Valores válidos son: 'Sí' y 'No'");
		}
	}else{
		customAlert(input, "Completar campo obligatorio");
	}
}

var validarAsientos = function(){
	var input = $(this);
	var valor = input.val().toLowerCase();
	if (valor.length > 0){
		if (valor == "1" || valor == "2" || valor == "3" || valor == "4" || valor == "5"){
			customAlertSuccess(input);
		}else{
			customAlert(input,"Valores válidos son: '1', '2', '3', '4' y '5'");
		}
	}else{
		customAlert(input, "Completar campo obligatorio");
	}
}

var customAlert = function(elemento,msg){
	var mensaje = msg;
	var popoverTemplate = ['<div class="popover-error popover">',
        '<div class="arrow arrow-error"></div>',
        '<div class="popover-content popover-content-error">',
        '</div>',
        '</div>'].join('');
	$(elemento).popover({
		animation: true,
		trigger: 'manual',
		placement: 'top',
		template: popoverTemplate,
		content: function() {
			return mensaje;
		}
	});
	$(elemento).popover("show");
	$(elemento).closest("tr").removeClass('has-success').addClass('has-error');
	
	$(elemento).focus(function(){
		$(elemento).popover("destroy");
		$(elemento).closest("tr").removeClass('has-error')
	});
}

var customAlertSuccess = function(elemento){
	var popoverTemplate = ['<div class="popover-success popover">',
        '<div class="arrow arrow-success"></div>',
        '<div class="popover-content popover-content-success">',
        '</div>',
        '</div>'].join('');
	$(elemento).popover({
		animation: true,
		trigger: 'manual',
		placement: 'left',
		template: popoverTemplate,
		html:true,
		content: function() {
			return "<span class='glyphicon glyphicon-ok'></span>";
		}
	});
	$(elemento).popover("show");
	$(elemento).closest("tr").removeClass('has-error').addClass('has-success');

	$(elemento).focus(function(){
		$(elemento).popover("destroy");
		$(elemento).closest("tr").removeClass('has-success')
	});
}

var siNoCaracter = function(entrada){
	switch (entrada.toLowerCase()) {
		case 'si': return "S";
		case 'no': return "N";
		case 'sí': return "S";
		case '': return "";
		case null: return "";
		default: return "";
	}
}

var imagenValida = function(file){
	var maxTam = 1500000; // tamano maximo 1.5MB
	if (file.size >= maxTam){
		modalMessage("error", "Archivo es demasiado grande", "Modificar Imagen");
		return false;
	}
    if (file.type.indexOf("image") == -1){
		modalMessage("error", "Debe seleccionar una imagen", "Modificar Imagen");
		return false;
	}	
	return true;
}

function readURL(input) {
	var id = $(input).attr("name");
    if (input.files && input.files[0] && imagenValida(input.files[0])) {
		var reader = new FileReader();

		reader.onload = function (e) {
			$("#"+id).attr('src', e.target.result).show();
		}

		reader.readAsDataURL(input.files[0]);
    }
}

// modal

$(document).on('hide.bs.modal', function (e) {
  $(".dinamico").each(function(){
	$(this).remove();
  });
});

var modalMessage = function (modalName,textMsg,titleMsg) {
	$('#'+modalName+'-message').text(textMsg);
	if (titleMsg){
		$('#modal-'+modalName +" .dialog-title").text(titleMsg);
	}
	$('#modal-'+modalName).modal('show');
}
var closeModal = function (name) {
	$('#modal-' + name).modal('hide');
}
var modalButton = function(modalName,btn){
	$('#modal-'+modalName+' .modal-footer').append(btn);
}
// funciones robadas // ladron que roba a ladron


function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}