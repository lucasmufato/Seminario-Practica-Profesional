package gestionViajes.controlador;

import gestionViajes.modelo.Viaje;

import java.util.List;
import java.sql.Timestamp;

class SchedulerThread implements Runnable {

	// Cuando terminar sea verdadero, se dejaran de programar tareas
	boolean terminar = false;
	long tiempoPlanificacion;
	
	public SchedulerThread (long tiempo_planificacion) {
		this.tiempoPlanificacion = tiempo_planificacion;
	}

	public void terminar() {
		// Dejar de programar tareas
		this.terminar = true;
	}

	public void run() {
		List<Viaje> viajes;

		while (!terminar) {
			// Buscar viajes que van a iniciar dentro del tiempo de planificacion
			viajes = SchedulerViajes.proximosAIniciar(this.tiempoPlanificacion);

			// Indicarles que se autoinicien
			for (Viaje viaje: viajes) {
				SchedulerViajes.autoIniciarViaje(viaje);
			}

			// Calcular tiempo de proxima planificacion y guardarlo en SchedulerViajes
			Timestamp prox = new Timestamp (new java.util.Date().getTime() + this.tiempoPlanificacion);
			SchedulerViajes.setProximaPlanificacion(prox);
			
			try {
				Thread.sleep (this.tiempoPlanificacion);
			} catch (InterruptedException e) {
				this.terminar = true;
			}
		}
	}

	
}
