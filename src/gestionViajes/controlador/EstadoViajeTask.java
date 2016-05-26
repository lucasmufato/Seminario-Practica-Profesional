package gestionViajes.controlador;

import gestionViajes.modelo.Viaje;
import gestionViajes.controlador.DAOViajes;

import java.util.TimerTask;

class EstadoViajeTask extends TimerTask {
	private Viaje viaje;
	private DAOViajes dao;

	public EstadoViajeTask (DAOViajes dao, Viaje viaje) {
		this.viaje = viaje;
		this.dao = dao;
	}
	
	@Override
	public void run () {
		if (this.dao.actualizarEstadoViaje(this.viaje.getId_viaje())) {
			System.out.println("[SCHEDULER]: Se ha iniciado por temporizacion el viaje con id "+this.viaje.getId_viaje());
		}
	}
}
