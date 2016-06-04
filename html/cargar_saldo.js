window.onload=function() {
	cargar_saldo();
}

var cargar_saldo = function() {
	var sendData = {
		entity: "saldo",
		action: "consultar"
	};
	
	var onsuccess = function(recibido) {
		actualizarSaldo(recibido.saldo);
		$(".loadingScreen").fadeOut();
	}
	
	vc.peticionAjax("/comisiones", sendData, "GET", onsuccess);
}

var actualizarSaldo = function(saldo) {
	$("#saldo_actual").text("$ "+saldo.toFixed(2));
}