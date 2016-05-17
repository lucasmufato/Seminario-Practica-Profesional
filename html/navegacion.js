//(function () {

var toggleSidebar = function () {
	$('body').toggleClass("sidebar-on");
}

var addNavegacion = function () {
	var nav = document.createElement('NAV');
	$(nav).addClass('navbar navbar-fixed-top');
	$(nav).load('navegacion.html #common-navbar');

	var sidebar = document.createElement('SIDEBAR');
	$(sidebar).load('navegacion.html #common-sidebar');

	var modal = document.createElement('DIV');
	$(modal).load('navegacion.html #common-modal');

	//$.getScript( "permisos.js");
	$('body').prepend (sidebar);
	$('body').prepend (nav);
	$('body').append (modal);
}

var oldFunc = window.onload;

if(oldFunc) {
	window.onload = function () {
		addNavegacion();
		oldFunc();
	}
} else {
		window.onload = addNavegacion;
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