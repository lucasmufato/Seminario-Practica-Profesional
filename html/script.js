ui = {};
ui.currentTab = 'personas';
ui.selectedId = null;

data={};

/* Estos deben cargarse desde el servidor */
data.personas=[];
data.usuarios=[];
data.roles=[];

/*  */
data.personas=[
	{
		'id': 1,
		'apellidos': 'Diaz',
		'nombres': 'Xavier',
		'tipo_doc': 1,
		'nro_doc': 65432109,
		'fecha_nacimiento': '1998-02-27',
		'domicilio': 'Calle sin nombre 404',
		'descripcion': null,
		'telefono': '011 15 342342',
		'estado': 0
	},
	{
		'id': 2,
		'apellidos': 'Guerrero',
		'nombres': 'Martin',
		'tipo_doc': 1,
		'nro_doc': 56789012,
		'fecha_nacimiento': '1994-12-30',
		'domicilio': 'Sarmiento 34',
		'descripcion': null,
		'telefono': '011 15 889876',
		'estado': 0
	}
];

data.usuarios=[
	{
		'id': 21,
		'id_persona': 1,
		'nombre_usuario': 'xdiaz',
		'password': 'abc123',
		'email': 'xdiaz@live.com',
		'descripcion': null,
		'estado': 1
	},
	{
		'id': 22,
		'id_persona': 2,
		'nombre_usuario': 'mguerrero',
		'password': 'xyz456',
		'email': 'mguerr@yahoo.com',
		'estado': 1,
		'descripcion': null
	}
];

data.roles=[
	{
		'id': 51,
		'nombre_rol': 'conductor',
		'nombre_amigable': 'Conductor motorizado',
		'estado': 1,
		'descripcion': null
	},{
		'id': 52,
		'nombre_rol': 'pasajero',
		'nombre_amigable': 'Pasajero Viajante',
		'estado': 1,
		'descripcion': null
	}

];

initUI = function() {
	ui.updatePersonasTable();
	ui.updateUsuariosTable();
	ui.updateRolesTable();
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
};

ui.showNewPersonaForm = function () {
	$('#formPersona').show();
};

ui.showNewUsuarioForm = function () {
	$('#formUsuario').show();
};

ui.showNewRolForm = function () {
	$('#formRol').show();
};

ui.showEditPersonaForm = ui.showNewPersonaForm;
ui.showEditUsuarioForm = ui.showNewUsuarioForm;
ui.showEditRolForm = ui.showNewRolForm;

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
		tr.appendChild (aux.td (elem.tipo_doc));
		tr.appendChild (aux.td (elem.nro_doc));
		tr.appendChild (aux.td (elem.fecha_nacimiento));
		tr.appendChild (aux.td (elem.domicilio));
		tr.appendChild (aux.td (elem.telefono));
		tr.appendChild (aux.td (elem.estado));
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
		tr.appendChild (aux.td (elem.estado));
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
		tr.appendChild (aux.td (elem.nombre_rol));
		tr.appendChild (aux.td (elem.nombre_amigable));
		tr.appendChild (aux.td (elem.estado));
		var thistr = tr;
		tr.onclick = function () {
			ui.selectedId = elem.id;
			aux.clearSelectedRow (tbody);
			thistr.className='selectedRow';
		}
		tbody.appendChild(tr);
	});
}

/* Funciones auxiliares */
aux = {};
aux.td = function (text){
	var td = document.createElement('TD');
	td.appendChild(document.createTextNode(text));
	return td;
}
aux.clearSelectedRow = function (tbody) {
	debugTBODY = tbody;
	var i = 0;

	while (i < tbody.childNodes.length) {
		tbody.childNodes[i].className = '';
		i++;
	}
}
