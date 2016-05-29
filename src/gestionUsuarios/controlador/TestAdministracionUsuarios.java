package gestionUsuarios.controlador;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gestionUsuarios.modelo.*;
import junit.framework.TestCase;
import otros.ExceptionViajesCompartidos;

public class TestAdministracionUsuarios extends TestCase {
	protected DAONotificaciones dao = new DAONotificaciones();
	protected DAOAdministracionUsuarios daoAdmUsu = new DAOAdministracionUsuarios();
	
	@Test
	public void testGetNotificaciones() throws ExceptionViajesCompartidos {
		List<Notificacion> notificaciones=this.dao.getNotificaciones(2);		//2=id_cliente
		assertEquals(notificaciones.size(),3);		//3 = cantidad de notificaciones que deberia tener
	}
	
	@Test
	public void testGetNotificacionesNoLeidas() throws ExceptionViajesCompartidos {
		List<Notificacion> notificaciones=this.dao.getNotificacionesNoLeidas(2);
		assertEquals(notificaciones.size(),1);
	}
	
	@Test
	public void testgetCantidadNotificacionesNoLeidas() throws ExceptionViajesCompartidos {
		Integer cant_not=this.dao.getCantidadNotificacionesNoLeidas(2);
		System.out.println("cant_not =" +cant_not);
		//assertEquals(cant_not,2);
	}
	
	@Test
	public void testSetNotificacionesLeidas() throws ExceptionViajesCompartidos {
		List<Integer> lista= new ArrayList<Integer>();
		lista.add(53);
		lista.add(103);
		boolean b=this.dao.setNotificacionesLeidas(2,lista);
		assertTrue(b);
	}
	
	
}
