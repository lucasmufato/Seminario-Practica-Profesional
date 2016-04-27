//(function () {

var toggleSidebar = function () {
	$('body').toggleClass("sidebar-on");
}

var addNavegacion = function () {
	var nav = document.createElement('NAV');
	$(nav).addClass('navbar navbar-fixed-top');
	$(nav).load('navegacion.html #common-navbar');

	var sidebar = document.createElement('SIDEBAR');
	$(sidebar).load('navegacion.html #common-sidebar');

	$('body').prepend (sidebar);
	$('body').prepend (nav);
}

var oldFunc = window.onload;

if(oldFunc) {
	window.onload = function () {
		addNavegacion();
		oldFunc();
	}
} else {
		window.onload = addNavegacion;
}
//}) ();
