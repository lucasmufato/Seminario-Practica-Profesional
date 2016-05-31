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
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

vc.ventana_mensaje = function (texto, titulo, tipo) {
	if (titulo == undefined) {
		titulo = "";
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