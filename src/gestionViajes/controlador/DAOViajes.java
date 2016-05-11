package gestionViajes.controlador;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionUsuarios.modelo.*;
import gestionViajes.modelo.*;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import otros.DataAccesObject;
import otros.ExceptionViajesCompartidos;
import otros.ManejadorErrores;

public class DAOViajes extends DataAccesObject {

    public DAOViajes(){
    	super();
    }
    
    public boolean NuevoVehiculo(JSONObject datos) throws ExceptionViajesCompartidos{
    	
    	/* estructura del JSON datos:
    	 * { "CONDUCTOR": ID_USUARIO,
    	 * "VEHICULO":{FOTO,MARCA,MODELO, PATENTE,ANIO,AIRE_ACOND,SEGURO} FOTO,AIRE_ACOND Y SEGURO NO ESTAN EN EL DIAGRAMA DE CLASE, Q SE HACE?
    	 * }
    	 * 
    	 * pasos a seguir:
    	 * recupero el cliente, si no existe tiro error
    	 * verifico si existe el auto (verifico por patente que es unique)
    	 * 			si existe, segun los casos de uso no pasa nada :P jjajajaja, asi q tiro error y listo, 
    	 * 						hay un caso de uso que dice "mantener conductores asociados", asi q ese tema se resuelve en otro metodo
    	 * 		no existe el auto, lo creo
    	 * 		creo la relacion Maneja
    	 */
    	
    	Integer id_cliente=(Integer) datos.get("conductor");
    	Cliente cliente=(Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);
    	if(cliente==null){
    		throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE!");
    	}
    	JSONObject datos_vehiculo= (JSONObject) datos.get("vehiculo");
    	String patente= (String) datos_vehiculo.get("patente");
    	Vehiculo vehiculo = (Vehiculo) this.buscarPorClaveCandidata("Vehiculo", patente);
    	if(vehiculo!=null){
    		throw new ExceptionViajesCompartidos("ERROR: EXISTE UN VEHICULO CON ESA PATENTE");
    	}
    	
    	entitymanager.getTransaction( ).begin( );
    	vehiculo = new Vehiculo();
    	vehiculo.setAnio( (Integer)datos_vehiculo.get("anio") );
    	vehiculo.setFecha_verificacion(null);
    	vehiculo.setMarca( (String)datos_vehiculo.get("marca") );
    	vehiculo.setModelo( (String)datos_vehiculo.get("modelo") );
    	vehiculo.setPatente( (String)datos_vehiculo.get("patente") );
    	//estos los pongo por q los pide la BD, abria q ir haciendo los enum
    	vehiculo.setEstado('A');
    	vehiculo.setVerificado('N');
    	
    	cliente.asignarVehiculo(vehiculo); 
    	try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
    	
    	return true;
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

	public boolean nuevoViaje(JSONObject datos) throws ExceptionViajesCompartidos {
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
		/*
		EL JSON QUE RECIBE EL METODO TENDRIA LA SIGUIENTE FORMA:
		 { "LOCALIDADES": {"ORIGEN":"ID_LOCALIDAD","INTERMEDIO":ID_LOCALIDAD,.....,"DESTINO":ID_LOCALIDAD},
		 "VEHICULO": ID_VEHICULO,
		 "VIAJE": {FECHA_SALIDA, HS_SALIDA, CANTIDAD_ASIENTOS, NOMBRE_AMIGABLE, COSTO_VIAJE},
		 "VUELTA": {FECHA_SALIDA,HS_SALIDA,CANTIDAD_ASIENTOS, NOMBRE_AMIGABLE},
		 "CLIENTE":ID_CLIENTE
		 }
		*/
		
		Integer id_cliente= (Integer) datos.get("cliente");
		Cliente cliente= (Cliente)this.buscarPorPrimaryKey(new Cliente(), id_cliente);
		if (cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
		String id_vehiculo = (String) datos.get("vehiculo");
		Vehiculo vehiculo = (Vehiculo) this.buscarPorClaveCandidata("Vehiculo", id_vehiculo);
		if(vehiculo==null){
			throw new ExceptionViajesCompartidos("ERROR: EL VEHICULO NO EXISTE");
		}
		//METODO QUE VERIFICA SI UN CLIENTE TIENE UNA RELACION ACTUAL CON ESE VEHICULO
		if( cliente.puedeManejar(vehiculo)==false ){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO MANEJA ESE VEHICULO");
		}
		this.entitymanager.getTransaction().begin();
		//pongo quien es el conductor del viaje y en q vehiculo
		Maneja maneja= this.buscarManeja(cliente, vehiculo);
		Viaje viaje= new Viaje();
		viaje.setConductor_vehiculo(maneja);
		//seteo los otros datos
		viaje.setFecha_inicio((Date) datos.get("fecha_inicio"));
		viaje.setFecha_alta(new Date((new java.util.Date()).getTime()));
		viaje.setFecha_cancelacion(null);
		viaje.setFecha_finalizacion(null);
		viaje.setEstado('a');		//falta hacer los enum	
		
		viaje.setAsientos_disponibles((Integer) datos.get("cantidad_asientos"));
		viaje.setNombre_amigable((String) datos.get("nombre_amigable"));
		//viaje.setCosto_viaje((String)datos.get("costo_viaje"));		//falta en viaje
		
		//creo el recorrido (lista de localidades) que tiene el viaje
		// le JSON datos tiene un JSON que se llama localidades, q tiene origen, destino y todos los puntos intermedios
		JSONObject localidades =(JSONObject) datos.get("localidades");
		if(localidades==null){
			throw new ExceptionViajesCompartidos("ERROR: FALTAN TODAS LAS LOCALIDADES DEL VIAJE");
		}
		Integer id_origen = (Integer) localidades.get("origen");
		if(id_origen==null){
			throw new ExceptionViajesCompartidos("ERROR: FALTA EL ORIGEN DEL VIAJE");
		}
		Integer id_destino = (Integer) localidades.get("destino");
		if(id_destino==null){
			throw new ExceptionViajesCompartidos("ERROR: FALTA EL DESTINO DEL VIAJE");
		}
		JSONArray intermedios = (JSONArray) localidades.get("intermedios");
		ArrayList<Localidad> recorrido= new ArrayList<Localidad>();
		//armo la lista de localidades en ORDEN
		recorrido.add( (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_origen) );
		if(intermedios!=null){
			for(Object o: intermedios){
				recorrido.add( (Localidad) this.buscarPorPrimaryKey(new Localidad(), o) );			
			}
		}
		recorrido.add( (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_destino) );
		viaje.crearRecorrido(recorrido);
		
		//si el viaje tiene marcado que es de ida y vuelta, le digo al viaje q cree la vuelta y le paso
		//los datos de la misma
		JSONObject vuelta=(JSONObject) datos.get("vuelta");
		if(vuelta!=null){
			try{
				this.entitymanager.persist(viaje);
	    		entitymanager.getTransaction( ).commit( );	
	    	}catch(RollbackException e){
	    		String error= ManejadorErrores.parsearRollback(e);
	    		throw new ExceptionViajesCompartidos("ERROR: "+error);
	    	}
			entitymanager.getTransaction().begin();
			viaje.crearTuVuelta(vuelta);
			try{
	    		entitymanager.getTransaction( ).commit( );	
	    	}catch(RollbackException e){
	    		String error= ManejadorErrores.parsearRollback(e);
	    		throw new ExceptionViajesCompartidos("ERROR: "+error);
	    	}
		}else{
			viaje.setViaje_complementario(null);
			this.entitymanager.persist(viaje);
			try{
	    		entitymanager.getTransaction( ).commit( );	
	    	}catch(RollbackException e){
	    		String error= ManejadorErrores.parsearRollback(e);
	    		throw new ExceptionViajesCompartidos("ERROR: "+error);
	    	}
		}
		

		
		return true;
		
		/*
		 * if(vuelta!=null){
			viaje.crearTuVuelta(vuelta);
		}else{
			viaje.setViaje_complementario(null);
		}
		this.entitymanager.persist(viaje);

		try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
		return true;
		 */
	}

	public Maneja buscarManeja(Cliente id_cliente, Vehiculo id_vehiculo){
		//podria ser resuelto por un buscar por pk compuesta en el DAO general
		//agregado fede
		/*
                Maneja conductor_vehiculo = new Maneja();    
                Query qry = entitymanager.createNamedQuery("Maneja.SearchById");
    		qry.setParameter("id_cliente",id_cliente);
                qry.setParameter("id_vehiculo",id_vehiculo);
    		conductor_vehiculo =(Maneja)qry.getSingleResult();
                
                
		return conductor_vehiculo;
		*/
		//NO TE ENOJES FEDE, ESTE ESTA PROBADO, SI QUIERES PROBA EL TUYO Y REEMPLAZA A ESTE
		Maneja maneja=(Maneja) this.buscarPorIDCompuesta("Maneja",id_cliente,id_vehiculo);
		return maneja;
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

	//metodo que borra todas las relaciones entre los viajes, para poder eliminarlos despues.
	public void borrarRelacionesEntreViajes() {
		this.entitymanager.getTransaction().begin();
		List<Viaje> viajes=this.selectAll("Viaje");
		for(Viaje v: viajes){
			v.setViaje_complementario(null);
		}
		this.entitymanager.getTransaction().commit();
	}
}
