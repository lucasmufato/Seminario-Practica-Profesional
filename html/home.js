initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover');
	$('input, select, textarea').addClass('form-control');
	$('label').addClass('control-label');
	/*-----------*/

	$('.loadingScreen').fadeOut(); 
};

$(document).ready(function(){
	initUI();
});
