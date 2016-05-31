package gestionComisiones.controlador;

import java.sql.Timestamp;

import javax.persistence.Query;
import javax.persistence.RollbackException;

import gestionComisiones.modelo.Comision;
import gestionComisiones.modelo.ComisionCobrada;
import gestionComisiones.modelo.EstadoComisionCobrada;
import gestionComisiones.modelo.MovimientoSaldo;
import gestionComisiones.modelo.Pago;
import gestionComisiones.modelo.PrecioComision;
import gestionComisiones.modelo.TipoMovSaldo;
import gestionUsuarios.modelo.Cliente;
import gestionViajes.modelo.PasajeroViaje;
import gestionViajes.modelo.Viaje;
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
	
	public boolean cobrarComision(PasajeroViaje pv){
		 ComisionCobrada cc=pv.getComision();
		 //CREO EL MOVIMIENTO SALDO
		 MovimientoSaldo ms=new MovimientoSaldo();
		 java.util.Date utilDate = new java.util.Date();
	     java.sql.Date fecha = new java.sql.Date(utilDate.getTime());
		 ms.setFecha(fecha);
		 ms.setComision_cobrada(cc);
		 ms.setMonto(cc.getMonto());
		 ms.setPago(null);
		 Query qry = entitymanager.createNamedQuery("TipoMovSaldo.SearchById");
		 qry.setParameter("id",1);
		 TipoMovSaldo tms= (TipoMovSaldo) qry.getSingleResult();
		 ms.setTipo_mov_saldo(tms);
		 //BUSCO CONDUCTOR
		 Viaje v=(Viaje) searchById(pv.getViaje());
		 Cliente conductor=v.getConductor();
		 //LE DESCUENCTO LA COMISION POR ESE PASAJERO Q RECIBIO
		 float saldo=conductor.getSaldo();
		 conductor.setSaldo(saldo-cc.getMonto());
		 
		return true;
	}
	
	
	public boolean sumarSaldo(int id_cliente,float monto){
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente); 
		float saldo=cliente.getSaldo();
		cliente.setSaldo(saldo-monto);
		
		//CREO PAGO
		Pago p=new Pago();
		java.util.Date utilDate = new java.util.Date();
	    java.sql.Date fecha = new java.sql.Date(utilDate.getTime());
		p.setFecha(fecha);
		p.setMonto(monto);
		p.setCliente(cliente);
	
		//CREO EL MOVIMIENTO SALDO
		 MovimientoSaldo ms=new MovimientoSaldo();
		 ms.setComision_cobrada(null);
		 ms.setFecha(fecha);
		 ms.setMonto(monto);
		 ms.setPago(p);
		 
		 
		 p.setMovimiento_saldo(ms); 
		
		return true;
	}
}
