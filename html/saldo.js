var movSaldo = [];

window.onload = function(){
  consultar_saldo();
  cargarMovSaldo();
}

var consultar_saldo = function() {
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

var cargarMovSaldo = function(){
  movsaldo = [{
    fecha:"22/04/1025",
    tipo_mov:"a",
    monto:"500"
  }];
  showMovSaldo();
}

var actualizarSaldo = function(saldo) {
	$("#saldo-actual").text("$ "+saldo.toFixed(2));
}

var showMovSaldo = function(){
  var tbody = $('#table-mov-saldo tbody')[0];
  var tr;


  tbody.innerHTML = '';

  if (!movSaldo || !movSaldo.length){
    var td;

    tr = document.createElement ('TR');
    td = document.createElement ('TD');
    td.setAttribute ('colspan', 3);
    td.textContent = "No hay resultados para la busqueda";
    td.className = "warning";

    tbody.appendChild (tr);
    tr.appendChild (td);
  }
  movSaldo.forEach(function (elem) {

      tr = document.createElement ('TR');
      tr.appendChild (getTd (elem.fecha));
      tr.appendChild (getTd (elem.tipo_mov));
      tr.appendChild (getTd (elem.monto));

      var thistr = tr;
      tr.onclick = function () {
        clearSelectedRow (tbody);
        $(thistr).addClass('info');
      }
      tbody.appendChild(tr);
  });

}

var getTd = function (text){
	var td = document.createElement('TD');
	td.appendChild(document.createTextNode(text));
	return td;
}

var clearSelectedRow = function (tbody) {
	debugTBODY = tbody;
	var i = 0;

	while (i < tbody.childNodes.length) {
		$(tbody.childNodes[i]).removeClass('info');
		i++;
	}
}
