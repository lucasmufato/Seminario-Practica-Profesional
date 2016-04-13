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
			data.permisos = jsonData.permisos;
			ui.updatePersonasTable();
			ui.updateUsuariosTable();
			ui.updateRolesTable();
			ui.updatePermisosTable();
			ui.updatePersonasSelect();
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
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
	sendData.id_persona = $('#formPersona input[name=id]').val();
	sendData.action = (sendData.id_persona == -1)? 'new': 'edit';
	sendData.apellidos = $('#formPersona input[name=apellidos]').val() || null;
	sendData.nombres= $('#formPersona input[name=nombres]').val() || null;
	sendData.tipo_doc= $('#formPersona select[name=tipo_doc]').val() || null;
	sendData.nro_doc= $('#formPersona input[name=nro_doc]').val() || null;
	sendData.fecha_nacimiento= $('#formPersona input[name=fecha_nacimiento]').val() || null;
	sendData.sexo= $('#formPersona select[name=sexo]').val() || null;
	sendData.domicilio= $('#formPersona input[name=domicilio]').val() || null;
	sendData.foto= $('#formPersona input[name=foto]').val() || null;
	sendData.telefono= $('#formPersona input[name=telefono]').val() || null;
	sendData.descripcion= $('#formPersona textarea[name=descripcion]').val() || null;
	sendData.estado= $('#formPersona select[name=estado]').val() || null;
	sendData.foto_registro= $('#formPersona input[name=foto_registro]').val() || null;

	aux.sendForm(sendData, data.loadData)

	$('#modalPersona').modal('hide');
}

ui.sendUsuarioForm = function() {
	var sendData = {};
	sendData.entity = 'usuario';
	sendData.id_usuario = $('#formUsuario input[name=id]').val() || null;
	sendData.action = (sendData.id_usuario == -1)? 'new': 'edit';
	sendData.id_persona = $('#formUsuario select[name=id_persona]').val() || null;
	sendData.nombre_usuario= $('#formUsuario input[name=nombre_usuario]').val() || null;
	sendData.password = $('#formUsuario input[name=password]').val() || null;
	sendData.email = $('#formUsuario input[name=email]').val() || null;
	sendData.descripcion = $('#formUsuario textarea[name=descripcion]').val() || null;
	sendData.estado = $('#formUsuario select[name=estado]').val() || null;
	aux.sendForm(sendData, data.loadData)

	$('#modalUsuario').modal('hide');
}

ui.sendRolForm = function() {
	var sendData = {};
	sendData.entity = 'rol';
	sendData.id_rol = $('#formRol input[name=id]').val() || null;
	sendData.action = (sendData.id_rol == -1)? 'new': 'edit';
	sendData.nombre_rol = $('#formRol input[name=nombre_rol]').val() || null;
	sendData.nombre_amigable = $('#formRol input[name=nombre_amigable]').val() || null;
	sendData.descripcion= $('#formRol textarea[name=descripcion]').val() || null;
	sendData.estado = $('#formRol select[name=estado]').val() || null;
	aux.sendForm(sendData, data.loadData);

	$('#modalRol').modal('hide');
}

ui.sendPermisoForm = function() {
	var sendData = {};
	sendData.entity = 'permiso';
	sendData.id_permiso = $('#formPermiso input[name=id]').val() || null;
	sendData.action = (sendData.id_permiso == -1)? 'new': 'edit';
	sendData.nombre_permiso = $('#formPermiso input[name=nombre_permiso]').val() || null;
	sendData.funcionalidad= $('#formPermiso input[name=funcionalidad]').val() || null;
	sendData.descripcion= $('#formPermiso textarea[name=descripcion]').val() || null;
	sendData.estado = $('#formPermiso select[name=estado]').val() || null;
	aux.sendForm(sendData, data.loadData);

	$('#modalPermiso').modal('hide');
}
ui.requestPersonaDeletion = function() {
	var sendData = {};
	sendData.entity = 'persona';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData, data.loadData);
}

ui.requestUsuarioDeletion = function() {
	var sendData = {};
	sendData.entity = 'usuario';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData, data.loadData);
}

ui.requestRolDeletion = function() {
	var sendData = {};
	sendData.entity = 'rol';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData, data.loadData);
}

ui.requestPermisoDeletion = function() {
	var sendData = {};
	sendData.entity = 'permiso';
	sendData.id = ui.selectedId;
	sendData.action = 'delete';
	aux.sendForm(sendData, data.loadData);
}

initUI = function() {
	/* Bootstrap */
	$('button').addClass('btn');
	$('table').addClass('table table-hover table-responsive');
	$('input, select, textarea').addClass('form-control');
	$('.saveButton').addClass('btn btn-success glyphicon glyphicon-ok');
	data.loadData();
};



ui.activateTab = function (tab) {
	var tabSelector = '#'+tab+'Tab';
	ui.currentTab = tab;
	ui.hideTabs();
	aux.clearSelectedRow ($(tabSelector+' table tbody')[0]);

	$(tabSelector).fadeIn();

	if (tab == 'roles') {
		$('#permisosButton').show();
	} else {
		$('#permisosButton').hide();
	}

	if (tab == 'usuarios') {
		$('#rolesButton').show();
	} else {
		$('#rolesButton').hide();
	}

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
		case 'permisos':
			ui.showNewPermisoForm();
			break;
	}
};

ui.editButtonPressed = function () {
	if (ui.selectedId == null) return;
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
		case 'permisos':
			ui.showEditPermisoForm();
			break;
	}
};

ui.deleteButtonPressed = function () {
	if (ui.selectedId == null) return;
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
		case 'permisos':
			ui.requestPermisoDeletion();
			break;
	}
};

ui.rolesButtonPressed = function () {
	if (ui.selectedId == null) return;
	$('#formUsuarioRol input[name=id_usuario]').val(ui.selectedId);
	ui.showUsuarioRolForm();
}

ui.permisosButtonPressed = function () {
	if (ui.selectedId == null) return;
	$('#formPermisoRols input[name=id_rol]').val(ui.selectedId);
	ui.showRolPermisoForm();
}

ui.updatePersonasSelect = function() {
	var select = $('#formUsuario select[name=id_persona]')[0];
	var newOpt;

	select.textContent = '';
	data.personas.forEach (function (persona) {
		newOpt = document.createElement ('OPTION');
		newOpt.value = persona.id
		newOpt.textContent = persona.nombres+' '+persona.apellidos;
		select.appendChild (newOpt);
	});
}

ui.showNewPersonaForm = function () {
	$('#formPersona input[name=id]').hide()
	$('#formPersona label[for=id]').hide()
	$('#formPersonaTitle').html('Nueva Persona');
	$('#formPersona input[name=id]').val('-1');
	$('#formPersona input[name=apellidos]').val('');
	$('#formPersona input[name=nombres]').val('');
	$('#formPersona select[name=tipo_doc]').val(1);
	$('#formPersona input[name=nro_doc]').val('');
	$('#formPersona input[name=fecha_nacimiento]').val('1990-01-01');
	$('#formPersona select[name=sexo]').val('M');
	$('#formPersona input[name=foto]').val('');
	$('#formPersona input[name=domicilio]').val('');
	$('#formPersona input[name=telefono]').val('');
	$('#formPersona textarea[name=descripcion]').val('');
	$('#formPersona select[name=estado]').val('A');
	$('#formPersona input[name=foto_registro]').val('');
	$('#formPersonaUsuario select[name=usuarios]').html('');
	$('#modalPersona').modal('show');
};

ui.showNewUsuarioForm = function () {
	$('#formUsuario input[name=id]').hide();
	$('#formUsuario label[for=id]').hide();
	$('#formUsuario input[name=id]').val('-1');
	$('#formUsuario input[name=id_persona]').val('');
	$('#formUsuario input[name=nombre_usuario]').val('');
	$('#formUsuario input[name=password]').val('');
	$('#formUsuario input[name=email]').val('');
	$('#formUsuario textarea[name=descripcion]').val('');
	$('#formUsuario select[name=estado]').val('A');
	$('#modalUsuario').modal('show');
};

ui.showNewRolForm = function () {
	$('#formRol input[name=id]').hide();
	$('#formRol label[for=id]').hide();
	$('#formRol input[name=id]').val(-1);
	$('#formRol input[name=nombre_rol]').val('');
	$('#formRol input[name=nombre_amigable]').val('');
	$('#formRol textarea[name=descripcion]').val('');
	$('#formRol input[name=estado]').val('A');
	$('#formPermisoRol').hide();
	$('#modalRol').modal('show');
}

ui.showNewPermisoForm = function () {
	$('#formPermiso input[name=id]').hide();
	$('#formPermiso label[for=id]').hide();
	$('#formPermiso input[name=id]').val(-1);
	$('#formPermiso input[name=nombre_permiso]').val('');
	$('#formPermiso input[name=funcionalidad]').val('');
	$('#formPermiso textarea[name=descripcion]').val('');
	$('#formPermiso input[name=estado]').val('A');
	$('#modalPermiso').modal('show');
};;

ui.showEditPersonaForm = function () {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formPersonaTitle').html('Modificar Persona');
	$('#formPersona input[name=id]').show();
	$('#formPersona label[for=id]').show();
	$('#formPersona input[name=id]').val(selected.id);
	$('#formPersona input[name=id_persona]').val(selected.id_persona);
	$('#formPersona input[name=apellidos]').val(selected.apellidos);
	$('#formPersona input[name=nombres]').val(selected.nombres);
	$('#formPersona select[name=tipo_doc]').val(selected.tipo_doc);
	$('#formPersona input[name=nro_doc]').val(selected.nro_doc);
	$('#formPersona input[name=fecha_nacimiento]').val(selected.fecha_nacimiento);
	$('#formPersona select[name=sexo]').val(selected.sexo);
	$('#formPersona input[name=foto]').val(selected.foto);
	$('#formPersona input[name=domicilio]').val(selected.domicilio);
	$('#formPersona input[name=telefono]').val(selected.telefono);
	$('#formPersona textarea[name=descripcion]').val(selected.descripcion);
	$('#formPersona select[name=estado]').val(selected.estado);
	$('#formPersona input[name=foto_registro]').val(selected.foto_registro);
	$('#formPersonaUsuario select[name=usuarios]').html('');
	$('#modalPersona').modal('show');
};

ui.showEditUsuarioForm = function() {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formUsuario input[name=id]').hide();
	$('#formUsuario label[for=id]').hide();
	$('#formUsuario input[name=id]').val(selected.id);
	$('#formUsuario select[name=id_persona]').val(selected.id_persona);
	$('#formUsuario input[name=nombre_usuario]').val(selected.nombre_usuario);
	$('#formUsuario input[name=password]').val(selected.password);
	$('#formUsuario input[name=email]').val(selected.email);
	$('#formUsuario textarea[name=descripcion]').val(selected.descripcion);
	$('#formUsuario select[name=estado]').val((selected.estado));
	$('#modalUsuario').modal('show');
}

ui.showEditRolForm = function() {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formRol input[name=id]').show();
	$('#formRol label[for=id]').show();
	$('#formRol input[name=id]').val(selected.id);
	$('#formRol input[name=nombre_rol]').val(selected.nombre_rol);
	$('#formRol input[name=nombre_amigable]').val(selected.nombre_amigable);
	$('#formRol textarea[name=descripcion]').val(selected.descripcion);
	$('#formRol select[name=estado]').val((selected.estado));
	$('#modalRol').modal('show');
}

ui.showEditPermisoForm = function() {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formPermiso input[name=id]').show();
	$('#formPermiso label[for=id]').show();
	$('#formPermiso input[name=id]').val(selected.id);
	$('#formPermiso input[name=nombre_permiso]').val(selected.nombre_permiso);
	$('#formPermiso input[name=funcionalidad]').val(selected.funcionalidad);
	$('#formPermiso textarea[name=descripcion]').val(selected.descripcion);
	$('#formPermiso select[name=estado]').val(selected.estado);
	$('#modalPermiso').modal('show');
}

ui.showUsuarioRolForm = function () {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formUsuarioRol h3').text(selected.nombre_usuario);
	$('#formUsuarioRol input[name=id_usuario]').val(selected.id);
	$('#formUsuarioRol select[name=roles_asignados]').html('');
	$('#formUsuarioRol select[name=roles_no_asignados]').html('');

	var newOption;
	var usuario;
	var asignados =  $('#formUsuarioRol select[name=roles_asignados]')[0];
	var noAsignados =  $('#formUsuarioRol select[name=roles_no_asignados]')[0];

	usuario = data.usuarios.getById(selected.id);
	data.roles.forEach(function (rol) {
		newOption = document.createElement ('OPTION');
		newOption.value = rol.id;
		newOption.textContent = rol.nombre_amigable;
		if (usuario.roles.includes(rol.id)) {
			asignados.appendChild (newOption);
		} else {
			noAsignados.appendChild (newOption);
		}
	});

	$('#modalUsuarioRol').modal('show');
}

ui.showRolPermisoForm = function () {
	var selected = ui.getSelectedElement();
	if (selected == null) return;
	$('#formPermisoRol h3').text(selected.nombre_rol);
	$('#formPermisoRol input[name=id_rol]').val(selected.id);
	$('#formPermisoRol select[name=permisos_asignados]').html('');
	$('#formPermisoRol select[name=permisos_no_asignados]').html('');

	var newOption;
	var rol;
	var asignados =  $('#formPermisoRol select[name=permisos_asignados]')[0];
	var noAsignados =  $('#formPermisoRol select[name=permisos_no_asignados]')[0];

	rol = data.roles.getById(selected.id);
	data.permisos.forEach(function (permiso) {
		newOption = document.createElement ('OPTION');
		newOption.value = permiso.id;
		newOption.textContent = permiso.nombre_permiso;
		if (rol.permisos.includes(permiso.id)) {
			asignados.appendChild (newOption);
		} else {
			noAsignados.appendChild (newOption);
		}
	});
	$('#modalPermisoRol').modal('show');
}

ui.hideTabs = function () {
	$('.tabSection').hide();
} 

ui.closeModal = function (name) {
	$('#modal' + name).modal('hide');
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
		tr.appendChild (aux.td (aux.sexoString(elem.sexo)));
		tr.appendChild (aux.td (elem.domicilio));
		tr.appendChild (aux.td (elem.telefono));
		tr.appendChild (aux.td (aux.estadoString(elem.estado)));

		if (elem.estado == 'B') {
			$(tr).addClass('danger');
		}

		var thistr = tr;
		tr.onclick = function () {
			aux.clearSelectedRow (tbody);
			ui.selectedId = elem.id;
			$(thistr).addClass('info');
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
		tr.appendChild (aux.td (aux.estadoString(elem.estado)));

		if (elem.estado == 'B') {
			$(tr).addClass('danger');
		} else if (elem.estado == 'S') {
			$(tr).addClass('warning');
		}

		var thistr = tr;
		tr.onclick = function () {
			aux.clearSelectedRow (tbody);
			ui.selectedId = elem.id;
			$(thistr).addClass('info');
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
		tr.appendChild (aux.td (elem.nombre_rol));
		tr.appendChild (aux.td (elem.nombre_amigable));
		tr.appendChild (aux.td (aux.estadoString(elem.estado)));

		if (elem.estado == 'B') {
			$(tr).addClass('danger');
		}

		var thistr = tr;
		tr.onclick = function () {
			aux.clearSelectedRow (tbody);
			ui.selectedId = elem.id;
			$(thistr).addClass('info');
		}
		tbody.appendChild(tr);
	});
}

ui.updatePermisosTable = function () {
	var tbody = $('#permisosTable tbody')[0];
	tbody.innerHTML = '';
	data.permisos.forEach(function (elem) {
		tr = document.createElement ('TR');
		tr.appendChild (aux.td (elem.id));
		tr.appendChild (aux.td (elem.funcionalidad));
		tr.appendChild (aux.td (elem.nombre_permiso));
		tr.appendChild (aux.td (aux.estadoString(elem.estado)));

		if (elem.estado == 'B') {
			$(tr).addClass('danger');
		}

		var thistr = tr;
		tr.onclick = function () {
			aux.clearSelectedRow (tbody);
			ui.selectedId = elem.id;
			$(thistr).addClass('info');
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
		case 'permisos':
			list = data.permisos;
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

ui.assignRolUsuario = function () {
	var sendData = {
		entity: 'usuario',
		action: 'assignRol',
		id_usuario: $('#formUsuarioRol input[name=id_usuario]').val(),
		id_rol: $('#formUsuarioRol select[name=roles_no_asignados]').val()
	};

	if (sendData.id_rol == null) {
		return;
	}

	var onsuccess = function (jsonData) {
		data.usuarios.getById(jsonData.id_usuario).roles.push(jsonData.id_rol);

		if ($('#formUsuarioRol input[name=id_usuario]').val() == jsonData.id_usuario) {
			$('#formUsuarioRol select[name=roles_no_asignados] option[value='+jsonData.id_rol+']')
				.detach()
				.appendTo('#formUsuarioRol select[name=roles_asignados]');
		}
	};
	aux.sendForm (sendData, onsuccess);
}

ui.removeRolUsuario = function () {
	var sendData = {
		entity: 'usuario',
		action: 'removeRol',
		id_usuario: $('#formUsuarioRol input[name=id_usuario]').val(),
		id_rol: $('#formUsuarioRol select[name=roles_asignados]').val()
	};

	if (sendData.id_rol == null) {
		return;
	}

	var onsuccess = function (jsonData) {
		data.usuarios.getById(jsonData.id_usuario).roles.removeElement(jsonData.id_rol);

		if ($('#formUsuarioRol input[name=id_usuario]').val() == jsonData.id_usuario) {
			$('#formUsuarioRol select[name=roles_asignados] option[value='+jsonData.id_rol+']')
				.detach()
				.appendTo('#formUsuarioRol select[name=roles_no_asignados]');
		}
	};
	aux.sendForm (sendData, onsuccess);
}

ui.assignPermisoRol = function () {
	var sendData = {
		entity: 'rol',
		action: 'assignPermiso',
		id_rol: $('#formPermisoRol input[name=id_rol]').val(),
		id_permiso: $('#formPermisoRol select[name=permisos_no_asignados]').val()
	};

	if (sendData.id_permiso == null) {
		return;
	}

	var onsuccess = function (jsonData) {
		data.roles.getById(jsonData.id_rol).permisos.push(jsonData.id_permiso);

		if ($('#formPermisoRol input[name=id_rol]').val() == jsonData.id_rol) {
			$('#formPermisoRol select[name=permisos_no_asignados] option[value='+jsonData.id_permiso+']')
				.detach()
				.appendTo('#formPermisoRol select[name=permisos_asignados]');
		}
	};
	aux.sendForm (sendData, onsuccess);
}
ui.revokePermisoRol = function () {
	var sendData = {
		entity: 'rol',
		action: 'revokePermiso',
		id_rol: $('#formPermisoRol input[name=id_rol]').val(),
		id_permiso: $('#formPermisoRol select[name=permisos_asignados]').val()
	};

	if (sendData.id_permiso == null) {
		return;
	}

	var onsuccess = function (jsonData) {
		data.roles.getById(jsonData.id_rol).permisos.removeElement(jsonData.id_permiso);

		if ($('#formPermisoRol input[name=id_rol]').val() == jsonData.id_rol) {
			$('#formPermisoRol select[name=permisos_asignados] option[value='+jsonData.id_permiso+']')
				.detach()
				.appendTo('#formPermisoRol select[name=permisos_no_asignados]');
		}
	};
	aux.sendForm (sendData, onsuccess);
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
		$(tbody.childNodes[i]).removeClass('info');
		i++;
	}
	ui.selectedId = null;
}

aux.sendForm = function (sendData, onsuccess) {
	$.ajax({
		url: '/users',
		method: 'POST',
		data: sendData,
		dataType: 'json',
		success: function (jsonData) {
			DEBUGresponse = jsonData;
			if (jsonData.result) {
				onsuccess (jsonData);
			} else {
				window.alert (jsonData.msg);
			}
		},
		error: function (er1, err2, err3) {
			document.body.innerHTML = er1.responseText;
			window.alert (err3);
		}
	});
}

aux.estadoString = function (caracter) {
	switch (caracter) {
		case 'A': return "Activo";
		case 'B': return "Inactivo";
		case 'S': return "Suspendido";
		case '': return "No especificado";
		case null: return "No especificado";
		default: return "Desconocido";
	}
}

aux.sexoString = function (caracter) {
	switch (caracter) {
		case 'F': return "Femenino";
		case 'M': return "Masculino";
		case 'O': return "Otro";
		case '': return "No especificado";
		case null: return "No especificado";
		default: return "Desconocido";
	}
}

Array.prototype.removeElement = function(item) {
	var pos = this.indexOf (item);
	if (pos != -1) {
		this.splice(pos, 1);
	}
	return (pos != -1)? item: null;
}

/* Para navegadores viejos */
if (Array.prototype.includes == undefined) {
	Array.prototype.includes = function (item) {
		return this.indexOf(item) != -1;
	}
}
