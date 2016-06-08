package gestionViajes.controlador;

import gestionViajes.modelo.Viaje;
import otros.Planificador;
import otros.TareaProgramable;
import java.util.List;
import java.util.ArrayList;

class PlanificadorEstadoViaje extends Planificador {
	private DAOViajes dao;

	public PlanificadorEstadoViaje (DAOViajes dao) {
		this.dao = dao;
		System.out.println("[PLANIFICADOR]: Se ha creado el planificador de estado de viajes");
	}

	@Override
	public List<TareaProgramable> tareasAtrasadas() {
		//Buscar viajes que ya deberian haber iniciado
		ArrayList<TareaProgramable> tareas = new ArrayList<TareaProgramable>();

		for (Viaje viaje: dao.listarViajesNoIniciadosAtrasados()) {
			tareas.add(new TareaEstadoViaje (this.dao, viaje));
		}

		if (tareas.size() > 0) {
			System.out.println("[PLANIFICADOR]: Se encontraron "+ tareas.size()+" viajes atrasados");
		}

		return tareas;
	}

	@Override
	public List<TareaProgramable> tareasProximas(long milisecs) {
		//Buscar viajes que deberian iniciar en los proximos milisecs milisegundos
		ArrayList<TareaProgramable> tareas = new ArrayList<TareaProgramable>();
		TareaProgramable tarea;

		for (Viaje viaje: dao.listarViajesProximosAIniciar(milisecs)) {
			tarea = new TareaEstadoViaje(this.dao, viaje);
			tarea.setTiempo(viaje.getFecha_inicio());
			tareas.add (tarea);
		}

		if (tareas.size() > 0) {
			System.out.println("[PLANIFICADOR]: En los proximos "+milisecs/1000+" segundos se iniciaran "+ tareas.size()+" viajes");
		}

		return tareas;
	}

}
