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
	
	//retocado por mufa
	public ComisionCobrada nuevaComisionCobrada(Double km) throws ExceptionViajesCompartidos{
            if(this.entitymanager.getTransaction().isActive()){
                this.entitymanager.getTransaction().rollback();
            }
            
            ComisionCobrada cc = new ComisionCobrada();
			Query qry = entitymanager.createNamedQuery("Comision.PrecioPorKM");
			qry.setParameter("km", km);
			Comision comision = (Comision) qry.getSingleResult();
			if(comision==null){
				throw new ExceptionViajesCompartidos("ERROR: NO SE PUDO RECUPERAR LA COMISION PARA: "+km+" Kms");
			}
			cc.setComision(comision);
			cc.setMonto( comision.getPrecio() );
			return cc;
	}
	
	public boolean cobrarComision(PasajeroViaje pv){
		if(this.entitymanager.getTransaction().isActive()){
                this.entitymanager.getTransaction().rollback();
                }
                
                this.entitymanager.getTransaction( ).begin( );
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
		 Viaje v=(Viaje) this.buscarPorPrimaryKey(new Viaje(),pv.getViaje().getId_viaje());
		 Cliente conductor=v.getConductor();
		 //LE DESCUENCTO LA COMISION POR ESE PASAJERO Q RECIBIO
		 float saldo=conductor.getSaldo();
		 conductor.setSaldo(saldo-cc.getMonto());
                 
                //le cambio el estado al PV por comision cobrada
                 pv.getComision().setEstado(EstadoComisionCobrada.pagado);

                 try{
                     this.entitymanager.persist(ms);
                     this.entitymanager.getTransaction().commit();
                 }catch(Exception e){
                    e.printStackTrace();
                 }
		return true;
	}
	
	
	public boolean sumarSaldo(int id_cliente,float monto){
            
            if(this.entitymanager.getTransaction().isActive()){
                this.entitymanager.getTransaction().rollback();
            }
            
                this.entitymanager.getTransaction( ).begin( );
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente); 
		// Esto es por las dudas
		this.refresh(cliente);
		float saldo=cliente.getSaldo();
		cliente.setSaldo(saldo+monto);
		
		//CREO PAGO
		Pago p=new Pago();
		java.util.Date utilDate = new java.util.Date();
                java.sql.Date fecha = new java.sql.Date(utilDate.getTime());
		p.setFecha(fecha);
		p.setMonto(monto);
		p.setCliente(cliente);
                try{
                 this.entitymanager.persist(p);
                 this.entitymanager.getTransaction().commit();
                 }catch(Exception e){
                    e.printStackTrace();
                 }
                
                this.entitymanager.getTransaction().begin();
		//CREO EL MOVIMIENTO SALDO
		 MovimientoSaldo ms=new MovimientoSaldo();
		 ms.setComision_cobrada(null);
		 ms.setFecha(fecha);
		 ms.setMonto(monto);
		 ms.setPago(p);
		 Query qry = entitymanager.createNamedQuery("TipoMovSaldo.SearchById");
		 qry.setParameter("id",2);
		 TipoMovSaldo tms= (TipoMovSaldo) qry.getSingleResult();
		 ms.setTipo_mov_saldo(tms);
		 
		 //p.setMovimiento_saldo(ms); 
                 
                 try{
                 //this.entitymanager.persist(p);
                 this.entitymanager.persist(ms);
                 this.entitymanager.getTransaction().commit();
                 }catch(Exception e){
                    e.printStackTrace();
                 }
		
		return true;
	}

	public float consultarSaldo(int id_cliente){
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente); 
		float saldo=cliente.getSaldo();
	return saldo;
	}
}
