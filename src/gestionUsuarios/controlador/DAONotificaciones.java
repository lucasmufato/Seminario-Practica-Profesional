package gestionUsuarios.controlador;

import gestionUsuarios.modelo.Notificacion;
import otros.DataAccesObject;

import java.util.List;

class DAONotificaciones extends DataAccesObject {
	public DAONotificaciones () {
		super();
	}

	public void marcarLeida(int id_notificacion) {
	}

	public List<Notificacion> listarNoLeidas(int id_usuario) {
		List<Notificacion> lista=null;
		
		return lista;
	}
}