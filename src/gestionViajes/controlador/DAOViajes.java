package gestionViajes.controlador;

import java.util.List;

import org.json.simple.JSONObject;

import gestionUsuarios.modelo.*;
import gestionViajes.modelo.*;
import javax.persistence.Persistence;
import javax.persistence.Query;
import otros.DataAccesObject;

public class DAOViajes extends DataAccesObject {

    public DAOViajes(){
    	super();
    }
    
	public Cliente getConductorViaje(Integer id_viaje) {
                //agregado de fede
                Cliente conductor = new Cliente();
                Viaje v = new Viaje();
                Query qry = entitymanager.createNamedQuery("Viaje.SearchById");
    		qry.setParameter("id_viaje", id_viaje);
    		v =(Viaje)qry.getSingleResult();
                Maneja conductor_maneja = v.getConductor_vehiculo();
                conductor = conductor_maneja.getCliente(); 
                
		return conductor;
                //fin agregado fede
	}
	
	public Vehiculo getAutoViaje(Integer id_viaje) {
		// TODO Auto-generated method stub
                //agregado de fede
                Cliente conductor = new Cliente();
                Viaje v = new Viaje();
                Vehiculo veh = new Vehiculo();
                Query qry = entitymanager.createNamedQuery("Viaje.SearchById");
    		qry.setParameter("id_viaje", id_viaje);
    		v =(Viaje)qry.getSingleResult();
                Maneja conductor_maneja = v.getConductor_vehiculo();
                veh= conductor_maneja.getVehiculo();
                
		return veh;
                //fin agregado fede
		
	}

	public void nuevoViaje(JSONObject datos) {
		// TODO Auto-generated method stub
		/*
		 * crear viaje, crear localidad_viaje para orige,destino,puntos intermedio
		 * asignar vehiculo, si tiene vuelta crear viaje de vuelta con los puntos invertidos y la fecha especificada
		 * linkear viaje de ida con viaje de vuelta
		 * asignar estado no_iniciado
		 * asignar conductor, nombre amigable, fecha,tipo_viaje, costo,asientos,
		 * asignar maneja
		 * 
		 */
		
	}

	public Maneja buscarManeja(Integer id_cliente, Integer id_vehiculo){
		//podria ser resuelto por un buscar por pk compuesta en el DAO general
		//agregado fede
                Maneja conductor_vehiculo = new Maneja();    
                Query qry = entitymanager.createNamedQuery("Maneja.SearchById");
    		qry.setParameter("id_cliente",id_cliente);
                qry.setParameter("id_vehiculo",id_vehiculo);
    		conductor_vehiculo =(Maneja)qry.getSingleResult();
                
                
		return conductor_vehiculo;
                //fin fede
	}
	
	public Cliente GetConductorViaje(Integer id_viaje){
		// TODO Auto-generated method stub
		return null;
	}
	
	public Vehiculo getVehiculoViaje(Integer id_viaje){
		// TODO Auto-generated method stub
		return null;
	}
	
	public void Cliente_se_postula_en_viaje(Integer id_viaje, Integer id_cliente){
		// TODO Auto-generated method stub
		/*
		 * recuperar cliente
		 * recuperar viaje
		 * viaje.addpasajero(cliente, localidad subida, localidad bajada)
		 * dao.notificar_usuario()
		 * crear_comision_por_pasajero estado pendiente
		 * 
		 */
	}
	
	public Integer comision_por_recorrido(Localidad inicio, Localidad destino, Integer id_viaje){

		// TODO Auto-generated method stub
		/*
		 * levantar viaje
		 * viaje.devolver_recorrido(inicio, destino)
		 * calcular distancia
		 * buscar comision y precio
		 */
		return null;
	}
	
	public List<Viaje> buscarViajes(JSONObject busqueda){
		// TODO Auto-generated method stub
		//crear query (campos obligatorios: origen, destino,fecha_desde)
		return null;
	}
	
	public List<PasajeroViaje> listarPasajerosPorViaje(Integer id_viaje) {
		//recupero viaje
		//viaje.get_lista_pasajeros()
		// TODO Auto-generated method stub
		return null;
	}

	public void nombreAmigablePorViaje(Integer id_viaje) {
		// TODO Auto-generated method stub
		
	}

	public void aceptarPasajero(Integer id_cliente_postulante, Integer id_viaje) {
		// TODO Auto-generated method stub
		/*
		 * recupero viaje
		 * recupero pasajero viaje
		 * pasajero viaje estado=aceptado/rechazado
		 * notificar al pasajero
		 */
	}

	public void rechazarPasajero(Integer id_cliente_postulante, Integer id_viaje) {
		// TODO Auto-generated method stub
		
	}
}
