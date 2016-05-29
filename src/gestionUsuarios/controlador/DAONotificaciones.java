package gestionUsuarios.controlador;

import gestionUsuarios.modelo.Cliente;
import gestionUsuarios.modelo.EstadoNotificacion;
import gestionUsuarios.modelo.Notificacion;
import otros.DataAccesObject;
import otros.ExceptionViajesCompartidos;

import java.util.List;

import javax.persistence.Query;

class DAONotificaciones extends DataAccesObject {
	public DAONotificaciones () {
		super();
	}

	public void marcarLeida(int idNotificacion) {
		this.entitymanager.getTransaction().begin();
		Notificacion notificacion =(Notificacion) this.buscarPorPrimaryKey(new Notificacion(), idNotificacion);
		notificacion.setEstado(EstadoNotificacion.leido);
		this.entitymanager.getTransaction().commit();
	}
	/* METODO REPETIDO
	public List<Notificacion> listarNoLeidas(int id_usuario) throws ExceptionViajesCompartidos {
		List<Notificacion> notificacionesNoLeidas=null;
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_usuario);
		if(cliente == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
		List<Notificacion> notificacionesTotales = cliente.getNotificaciones();
		for (Integer i = 0; i < notificacionesTotales.size(); i++) {
			if (notificacionesTotales.get(i).getEstado().equals(EstadoNotificacion.no_leido)) {
				notificacionesNoLeidas.add(notificacionesTotales.get(i));
			}
		}
		return notificacionesNoLeidas;
	}
	*/
	
	public List<Notificacion> getNotificaciones(Integer id_usuario) throws ExceptionViajesCompartidos{
    	Cliente cliente= (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_usuario);
    	if(cliente == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
    	Query qry = entitymanager.createNamedQuery("Notificacion.porUsuario");
		qry.setParameter("id_cliente", cliente);
		List<Notificacion> notificaciones = qry.getResultList();
    	return notificaciones;
    }
    
    public List<Notificacion> getNotificacionesNoLeidas(Integer id_usuario) throws ExceptionViajesCompartidos{
    	Cliente cliente= (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_usuario);
    	if(cliente == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
    	Query qry = entitymanager.createNamedQuery("Notificacion.NoLeidasPorUsuario");
		qry.setParameter("id_cliente", cliente);
		List<Notificacion> notificaciones = qry.getResultList();
    	return notificaciones;
    }
    
    public Integer getCantidadNotificacionesNoLeidas(Integer id_usuario) throws ExceptionViajesCompartidos{
    	Cliente cliente= (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_usuario);
    	if(cliente == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
    	Query qry = entitymanager.createNamedQuery("Notificacion.cantidadNoLeidaPorUsuario");
		qry.setParameter("id_cliente", cliente);
		Long cantidad =(Long) qry.getSingleResult();
    	return cantidad.intValue();
    }
    
    public boolean setNotificacionesLeidas(Integer id_usuario, List<Integer> lista_id_notificaciones) throws ExceptionViajesCompartidos{
    	Cliente cliente= (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_usuario);
    	if(cliente == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
    	this.entitymanager.getTransaction().begin();
    	for(Integer i:lista_id_notificaciones){
    		Notificacion n=(Notificacion) this.buscarPorPrimaryKey(new Notificacion(), i);
    		n.setEstado(EstadoNotificacion.leido);
    	}
    	this.entitymanager.getTransaction().commit();
    	return true;
    }
}