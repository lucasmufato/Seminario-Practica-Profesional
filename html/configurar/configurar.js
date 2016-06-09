window.onload=function() {
	$('#panel_db').hide();
	$('#panel_progreso').hide();
	$('#panel_adminpass').hide();
	comprobar_configuracion();
}

comprobar_configuracion = function() {
	// Esta funcion deberia enviar una query al servidor para preguntarle si ya esta configurado
	// Si no esta configurado muestra el panel de configuracion
	// Si ya esta configurado muestra el cartel "ya esta configurado" y redirije al index
	$('.loadingScreen').fadeOut();
	paso1();
}

paso1 = function() {
	// Esta funcion tiene que mostrar el primer panel
	$('#panel_progreso').hide();
	$('#panel_adminpass').hide();
	$('#panel_db').show();
}

paso2 = function() {
	// Esta funcion tiene que mandar los datos ingresados en el formulario al servlet y mostrar el panel de progreso
	// El panel de progreso tiene que actualizarse cada cierto tiempo para mostrar los cambios
	
	$("#progressbar_instalacion").removeClass("progress-bar-success");
	$("#progressbar_instalacion").removeClass("progress-bar-danger");
	$("#progressbar_instalacion").removeClass("active");
	$("#progressbar_instalacion").css({width: "1%"});
	relojitos(0);

	var sendData = {
		host: $('input[name=db_host]').val(),
		port: $('input[name=db_port]').val(),
		username: $('input[name=db_username]').val(),
		password: $('input[name=db_password]').val(),
		dbname: $('input[name=db_dbname]').val(),
		mode: $('select[name=db_mode]').val()
	}


	$.ajax({
		url: '/dbconfig',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function() {
			$('#panel_db').hide();
			$('#panel_adminpass').hide();
			$('#panel_progreso').show();
			window.setTimeout(comprobar_estado, 1000);
		}
	});
	
}

paso3 = function() {
	// Esta funcion se ejecuta cuando se ha completado la instalación
	$('#panel_db').hide();
	$('#panel_progreso').hide();
	$('#panel_adminpass').show();
}

comprobar_estado = function() {
	// Esta funcion pregunta al servlet el estado actual del proceso y lo muestra en el panel de progreso
	$.ajax({
		url: '/dbconfig',
		method: 'GET',
		dataType: 'json',
		success: function(recv) {
			var recomprobar = true;
			var pb=$("#progressbar_instalacion");
			switch (recv.estado) {
				case "no_iniciado":
					pb.removeClass("progress-bar-success");
					pb.removeClass("progress-bar-danger");
					pb.removeClass("active");
					pb.css({width: "1%"});
					relojitos(0);
					break;
				case "trabajando":
					pb.removeClass("progress-bar-success");
					pb.removeClass("progress-bar-danger");
					pb.addClass("active");
					var porcentaje = 20*recv.step;
					relojitos(recv.step);
					pb.css({width: porcentaje+"%"});
					break;
				case "completo":
					recomprobar = false;
					pb.removeClass("progress-bar-danger");
					pb.addClass("progress-bar-success");
					pb.removeClass("active");
					pb.css({width: "100%"});
					relojitos(5);
					break;
				case "fallo":
					recomprobar = false;
					pb.removeClass("progress-bar-success");
					pb.addClass("progress-bar-danger");
					pb.removeClass("active");
					relojitos(recv.step, "cancelado");
					break;
			}
			if (recomprobar) {
				window.setTimeout(comprobar_estado, 1000);
			}
			
		}
	});
}

relojitos = function (step, err) {
	for (var i = 1; i<6; i++) {
		var item = $('#icon-step-'+i)[0];
		if(i<=step) {
			item.className="glyphicon glyphicon-ok";
		} else if(err) {
			item.className="glyphicon glyphicon-remove";
		} else {
			item.className="glyphicon glyphicon-hourglass";
		}
	}
}