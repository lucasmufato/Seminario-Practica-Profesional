package gestionViajes.controlador;

import otros.TareaProgramable;
import gestionViajes.modelo.Viaje;
import gestionViajes.controlador.DAOViajes;

import java.util.TimerTask;

class TareaEstadoViaje extends TareaProgramable {
	private Viaje viaje;
	private DAOViajes dao;

	public TareaEstadoViaje (DAOViajes dao, Viaje viaje) {
		this.viaje = viaje;
		this.dao = dao;
	}
	
	@Override
	public void ejecutar () {
		if (this.dao.actualizarEstadoViaje(this.viaje.getId_viaje())) {
			System.out.println("[PLANIFICADOR]: Se ha iniciado por temporizacion el viaje con id "+this.viaje.getId_viaje());
		}
	}
}
