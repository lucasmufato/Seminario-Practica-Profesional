window.onload = function () {
	
	$('#anio').datetimepicker({
        viewMode: 'years',
        format: 'yyyy',
    	language: "es",
    	startView: 4,
    	minView: 4,
    	maxView: 4,
    	autoclose: true
	});

}

submitFormVehiculo = function () {
	// Falta foto
	var sendData = {
		entity: 'vehiculo',
		action: 'new',
		marca: $('#form-vehiculo input[name=marca]').val(),
		patente: $('#form-vehiculo input[name=patente]').val(),
		modelo: $('#form-vehiculo input[name=modelo]').val(),
		color: $('#form-vehiculo input[name=color]').val(),
		anio: $('#form-vehiculo input[name=anio]').val(),
		asientos: $('#form-vehiculo select[name=asientos]').val(),
		aire: $('#form-vehiculo select[name=aire]').val(),
		seguro: $('#form-vehiculo select[name=seguro]').val()
	}

	vc.peticionAjax('/viajes', sendData);

	return false;
}