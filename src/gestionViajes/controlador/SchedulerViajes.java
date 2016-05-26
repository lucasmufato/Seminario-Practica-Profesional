package gestionViajes.controlador;

import gestionViajes.modelo.Viaje;
import gestionViajes.controlador.DAOViajes;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class SchedulerViajes {
	static final long TIEMPO_PREVISION = 60000; //1 minuto

	static DAOViajes dao = null;
	static Thread schedulerThread = null;
	static Timer timer = new Timer();
	static Timestamp proximaPlanificacion = null;
	
	private SchedulerViajes() {
		//Constructor es privado, usar solo metodos static
		
	}

	public static void setDao(DAOViajes dao) {
		SchedulerViajes.dao = dao;
	}

	public static void iniciar () {
		if (SchedulerViajes.dao != null) {
			actualizarIniciosAtrasados();
			planificarProximos(TIEMPO_PREVISION);
		}
	}

	private synchronized static void planificarProximos(long milisecs){
		//Crear un thread que programe los proximos 
		if (SchedulerViajes.schedulerThread == null) {
			SchedulerViajes.schedulerThread = new Thread (new SchedulerThread(milisecs));
			SchedulerViajes.schedulerThread.start();
		}
	}

	private static void actualizarIniciosAtrasados() {
		//Buscar viajes que ya deberian haber iniciado e iniciarlos
		List<Viaje> atrasados = dao.listarViajesNoIniciadosAtrasados();
		for(Viaje viaje: atrasados) {
			if (dao.actualizarEstadoViaje(viaje.getId_viaje())) {
				System.out.println("[SCHEDULER]: Se ha iniciado el viaje atrasado con id " + viaje.getId_viaje());
			}
		}
		
	}

	public static void autoIniciarViaje(Viaje viaje) {
		SchedulerViajes.timer.schedule(new EstadoViajeTask (SchedulerViajes.dao, viaje), viaje.getFecha_inicio());
		System.out.println("[SCHEDULER]: Viaje " + viaje.getId_viaje() + " se iniciara automaticamente a las "+viaje.getFecha_inicio());
	}

	public static List<Viaje> proximosAIniciar(long milisegs) {
		List<Viaje> proximos = dao.listarViajesProximosAIniciar(milisegs);
		if (proximos.size() > 0) {
			System.out.println("[SCHEDULER]: En los proximos " + (milisegs/1000) + " segundos se iniciaran " + proximos.size() + " viajes");
		}
		return proximos;
	}

	public static void nuevoViaje(Viaje viaje) {
		// Si la fecha de inicio del viaje es anterior a la de la proxima planificacion, marcar como autoinicable
		if(viaje.getFecha_inicio().compareTo(SchedulerViajes.proximaPlanificacion) <= 0) {
			SchedulerViajes.autoIniciarViaje(viaje);
		}
	}

	public static void setProximaPlanificacion(Timestamp proxima) {
		SchedulerViajes.proximaPlanificacion = proxima;
	}
}
