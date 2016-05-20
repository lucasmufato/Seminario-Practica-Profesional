window.onload= function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	$('#fechadesde, #fechahasta').datetimepicker({
        format: 'yyyy-mm-dd',
    	language: "es",
    	startView: 3,
    	minView: 2,
    	maxView: 2,    
		autoclose: true,
    	todayBtn: true,
		clearBtn: true,
	});
}
var generarReporte = function(){
	var data = {};
	data.id_desde = $("#reportForm input[name=iddesde]").val();
	data.id_hasta = $("#reportForm input[name=idhasta]").val();
	data.fecha_desde = $("#reportForm input[name=fechadesde]").val();
	data.fecha_hasta = $("#reportForm input[name=fechahasta]").val();
	data.precio_desde = $("#reportForm input[name=preciodesde]").val();
	data.precio_hasta = $("#reportForm input[name=preciohasta]").val();
	data.km_desde = $("#reportForm input[name=kmdesde]").val();
	data.km_hasta = $("#reportForm input[name=kmhasta]").val();
	data.pasajeros_desde = $("#reportForm input[name=pasajerosdesde]").val();
	data.pasajeros_hasta = $("#reportForm input[name=pasajeroshasta]").val();
	data.conductor = $("#reportForm input[name=conductor]").val();
	data.estado = $("#reportForm select[name=estado]").val();

	var sendData = {
		action: "reporte_viajes",
		data: data
	};	
	var onsuccess = function(jsonData){
		if (jsonData.relocate){
			window.open(jsonData.relocate,"_blank");
		}
	}
	vc.peticionAjax("/reportes",sendData,"POST",onsuccess);
}