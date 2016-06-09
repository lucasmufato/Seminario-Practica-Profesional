package gestionViajes.controlador;

import gestionViajes.modelo.Viaje;
import otros.Planificador;
import otros.TareaProgramable;
import java.util.List;
import java.util.ArrayList;

class PlanificadorFinalizarViaje extends Planificador {
	private DAOViajes dao;

	public PlanificadorFinalizarViaje (DAOViajes dao) {
		this.dao = dao;
		System.out.println("[PLANIFICADOR]: Se ha creado el planificador de finalizar viajes");
	}

	@Override
	public List<TareaProgramable> tareasAtrasadas() {
		//Buscar viajes que ya deberian haber finalizado
		ArrayList<TareaProgramable> tareas = new ArrayList<TareaProgramable>();

		for (Viaje viaje: dao.listarViajesNoFinalizadosAtrasados()) {
			tareas.add(new TareaFinalizarViaje (this.dao, viaje));
		}

		if (tareas.size() > 0) {
			System.out.println("[PLANIFICADOR]: Se encontraron "+ tareas.size()+" viajes no finalizados atrasados");
		}

		return tareas;
	}

	@Override
	public List<TareaProgramable> tareasProximas(long milisecs) {
		//Buscar viajes que deberian finalizar en los proximos milisecs milisegundos
		ArrayList<TareaProgramable> tareas = new ArrayList<TareaProgramable>();
		TareaProgramable tarea;

		for (Viaje viaje: dao.listarViajesProximosAFinalizar(milisecs)) {
			tarea = new TareaFinalizarViaje(this.dao, viaje);
			tareas.add (tarea);
		}

		if (tareas.size() > 0) {
			System.out.println("[PLANIFICADOR]: En los proximos "+milisecs/1000+" segundos finailzaran "+ tareas.size()+" viajes");
		}

		return tareas;
	}

}
