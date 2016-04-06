ui = {};
ui.currentTab = 'personas';
ui.selectedId = null;

data={};

data.personas=[];
data.usuarios=[];
data.roles=[];



data.loadData = function() {
	$.ajax({
		url: '/users',
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			data.personas = jsonData.personas;
			data.usuarios = jsonData.usuarios;
			data.roles = jsonData.roles;
			ui.updatePersonasTable();
			ui.updateUsuariosTable();
			ui.updateRolesTable();
			ui.updatePersonasSelect();
		},
		error: function (er1, err2, err3) {
			window.alert (err3);
			document.body.innerHTML = er1;
		}
	});
}

Array.prototype.getById = function (id) {
	var pos = 0;
	var found = null;
	var retval = null;
	while (found == null && pos < this.length) {
		if (this[pos].id == id) {
			retval = this[pos];
		}
		pos++;
	}
	return retval;
}

ui.sendPersonaForm = function () {
	var sendData = {};
	sendData.entity = 'persona';
	sendData.id_persona = $('#formPersona input[name=id]').val() ;
	sendData.action = (sendData.id == -1)? 'new': 'edit';
	sendData.apellidos = $('#formPersona input[name=apellidos]').val()
	sendData.nombres= $('#formPersona input[name=nombres]').val()
	sendData.tipo_doc= $('#formPersona select[name=tipo_doc]').val()
	sendData.nro_doc= $('#formPersona input[name=nro_doc]').val()
	sendData.fecha_nacimiento= $('#formPersona input[name=fecha_nacimiento]').val()
	sendData.domicilio= $('#formPersona input[name=domicilio]').val()
	sendData.telefono= $('#formPersona input[name=telefono]').val()
	sendData.descripcion= $('#formPersona textarea[name=descripcion]').val()
	sendData.estado= $('#formPersona select[name=estado]').val()

	aux.sendForm(sendData)

	$('#formPersona').hide();
}

ui.sendUsuarioForm = function() {
	var sendData = {};
	sendData.entity = 'usuario';
	sendData.action = (sendData.id == -1)? 'new': 'edit';
	sendData.id_usuario = $('#formUsuario input[name=id]').val();
	sendData.id_persona = $('#formUsuario input[name=id_persona]').val();
	sendData.nombre_usuario= $('#formUsuario input[name=nombre_usuario]').val();
	sendData.password = $('#formUsuario input[name=password]').val();
	sendData.email = $('#formUsuario input[name=email]').val();
	sendData.descripcion = $('#formUsuario textarea[name=descripcion]').val();
	sendData.estado = $('#formUsuario select[name=estado]').val();
	aux.sendForm(sendData)

	$('#formUsuario').hide();
}

ui.sendRolForm = function() {
	var sendData = {};
	sendData.entity = 'rol';
	sendData.action = (sendData.id == -1)? 'new': 'edit';
	sendData.id_rol = $('#formRol input[name=id]').val();
	sendData.nombre = $('#formRol input[name=nombre]').val();
	sendData.nombre_amigable = $('#formRol input[name=nombre_amigable]').val();
	sendData.descripcion= $('#formRol textarea[name=descripcion]').val()
	sendData.estado = $('#formRol select[name=estado]').val();
	aux.sendForm(sendData);

	$('#formRol').hide();
}

ui.requestPersonaDeletion = function() {
	var sendData = {};
	sendData.entity = 'persona';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData);
}

ui.requestUsuarioDeletion = function() {
	var sendData = {};
	sendData.entity = 'usuario';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData);
}

ui.requestRolDeletion = function() {
	var sendData = {};
	sendData.entity = 'rol';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData);
}

initUI = function() {
	data.loadData();
};



ui.activatePersonasTab = function () {
	ui.currentTab = 'personas';
	$('#usuariosTable').hide();
	$('#rolesTable').hide();
	$('#personasTable').show();
};


ui.activateUsuariosTab = function () {
	ui.currentTab = 'usuarios';
	$('#usuariosTable').show();
	$('#rolesTable').hide();
	$('#personasTable').hide();
}

ui.activateRolesTab = function () {
	ui.currentTab = 'roles';
	$('#usuariosTable').hide();
	$('#rolesTable').show();
	$('#personasTable').hide();
};

ui.newButtonPressed = function () {
	switch (ui.currentTab) {
		case 'personas':
			ui.showNewPersonaForm();
			break;
		case 'usuarios':
			ui.showNewUsuarioForm();
			break;
		case 'roles':
			ui.showNewRolForm();
			break;
	}
};

ui.editButtonPressed = function () {
	switch (ui.currentTab) {
		case 'personas':
			ui.showEditPersonaForm();
			break;
		case 'usuarios':
			ui.showEditUsuarioForm();
			break;
		case 'roles':
			ui.showEditRolForm();
			break;
	}
};

ui.deleteButtonPressed = function () {
	switch (ui.currentTab) {
		case 'personas':
			ui.requestPersonaDeletion();
			break;
		case 'usuarios':
			ui.requestUsuarioDeletion();
			break;
		case 'roles':
			ui.requestRolDeletion();
			break;
	}
};

ui.updatePersonasSelect = function() {
	var select = $('#formUsuario select[name=id_persona]')[0];
	var newOpt;

	select.textContent = '';
	data.personas.forEach (function (persona) {
		if (persona.estado) {
			newOpt = document.createElement ('OPTION');
			newOpt.value = persona.id
			newOpt.textContent = persona.nombres;
			select.appendChild (newOpt);
		}
	});
}

ui.showNewPersonaForm = function () {
	$('#formPersona input[name=id]').hide()
	$('#formPersona label[for=id]').hide()
	$('#formPersonaTitle').html('Nueva Persona');
	$('#formPersona').show();
	$('#formPersona input[name=id]').val('-1');
	$('#formPersona input[name=apellidos]').val('');
	$('#formPersona input[name=nombres]').val('');
	$('#formPersona select[name=tipo_doc]').val(1);
	$('#formPersona input[name=nro_doc]').val('');
	$('#formPersona input[name=fecha_nacimiento]').val('1990-01-01');
	$('#formPersona input[name=domicilio]').val('');
	$('#formPersona input[name=telefono]').val('');
	$('#formPersona textarea[name=descripcion]').val('');
	$('#formPersona select[name=estado]').val(1);
	$('#formPersonaUsuario select[name=usuarios]').html('');
};

ui.showNewUsuarioForm = function () {
	$('#formUsuario').show();
	$('#formUsuario input[name=id]').hide();
	$('#formUsuario label[for=id]').hide();
	$('#formUsuario input[name=id]').val('-1');
	$('#formUsuario input[name=id_persona]').val('');
	$('#formUsuario input[name=nombre_usuario]').val('');
	$('#formUsuario input[name=password]').val('');
	$('#formUsuario input[name=email]').val('');
	$('#formUsuario textarea[name=descripcion]').val('');
	$('#formUsuario select[name=estado]').val(1);
};

ui.showNewRolForm = function () {
	$('#formRol').show();
	$('#formRol input[name=id]').hide();
	$('#formRol label[for=id]').hide();
	$('#formRol input[name=id]').val(-1);
	$('#formRol input[name=nombre]').val('');
	$('#formRol input[name=nombre_amigable]').val('');
	$('#formRol textarea[name=descripcion]').val('');
	$('#formRol input[name=estado]').val(1);
};

ui.showEditPersonaForm = function () {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formPersonaTitle').html('Modificar Persona');
	$('#formPersona').show();
	$('#formPersona input[name=id]').show();
	$('#formPersona label[for=id]').show();
	$('#formPersona input[name=id]').val(selected.id);
	$('#formPersona input[name=id_persona]').val(selected.id_persona);
	$('#formPersona input[name=apellidos]').val(selected.apellidos);
	$('#formPersona input[name=nombres]').val(selected.nombres);
	$('#formPersona select[name=tipo_doc]').val(selected.tipo_doc);
	$('#formPersona input[name=nro_doc]').val(selected.nro_doc);
	$('#formPersona input[name=fecha_nacimiento]').val(selected.fecha_nacimiento);
	$('#formPersona input[name=domicilio]').val(selected.domicilio);
	$('#formPersona input[name=telefono]').val(selected.telefono);
	$('#formPersona textarea[name=descripcion]').val(selected.descripcion);
	$('#formPersona select[name=estado]').val((selected.estado)?'1':'0');
	$('#formPersonaUsuario select[name=usuarios]').html('');

	var newOption;
	var usuario;
	var usuariosSelect = $('#formPersonaUsuario select[name=usuarios]')[0];

	data.personas.getById (selected.id).usuarios.forEach(function (idUsuario) {
		usuario = data.usuarios.getById(idUsuario);
		newOption = document.createElement ('OPTION');
		newOption.value = idUsuario;
		newOption.textContent = usuario.nombre_usuario;
		usuariosSelect.appendChild (newOption);
	});
};

ui.showEditUsuarioForm = function() {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formUsuario').show();
	$('#formUsuario input[name=id]').hide();
	$('#formUsuario label[for=id]').hide();
	$('#formUsuario input[name=id]').val(selected.id);
	$('#formUsuario select[name=id_persona]').val(selected.id_persona);
	$('#formUsuario input[name=nombre_usuario]').val(selected.nombre_usuario);
	$('#formUsuario input[name=password]').val(selected.password);
	$('#formUsuario input[name=email]').val(selected.email);
	$('#formUsuario textarea[name=descripcion]').val(selected.descripcion);
	$('#formUsuario select[name=estado]').val((selected.estado)?'1':'0');
}

ui.showEditRolForm = function() {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formRol').show();
	$('#formRol input[name=id]').show();
	$('#formRol label[for=id]').show();
	$('#formRol input[name=id]').val(selected.id);
	$('#formRol input[name=nombre]').val(selected.nombre);
	$('#formRol input[name=nombre_amigable]').val(selected.nombre_amigable);
	$('#formRol textarea[name=descripcion]').val(selected.descripcion);
	$('#formRol select[name=estado]').val((selected.estado)?'1':'0');
}

ui.closeForms = function () {
	$('#formPersona').hide();
	$('#formUsuario').hide();
	$('#formRol').hide();
}


ui.updatePersonasTable = function () {
	var tbody = $('#personasTable tbody')[0];
	var tr;

	tbody.innerHTML = '';
	data.personas.forEach(function (elem) {
		tr = document.createElement ('TR');
		tr.appendChild (aux.td (elem.id));
		tr.appendChild (aux.td (elem.apellidos));
		tr.appendChild (aux.td (elem.nombres));
		tr.appendChild (aux.td (aux.tipoDoc(elem.tipo_doc)));
		tr.appendChild (aux.td (elem.nro_doc));
		tr.appendChild (aux.td (elem.fecha_nacimiento));
		tr.appendChild (aux.td (elem.domicilio));
		tr.appendChild (aux.td (elem.telefono));
		tr.appendChild (aux.td (elem.estado? 'Activo': 'Inactivo'));
		var thistr = tr;
		tr.onclick = function () {
			ui.selectedId = elem.id;
			aux.clearSelectedRow (tbody);
			thistr.className='selectedRow';
		}
		tbody.appendChild(tr);
	});
	
}

ui.updateUsuariosTable = function () {
	var tbody = $('#usuariosTable tbody')[0];
	tbody.innerHTML = '';
	data.usuarios.forEach(function (elem) {
		tr = document.createElement ('TR');
		tr.appendChild (aux.td (elem.id));
		tr.appendChild (aux.td (elem.nombre_usuario));
		tr.appendChild (aux.td (elem.email));
		tr.appendChild (aux.td (elem.estado? 'Activo': 'Inactivo'));
		var thistr = tr;
		tr.onclick = function () {
			ui.selectedId = elem.id;
			aux.clearSelectedRow (tbody);
			thistr.className='selectedRow';
		}
		tbody.appendChild(tr);
	});
}

ui.updateRolesTable = function () {
	var tbody = $('#rolesTable tbody')[0];
	tbody.innerHTML = '';
	data.roles.forEach(function (elem) {
		tr = document.createElement ('TR');
		tr.appendChild (aux.td (elem.id));
		tr.appendChild (aux.td (elem.nombre));
		tr.appendChild (aux.td (elem.nombre_amigable));
		tr.appendChild (aux.td (elem.estado? 'Activo': 'Inactivo'));
		var thistr = tr;
		tr.onclick = function () {
			ui.selectedId = elem.id;
			aux.clearSelectedRow (tbody);
			thistr.className='selectedRow';
		}
		tbody.appendChild(tr);
	});
}

ui.getSelectedElement = function() {
	var elem, list, index, found;
	index = 0;
	found = false;
	elem = null;
	switch (ui.currentTab) {
		case 'personas':
			list = data.personas;
			break;
		case 'usuarios':
			list = data.usuarios;
			break;
		case 'roles':
			list = data.roles;
			break;
	}
	while (!found & index < list.length) {
		elem= list[index];
		if (list[index].id == ui.selectedId) {
			found=true;
			elem=list[index];
		}
		index++;
	}
	return elem;
}

/* Funciones auxiliares */
aux = {};
aux.td = function (text){
	var td = document.createElement('TD');
	td.appendChild(document.createTextNode(text));
	return td;
}

aux.tipoDoc = function(number) {
	switch (number) {
		case 1:
			return 'DNI';
		case 2:
			return 'LE';
		case 3:
			return 'LC';
		default:
			return number;
	}
}

aux.clearSelectedRow = function (tbody) {
	debugTBODY = tbody;
	var i = 0;

	while (i < tbody.childNodes.length) {
		tbody.childNodes[i].className = '';
		i++;
	}
}

aux.sendForm = function (sendData) {
	$.ajax({
		url: '/users',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if (jsonData.result) {
				data.loadData ();
			} else {
				window.alert ("Ocurrio un error");
			}
		},
		error: function (er1, err2, err3) {
			window.alert (err3);
			DEBUGerror = er1;
		}
	});
}
