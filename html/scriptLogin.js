

$(document).ready(function(){
  console.log("entro a scriptLogin.js");
  if (getCookie("nombre_usuario") != ""){
	$("#loginLink").hide();
  }else{
	$("#logoutLink").hide();
  };
})

function getCookie(nombreCookie) {
    var nombre = nombreCookie + "=";
    var propiedadesCookies = document.cookie.split(';');
    for(var i=0; i<propiedadesCookies.length; i++) {
        var c = propiedadesCookies[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(nombre) == 0) return c.substring(nombre.length,c.length);
    }
    return "";
}