var idViaje = getUrlVars()["id"];
var postulantes = [];

var loadData = function() {

	var sendData = {
		action: "ver_postulantes",
		"id": idViaje
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			postulantes = jsonData.postulantes;
			cargarPostulantes();
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;// si no es conductor de este viaje, acceso denegado
		}
	}
	simular();
	
	sendAjax(sendData,onsuccess);
}

window.onload=loadData;

var simular = function(){
	postulantes = [{
		nombre_usuario: "Carolo4",
		foto:"upload/foto.jpg",
		origen: "San Andres de Giles, Buenos Aires, Argentina",
		destino: "Carmen de areco, Buenos Aires, Argentina",
		apellido: "Perez",
		nombres: "Carolo",
		telefono: "421154",
		mail: "carolo4@gmail.com"
	},{
		nombre_usuario: "KarinaK100",
		foto:"upload/foto.jpg",
		origen: "Navarro, Buenos Aires, Argentina",
		destino: "Rosario, Santa Fe, Argentina",
		apellido: "Krenz",
		nombres: "Karina",
		telefono: "497673",
		mail: "Karina234123@outlook.com"
	},{
		nombre_usuario: "RodolfoU",
		foto:"upload/foto.jpg",
		origen: "Rosario, Santa Fe, Argentina",
		destino: "Montevideo, Uruguay",
		apellido: "Uber",
		nombres: "Rodolfo",
		telefono: "3434 15421154",
		mail: "rodolfoU@hotmail.com"
	}];
	cargarPostulantes();
}

var cargarPostulantes = function(){
	
}

var sendAjax = function(sendData,callback){
	console.log("mando: ",sendData);
	/*
	$.ajax({
		url: '/viaje', 
		dataType: 'json',
		method: 'POST',
		data: sendData,
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			callback(jsonData);
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
	*/
}



function getUrlVars() {
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}