var imprimir = function(){
	var sendData = {
		action: "reporte_viajes"
	};
	var onsuccess = function(jsonData){
		if (jsonData.relocate){
			window.open(jsonData.relocate,"_blank");
		}
	}
	vc.peticionAjax("/reportes",sendData,"POST",onsuccess);
}