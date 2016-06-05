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
	var sendData = {
		host: $('input[name=db_host]').val(),
		port: $('input[name=db_port]').val(),
		username: $('input[name=db_username]').val(),
		password: $('input[name=db_password]').val(),
		dbname: $('input[name=db_dbname]').val(),
		mode: $('select[name=db_mode]').val()
	}

	$('#panel_db').hide();
	$('#panel_adminpass').hide();
	$('#panel_progreso').show();
	
	window.setTimeout(comprobar_estado, 1000);
}

paso3 = function() {
	// Esta funcion se ejecuta cuando se ha completado la instalación
	$('#panel_db').hide();
	$('#panel_progreso').hide();
	$('#panel_adminpass').show();
}

comprobar_estado = function() {
	// Esta funcion pregunta al servlet el estado actual del proceso y lo muestra en el panel de progreso
}