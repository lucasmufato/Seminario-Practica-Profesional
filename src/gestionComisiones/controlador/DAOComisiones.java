package gestionComisiones.controlador;

import javax.persistence.Query;
import javax.persistence.RollbackException;

import gestionComisiones.modelo.Comision;
import gestionComisiones.modelo.ComisionCobrada;
import gestionComisiones.modelo.EstadoComisionCobrada;
import gestionComisiones.modelo.PrecioComision;
import gestionViajes.modelo.PasajeroViaje;
import otros.DataAccesObject;
import otros.ExceptionViajesCompartidos;
import otros.ManejadorErrores;

public class DAOComisiones extends DataAccesObject {

	public DAOComisiones() {
		super();
	}
	
	public ComisionCobrada nuevaComisionCobrada(Double km){
		ComisionCobrada cc = new ComisionCobrada();
		try {
			Query qry = entitymanager.createNamedQuery("Comision.porKm");
			qry.setParameter("km", km);
			Comision comision = (Comision) qry.getSingleResult();
			PrecioComision pc = comision.getPrecio_comision();
			cc.setComision(comision);
			float monto = pc.getMonto();
			cc.setMonto(monto);
		}catch(Exception e){
			cc.setComision(null);
			cc.setMonto(0);
			return cc;
		}
		
			// TODO este metodo tendria que buscar la comision actual para esos KM, y asignarle el monto a la ComisionCobrada
		return 	cc;
	}

	//vacio por ahora
}
