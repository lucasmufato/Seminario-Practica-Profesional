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

	$("input[type='file']").change(function(){
		readURL(this);
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
		asientos: $('#form-vehiculo input[name=asientos]').val(),
		aire: $('#form-vehiculo select[name=aire]').val(),
		seguro: $('#form-vehiculo select[name=seguro]').val(),
		foto: $('#foto_vehiculo').attr("src")
	}

	var redirec = new function() {
		vc.ventana_mensaje ("Vehiculo creado correctamente<br>Redireccionando a mis vehiculos");
		window.setTimeout(function(){window.location="/mis_vehiculos.html"}, 1000);
	}

	vc.peticionAjax('/viajes', sendData, 'POST', redirec);

	return false;
}
//---------------------------- IMAGEN---------------------------------------//

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
//---------------------------------Fin Foto ------------------------------//
//---------------------------------Modal ------------------------------//

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

//---------------------------------FIN Modal ------------------------------//
