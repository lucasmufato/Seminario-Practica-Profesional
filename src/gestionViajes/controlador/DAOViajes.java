package gestionViajes.controlador;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionComisiones.modelo.ComisionCobrada;
import gestionPuntos.modelo.Calificacion;
import gestionPuntos.modelo.EstadoClasificacion;
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
    
    public boolean NuevoVehiculo(JSONObject datos) throws ExceptionViajesCompartidos{	//tiene tests
    	
    	/* estructura del JSON datos:
    	 * { "CONDUCTOR": ID_USUARIO,
    	 * "VEHICULO":{FOTO,MARCA,MODELO, PATENTE,ANIO,AIRE_ACOND,SEGURO,foto,seguro, aire, asientos}
    	 * }
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
    	vehiculo.setAire_acondicionado( (Character) datos_vehiculo.get("aire"));
    	vehiculo.setColor((String) datos_vehiculo.get("color"));
    	vehiculo.setCantidad_asientos( (Integer) datos_vehiculo.get("asientos"));
    	vehiculo.setSeguro( (Character) datos_vehiculo.get("seguro"));
    	//vehiculo.setFoto(foto);		//TODO falta guardar la foto del auto
    	
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
    
    /*
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
	*/
	
	public Vehiculo getAutoViaje(Integer id_viaje) {
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

	public boolean nuevoViaje(JSONObject datos) throws ExceptionViajesCompartidos {		//tiene tests
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
		viaje.setFecha_inicio((Timestamp) datos.get("fecha_inicio"));
		viaje.setFecha_alta(new Timestamp((new java.util.Date()).getTime()));
		viaje.setFecha_cancelacion(null);
		viaje.setFecha_finalizacion(null);
		viaje.setEstado(EstadoViaje.no_iniciado);		//falta hacer los enum	
		
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
		
		//si el viaje tiene marcado que es de ida y vuelta, le digo al viaje q cree la vuelta y le paso los datos de la misma
		JSONObject vuelta=(JSONObject) datos.get("vuelta");
		if(vuelta!=null){ 
			try{	//SI EL VIAJE TIENE VUELTA, GUARDO EL PRIMER VIAJE EN LA BD
				this.entitymanager.persist(viaje);
	    		entitymanager.getTransaction( ).commit( );	
	    	}catch(RollbackException e){
	    		String error= ManejadorErrores.parsearRollback(e);
	    		throw new ExceptionViajesCompartidos("ERROR: "+error);
	    	}
			//HAGO OTRA TRANSACCION, Y AHI LE DIGO AL VIAJE Q CREE SU VUELTA, Y LO GUARDO EN LA BD (EL NUEVO VIAJE SE GUARDA POR PERSIST EN CASCADA)
			entitymanager.getTransaction().begin();
			viaje.crearTuVuelta(vuelta);
			try{
	    		entitymanager.getTransaction( ).commit( );	
	    	}catch(RollbackException e){
	    		String error= ManejadorErrores.parsearRollback(e);
	    		throw new ExceptionViajesCompartidos("ERROR: "+error);
	    	}
		}else{		//SI EL VIAJE NO TIENE VUELTA, LA SETEO COMO NULA, Y GUARDO EL VIAJE
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
	
	public Cliente getConductorViaje(Integer id_viaje){		//tiene test
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			return null;		// o puede tirar una exception
		}else{
			return viaje.getConductor_vehiculo().getCliente();
		}	
	}
	
	public Vehiculo getVehiculoViaje(Integer id_viaje){		//tiene test
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			return null;		// o puede tirar una exception
		}else{
			return viaje.getConductor_vehiculo().getVehiculo();
		}
	}
	
	public boolean Cliente_se_postula_en_viaje(JSONObject json) throws ExceptionViajesCompartidos{//no testeado todavia
		/*
		 * JSON{
		 * "CLIENTE":ID_CLIENTE,
		 * "VIAJE":ID_VIAJE,
		 * "LOCALIDAD_SUBIDA":ID_LOCALIDAD,
		 * "LOCALIDAD_BAJADA": ID_LOCALIDAD
		 * } 
		 */
		/*
		 * recuperar cliente
		 * recuperar viaje
		 * viaje.addpasajero(cliente, localidad subida, localidad bajada)
		 * dao.notificar_usuario()
		 * crear_comision_por_pasajero estado pendiente
		 * 
		 */
		Integer id_cliente= (Integer) json.get("cliente");
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: CLIENTE NO ENCONTRADO");
		}
		Integer id_viaje = (Integer) json.get("viaje");
		Viaje viaje = (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			throw new ExceptionViajesCompartidos("ERROR: VIAJE NO ENCONTRAD");
		}
		Integer id_subida= (Integer) json.get("localidad_subida");
		Localidad localidad_subida= (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_subida);
		if(localidad_subida==null){
			throw new ExceptionViajesCompartidos("ERROR: LA LOCALIDAD DE SUBIDA NO EXISTE");
		}
		Integer id_bajada= (Integer) json.get("localidad_bajada");
		Localidad localidad_bajada= (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_bajada);
		if(localidad_bajada==null){
			throw new ExceptionViajesCompartidos("ERROR: LA LOCALIDAD DE BAJADA NO EXISTE");
		}
		if(viaje.getEstado()==EstadoViaje.cancelado || viaje.getEstado()==EstadoViaje.finalizado){
			throw new ExceptionViajesCompartidos("ERROR: NO PUEDE POSTULARSE A UN VIAJE FINALIZADO/CANCELADO");
		}
		if( cliente.getId_usuario()==viaje.getConductor_vehiculo().getCliente().getId_usuario() ){
			throw new ExceptionViajesCompartidos("ERROR: NO PUEDES UNIRTE COMO PASAJERO A UN VIAJE QUE VOS MISMO CREASTE");
		}
		if (!viaje.contiene_localidades_en_orden(localidad_subida,localidad_bajada)){
			throw new ExceptionViajesCompartidos("ERROR: NO LA LOCALIDAD DE SUBIDA ES DESPUES DE LA DE BAJADA");
		}
		this.entitymanager.getTransaction().begin();
		PasajeroViaje pasajero= new PasajeroViaje();
		pasajero.setCalificacion(null);
		pasajero.setCliente(cliente);
		pasajero.setEstado(EstadoPasajeroViaje.postulado);
		pasajero.setKilometros(0);			//TODO falta hacer la parte de los kilometros
		pasajero.setComision(null);			// y de calcular la comision
		
		viaje.aniadir_pasajeroViaje(pasajero, localidad_subida, localidad_bajada);
		
		//	TODO la parte de crear la comision
		ComisionCobrada comisionCobrada = new ComisionCobrada();
		comisionCobrada.setMonto(0);
		comisionCobrada.setMovimiento_saldo(null);
		comisionCobrada.setComision(null);
		comisionCobrada.setPasajero_viaje(null);
		
		// TODO la parte de calificacion bien
		Calificacion calificacion = new Calificacion();
		calificacion.setMovimiento_puntos(null);
		calificacion.setParticipo(EstadoClasificacion.pendiente_ambos);
		calificacion.setPasajero_viaje(null);
		
		//le asigno la calificacion y la comision al pasajero
		pasajero.setCalificacion(calificacion);
		pasajero.setComision(comisionCobrada);

		this.entitymanager.persist(calificacion);
		this.entitymanager.persist(comisionCobrada);
		this.entitymanager.persist(pasajero);
		this.entitymanager.getTransaction().commit();
		this.entitymanager.getTransaction().begin();
		
		//creo la notificacion que le va a llegar al conductor de ese viaje, informandole que tiene un postulante
		Notificacion notificacion= new Notificacion();
		notificacion.setCliente(viaje.getConductor());
		notificacion.setEstado(EstadoNotificacion.no_leido);
		notificacion.setFecha(new Timestamp((new java.util.Date()).getTime()) ); 			//TODO modificar fecha
		notificacion.setTexto("El usuario: "+cliente.getNombre_usuario()+
				" se ha postulado para participar en tu viaje: "+viaje.getNombre_amigable());
		this.entitymanager.persist(notificacion);
		calificacion.setPasajero_viaje(pasajero);
		comisionCobrada.setPasajero_viaje(pasajero);
		this.entitymanager.getTransaction().commit();
		return true;
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
	
	public Viaje getViajeById(Integer id_viaje) {
		Viaje viaje;
		viaje = (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		return viaje;
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

	public List<Viaje> listarViajesPorConductor(Integer id_conductor) {
		Cliente conductor = (Cliente) this.buscarPorPrimaryKey (new Cliente(), id_conductor);
		Query qry = entitymanager.createNamedQuery("Viaje.SearchByConductor");
    	qry.setParameter("conductor", conductor);
		return (List<Viaje>)qry.getResultList();
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
	
	public List<Notificacion> notificacionesPorCliente(Integer id_cliente){
		//TODO
		return null;
	}
	
	public boolean tieneNotificaciones(Integer id_cliente){
		//TODO
		return false;
	}

	//metodo que borra todas las relaciones entre los viajes, para poder eliminarlos despues.
	@Deprecated		//le puse q es deprecated para q no lo vaya a usar sin querer y hacer boleta la BD jjajajajaja
	public void borrarRelacionesEntreViajes() {
		this.entitymanager.getTransaction().begin();
		List<Viaje> viajes=this.selectAll("Viaje");
		for(Viaje v: viajes){
			v.setViaje_complementario(null);
		}
		this.entitymanager.getTransaction().commit();
	}
}
