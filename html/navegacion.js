//(function () {

var toggleSidebar = function () {
	$('body').toggleClass("sidebar-on");
}

var navegacionInit = function () {
	var nav = document.createElement('NAV');
	$(nav).addClass('navbar navbar-fixed-top');
	$(nav).load('/navegacion.html #common-navbar');

	var sidebar = document.createElement('SIDEBAR');
	$(sidebar).load('/navegacion.html #common-sidebar');

	var modal = document.createElement('DIV');
	$(modal).load('/navegacion.html #common-modal');

	//cargo script de permisos
	$.ajax({
		url: "/permisos.js",
		dataType: "script",
		success: function () { permisosData.iniciarScriptPermisos();},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});

	$('body').prepend (sidebar);
	$('body').prepend (nav);
	$('body').append (modal);


	// Validacion de campos
	var campos = document.getElementsByClassName("vc-validar");
	for (var i=0; i<campos.length; i++) {
		campos[i].onchange = function () {
			var inv_msg = this.getAttribute("data-invalid-msg");
			if(!inv_msg) {
				inv_msg = "El dato ingresado no es valido";
			}
			if (this.checkValidity()) {
				vc.customAlertSuccess (this, "OK");
			} else {
				vc.customAlert (this, inv_msg);
			}
		}
	};
}

var oldFunc = window.onload;

if(oldFunc) {
	window.onload = function () {
		navegacionInit();
		oldFunc();
	}
} else {
		window.onload = navegacionInit;
}
//}) ();

/* FUNCIONES DE USO GENERAL */
vc = {};

vc.peticionAjax = function (url, datos, method, onsuccess) {
	if (!method) {
		method = 'POST';
	}

	var callbacksuccess = function (jsonData) {
		var tipo = null;
		var mensaje = null;
		var titulo = null;
		var sobreescrito = false;

		if (jsonData.msg != undefined) {
			mensaje = jsonData.msg;
		}

		if (jsonData.result != undefined) {
			if (jsonData.result) {
				tipo = 'success';
				titulo = 'Operacion exitosa';
				if (onsuccess != undefined) {
					sobreescrito = true;
				}
			} else {
				tipo = 'error';
				titulo = 'Error';
			}
		}

		if (jsonData.redirect != undefined) {
			document.title = "Redirigiendo...";
			window.setTimeout(function(){window.location = jsonData.redirect}, 1000);
		}

		if (sobreescrito) {
			onsuccess(jsonData);
		} else {
			vc.ventana_mensaje(mensaje, titulo, tipo);
		}
	}

	$.ajax({
		'url': url,
		'dataType': 'json',
		'method': method,
		'data': datos,
		'success': callbacksuccess,
		error: function (err1, err2, err3) {
			var htmlBody = err1.responseText.replace(/.*<body[^>]*>/i, "").replace(/<\/body[^>]*>.*/i, "");
			var modalBody =
			'<p>No se pudo realizar la acción solicitada. Ha ocurrido un error en el servidor.</p>'+
			'<div class="panel panel-danger">' +
			'	<div class="panel-heading" style="cursor:pointer" data-toggle="collapse" data-target="#vc-modal-debug-info">' +
			'		  <h3 class="panel-title">Ver informe de depuracion</h3>' +
			'	</div>' +
			'	<div class="panel-body collapse" id="vc-modal-debug-info">' +
			htmlBody +
			'	</div>' +
			'</div>';
			vc.ventana_mensaje(modalBody, err3, "error");
		}
	});
}

vc.ventana_mensaje = function (texto, titulo, tipo) {
	if (titulo == undefined) {
		titulo = "";
	}
	$("#common-modal").removeClass("error").removeClass("success");
	if(tipo == "error" || tipo == "success") {
		$("#common-modal").addClass(tipo);
	}
	$("#common-modal-body").html(texto);
	$("#common-modal-title").html(titulo);
	$("#common-modal").modal("show");
}

vc.customAlert = function(elemento,msg){
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

vc.customAlertSuccess = function(elemento){
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

vc.fechaAMD = function (fecha_dma) {
    if (fecha_dma.match (/(\d{1,2})\/(\d{1,2})\/(\d{2,})/)) {
		var anio = Number(fecha_dma.replace (/(\d{1,2})\/(\d{1,2})\/(\d{2,}).*/, "$3"));
		var mes = Number(fecha_dma.replace (/(\d{1,2})\/(\d{1,2})\/(\d{2,}).*/, "$2"));
		var dia = Number(fecha_dma.replace (/(\d{1,2})\/(\d{1,2})\/(\d{2,}).*/, "$1"));
		if (anio < 50) {
			   anio = 2000+Number(anio);
		} else if (anio < 100){
			   anio = 1900+Number(anio);
		}
		if (mes < 10) {mes = "0"+mes;}
		if (dia < 10) {dia = "0"+dia;}
		return fecha_dma.replace (/(\d{1,2})\/(\d{1,2})\/(\d{2,})/, anio+"-"+mes+"-"+dia);
    }
}

vc.toFechaLocal = function(fecha_string){
	var date = new Date(fecha_string)
	return date.toLocaleString();
}
