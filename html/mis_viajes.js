
var loadData = function() {

	var sendData = {
		action: "ver_mis_viajes",
		"id": "",
	}
	var onsuccess = function(jsonData){
		if(jsonData.result){
			$('.loadingScreen').fadeOut();
			
			
		} else if (jsonData.redirect != undefined) {
			window.location = jsonData.redirect;
		}
	}
	//simular();
	
	//sendAjax(sendData,onsuccess);
	
}

var initUI = function(){
	loadData();
	$('[data-toggle="tooltip"]').tooltip(); 
	
}

window.onload=initUI;