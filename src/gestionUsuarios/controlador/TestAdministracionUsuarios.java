package gestionUsuarios.controlador;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gestionUsuarios.modelo.*;
import junit.framework.TestCase;

public class TestAdministracionUsuarios extends TestCase {
	protected DAOAdministracionUsuarios dao = new DAOAdministracionUsuarios();
	
	@Test
	public void testGetNotificaciones() {
		List<Notificacion> notificaciones=this.dao.getNotificaciones(2);		//2=id_cliente
		assertEquals(notificaciones.size(),3);		//3 = cantidad de notificaciones que deberia tener
	}
	
	@Test
	public void testGetNotificacionesNoLeidas() {
		List<Notificacion> notificaciones=this.dao.getNotificacionesNoLeidas(2);
		assertEquals(notificaciones.size(),1);
	}
	
	@Test
	public void testgetCantidadNotificacionesNoLeidas() {
		Integer cant_not=this.dao.getCantidadNotificacionesNoLeidas(2);
		System.out.println("cant_not =" +cant_not);
		//assertEquals(cant_not,2);
	}
	
	@Test
	public void testSetNotificacionesLeidas() {
		List<Integer> lista= new ArrayList<Integer>();
		lista.add(53);
		lista.add(103);
		boolean b=this.dao.SetNotificacionesLeidas(2,lista);
		assertTrue(b);
	}
	
	
}
