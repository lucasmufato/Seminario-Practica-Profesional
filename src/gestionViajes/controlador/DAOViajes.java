package gestionViajes.controlador;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionComisiones.modelo.Comision;
import gestionComisiones.modelo.ComisionCobrada;
import gestionComisiones.modelo.EstadoComisionCobrada;
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

/*import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;*/

public class DAOViajes extends DataAccesObject {

    public DAOViajes(){
    	super();
    }
    
    //by mufa
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
    	
    	if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
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
    
    /* //comentado por mufa
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
	
	//by mufa
	@SuppressWarnings("unused")
	public boolean nuevoViaje(JSONObject datos) throws ExceptionViajesCompartidos {		//tiene tests
		/*
		EL JSON QUE RECIBE EL METODO TENDRIA LA SIGUIENTE FORMA:
		 { "LOCALIDADES": {"ORIGEN":"ID_LOCALIDAD","INTERMEDIO":ID_LOCALIDAD,.....,"DESTINO":ID_LOCALIDAD},
		 "VEHICULO": ID_VEHICULO,
		 "VIAJE": {FECHA_SALIDA, NOMBRE_AMIGABLE, COSTO_VIAJE},
		 "VUELTA": {FECHA_SALIDA},
		 "CLIENTE":ID_CLIENTE,
		 "cantidad_asientos"=asientos,
		 }
		*/
		JSONObject d_viaje= (JSONObject) datos.get("viaje");
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
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		//pongo quien es el conductor del viaje y en q vehiculo
		Maneja maneja= this.buscarManeja(cliente, vehiculo);
		Viaje viaje= new Viaje();
		viaje.setConductor_vehiculo(maneja);
		//seteo los otros datos
		Timestamp fecha_inicio= (Timestamp) d_viaje.get("fecha_inicio");
		if(fecha_inicio.before(new Timestamp((new java.util.Date()).getTime()))){
			throw new ExceptionViajesCompartidos("ERROR:LA FECHA DE INICIO NO PUEDE SER ANTERIOR A LA ACTUAL");
		}
		if(fecha_inicio==null){
			throw new ExceptionViajesCompartidos("ERROR: EL VIAJE NO TIENE FECHA DE INICIO");
		}
		viaje.setFecha_inicio(fecha_inicio);
		viaje.setFecha_alta(new Timestamp((new java.util.Date()).getTime()));
		viaje.setFecha_cancelacion(null);
		viaje.setFecha_finalizacion(null);
		viaje.setEstado(EstadoViaje.no_iniciado);
		
		Integer cantidad_asientos = (Integer) d_viaje.get("cantidad_asientos");
		if(cantidad_asientos==null){
			throw new ExceptionViajesCompartidos("ERROR: NO INGRESO LA CANTIDAD DE ASIENTOS DISPONIBLES");
		}
		if (cantidad_asientos>maneja.getVehiculo().getCantidad_asientos()){
			throw new ExceptionViajesCompartidos("ERROR: INGRESO MAS ASIENTOS DISPONIBLES QUE LA CANTIDAD DE ASIENTOS DEL VEHICULO");
		}
		viaje.setAsientos_disponibles(cantidad_asientos);
		String nombre_amigable = (String) d_viaje.get("nombre_amigable");
		if(nombre_amigable==null){
			nombre_amigable="Viaje sin nombre "+fecha_inicio.toString();
		}
		viaje.setNombre_amigable(nombre_amigable);
		
		Float precio=(Float) d_viaje.get("precio");
		if(precio==null){
			throw new ExceptionViajesCompartidos("ERROR: EL VIAJE NO TIENE PRECIO");
		}
		if(precio<0){
			throw new ExceptionViajesCompartidos("ERROR: EL PRECIO NO PUEDE SER NEGATIVO");
		}
		viaje.setPrecio(precio);
		
		
		// chequeo los datos de la vuelta
		Timestamp fecha_vuelta = null;
		Integer cantidad_asientos_vuelta=0;
		Float precio_vuelta=0f;
		
		JSONObject vuelta=(JSONObject) datos.get("vuelta");
		if(vuelta!=null){
			fecha_vuelta= (Timestamp) vuelta.get("fecha_inicio");
			if(fecha_vuelta.before(fecha_inicio)){
				throw new ExceptionViajesCompartidos("ERROR:LA FECHA DE INICIO DEL VIAJE DE VUELTA NO PUEDE SER ANTERIOR A LA FECHA DEL VIAJE DE IDA");
			}
			if(fecha_vuelta==null){
				throw new ExceptionViajesCompartidos("ERROR: EL VIAJE DE VUELTA NO TIENE FECHA DE INICIO");
			}
			cantidad_asientos_vuelta = (Integer) vuelta.get("cantidad_asientos");
			if(cantidad_asientos_vuelta==null){
				throw new ExceptionViajesCompartidos("ERROR: NO INGRESO LA CANTIDAD DE ASIENTOS DISPONIBLES PARA LA VUELTA");
			}

			if (cantidad_asientos_vuelta>maneja.getVehiculo().getCantidad_asientos()){
				throw new ExceptionViajesCompartidos("ERROR: INGRESO MAS ASIENTOS DISPONIBLES QUE LA CANTIDAD DE ASIENTOS DEL VEHICULO PARA LA VUELTA");
			}
			precio_vuelta=(Float) vuelta.get("precio");
			if(precio_vuelta == null) {
				throw new ExceptionViajesCompartidos("ERROR: NO INGRESO PRECIO PARA LA VUELTA");
			}
		}
		
		
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
		//armo la lista de localidades en ORDEN, el ordinal lo pone el viaje
		Localidad origen= (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_origen);
		if(origen==null){
			throw new ExceptionViajesCompartidos("ERROR: EL ORIGEN NO EXISTE EN EL SISTEMA");
		}
		recorrido.add(origen);
		if(intermedios!=null){
			for(Object o: intermedios){
				origen =(Localidad) this.buscarPorPrimaryKey(new Localidad(), o);
				if(origen==null){
					throw new ExceptionViajesCompartidos("ERROR: UN PUNTO INTERMEDIO NO EXISTE EN EL SISTEMA (NOSE CUAL :-( )");
				}
				recorrido.add( origen );			
			}
		}
		origen=(Localidad) this.buscarPorPrimaryKey(new Localidad(), id_destino); //cambio el origen por el destino, mantengo la variable
		if(origen==null){
			throw new ExceptionViajesCompartidos("ERROR: EL DESTINO NO EXISTE EN EL SISTEMA");
		}
		recorrido.add(origen);
		viaje.crearRecorrido(recorrido);
		
		List<LocalidadViaje> lista_localidad_viaje=viaje.getLocalidades();
		for(int i=0;i<(lista_localidad_viaje.size()-1);i++){
			Double distancia = this.distanciaEntreLocalidades(lista_localidad_viaje.get(i).getLocalidad(),lista_localidad_viaje.get(i+1).getLocalidad());
			lista_localidad_viaje.get(i).setKms_a_localidad_siguiente(distancia);
		}
		Integer ultimo=lista_localidad_viaje.size();
		lista_localidad_viaje.get(ultimo-1).setKms_a_localidad_siguiente(0.0);		//a la ultima localidadViaje le pongo distancia 0
		
		//si el viaje tiene marcado que es de ida y vuelta, le digo al viaje q cree la vuelta y le paso los datos de la misma
		vuelta=(JSONObject) datos.get("vuelta");
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
			Viaje viaje_vuelta = viaje.crearTuVuelta(vuelta);
			viaje_vuelta.setPrecio(precio_vuelta);
			viaje_vuelta.setAsientos_disponibles(cantidad_asientos_vuelta);
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
	
	//by pablo
	protected Double distanciaEntreLocalidades(Localidad localidad1, Localidad localidad2){
	/* 
		GeoApiContext context = new GeoApiContext ();

		// La apikey no deberia estar hardcodeada
		//juan: creo que en java se guardan en un objeto property o algo por el estilo
		//en node.js yo lo metia en un archivo txt y lo cargaba con un modulo standard pero en Java ni idea
		String claveJuan = "AIzaSyC83dfqf-uzt4fyBckfpNa51sToN6t7kZA";
		String clavePablo = "AIzaSyCu2P6zmQmOyESf872DSdZgYam9PMJnzwg";
		context.setApiKey(clavePablo);

		// TODO: permitir configurar proxy 
		//context.setProxy(proxy)

		LatLng origen = new LatLng(localidad1.getLatitud(), localidad1.getLongitud());
		LatLng destino = new LatLng(localidad2.getLatitud(), localidad2.getLongitud());

		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest (context);
		request.origins(origen);
		request.destinations(destino);
		request.mode(TravelMode.DRIVING);

		long mts = -1;
		
		try {
			long distance;
			DistanceMatrix result = request.await();
			for (DistanceMatrixElement element: result.rows[0].elements) {
					distance = element.distance.inMeters;
				if ((element.status == DistanceMatrixElementStatus.OK) && (mts == -1 || distance < mts)) {
					mts = distance;
				}
			}
		} catch (Exception e) {
			//throw new ExceptionViajesCompartidos ("No se pudo calcular la distancia entre las localidades");
		}

		if (mts == -1) {
			//throw new ExceptionViajesCompartidos ("No se pudo calcular la distancia entre las localidades");
		}

		double kms = (double) mts / 1000d;
		return kms;
		*/
		return this.distanceCalculation(localidad1.getLatitud(), localidad1.getLongitud(), localidad2.getLatitud(), localidad2.getLongitud());
	}
	
	protected double distanceCalculation(double point1_lat, double point1_long, double point2_lat, double point2_long) {
	/*
	Descripción: Cálculo de la distancia entre 2 puntos en función de su latitud/longitud
	Autor: Rajesh Singh (2014)
	Sito web: AssemblySys.com
	*/
	// Cálculo de la distancia en grados
	double degrees = Math.toDegrees(Math.acos((Math.sin(Math.toRadians(point1_lat))*Math.sin(Math.toRadians(point2_lat))) + (Math.cos(Math.toRadians(point1_lat))*Math.cos(Math.toRadians(point2_lat))*Math.cos(Math.toRadians(point1_long-point2_long)))));
 
	double distance = degrees * 111.13384; // 1 grado = 111.13384 km, basándose en el diametro promedio de la Tierra (12.735 km)
	return distance;
}
	
	//by mufa
	public Maneja buscarManeja(Cliente id_cliente, Vehiculo id_vehiculo){ //tiene test
		Maneja maneja=(Maneja) this.buscarPorIDCompuesta("Maneja",id_cliente,id_vehiculo);
		return maneja;
	}
	
	//by mufa
	public Cliente getConductorViaje(Integer id_viaje){		//tiene test
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			return null;		// o puede tirar una exception
		}else{
			return viaje.getConductor_vehiculo().getCliente();
		}	
	}
	
	//by mufa
	public Vehiculo getVehiculoViaje(Integer id_viaje){		//tiene test
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			return null;		// o puede tirar una exception
		}else{
			return viaje.getConductor_vehiculo().getVehiculo();
		}
	}
	
	//by mufa
	public boolean Cliente_se_postula_en_viaje(JSONObject json) throws ExceptionViajesCompartidos{// tiene test
		/*
		 * JSON{
		 * "CLIENTE":ID_CLIENTE,
		 * "VIAJE":ID_VIAJE,
		 * "LOCALIDAD_SUBIDA":ID_LOCALIDAD,
		 * "LOCALIDAD_BAJADA": ID_LOCALIDAD
		 * } 
		 */
		
		Integer id_cliente= (Integer) json.get("cliente");
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: CLIENTE NO ENCONTRADO");
		}
		Integer id_viaje = (Integer) json.get("viaje");
		Viaje viaje = (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			throw new ExceptionViajesCompartidos("ERROR: VIAJE NO ENCONTRADO");
		}
		/*
		 * Nota de juan
		 * Bajo esta restriccion el cliente no se puede postular en dos tramos diferentes
		 * Si, es un boludito si hace eso pero lo anoto para que se sepa nomas.
		 */
		if( viaje.recuperar_pasajeroViaje_por_cliente(cliente) != null ){
			throw new ExceptionViajesCompartidos("ERROR: YA ESTAS POSTULADO A ESTE VIAJE");
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
			throw new ExceptionViajesCompartidos("ERROR: LA LOCALIDAD DE SUBIDA ESTA DESPUES QUE LA DE BAJADA");
		}
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		PasajeroViaje pasajero= new PasajeroViaje();
		pasajero.setCalificacion(null);
		pasajero.setCliente(cliente);
		pasajero.setEstado(EstadoPasajeroViaje.postulado);
		
		Double km = viaje.calcularKM(localidad_subida,localidad_bajada);
		pasajero.setKilometros(km);
		pasajero.setComision(null);
		
		viaje.aniadir_pasajeroViaje(pasajero, localidad_subida, localidad_bajada);
		
		//	TODO la parte de crear la comision
		ComisionCobrada comisionCobrada = Comision.NuevaComisionCobrada(km);	//este metodo falta!! tendria q devolver la comision que se le cobraria
		comisionCobrada.setMovimiento_saldo(null);
		comisionCobrada.setPasajero_viaje(null);
		comisionCobrada.setEstado(EstadoComisionCobrada.informativa);		//significa que se guarda solo para saber cuanto le dijimos q le ibamos a cobrar cuando se postulo
		
		//le asigno la comision al pasajero
		pasajero.setComision(comisionCobrada);

		this.entitymanager.persist(comisionCobrada);
		this.entitymanager.persist(pasajero);
		this.entitymanager.getTransaction().commit();
		//hago un guardado anterior por que no puedo vincular doblemente al pasajero con la calificacion y a la calificacion con el pasajero
		this.entitymanager.getTransaction().begin();
		
		//creo la notificacion que le va a llegar al conductor de ese viaje, informandole que tiene un postulante
		Notificacion notificacion= new Notificacion();
		notificacion.setCliente(viaje.getConductor());
		notificacion.setEstado(EstadoNotificacion.no_leido);
		notificacion.setFecha(new Timestamp((new java.util.Date()).getTime()) ); 
		notificacion.setTexto("El usuario: "+cliente.getNombre_usuario()+
				" se ha postulado para participar en tu viaje: "+viaje.getNombre_amigable());
		this.entitymanager.persist(notificacion);
		//ahora que ya tengo guardado el pasajero en la BD, la calificacion y la comision tiene una entidad a la cual apuntar
		comisionCobrada.setPasajero_viaje(pasajero);
		try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
		return true;
	}

	//by mufa
	public float comision_por_recorrido(Localidad inicio, Localidad destino, Integer id_viaje) throws ExceptionViajesCompartidos{	//tiene test
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			throw new ExceptionViajesCompartidos("ERROR: VIAJE NO ENCONTRADO");
		}
		if (!viaje.contiene_localidades_en_orden(inicio, destino) ){
			throw new ExceptionViajesCompartidos("ERROR: LAS LOCALIDADES NO EXISTEN O NO ESTAN EN ORDEN");
		}
		Double km = viaje.calcularKM(inicio,destino);
		ComisionCobrada comisionCobrada = Comision.NuevaComisionCobrada(km);
		return comisionCobrada.getMonto();
	}
	
	public Viaje getViajeById(Integer id_viaje) {
		Viaje viaje;
		viaje = (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		return viaje;
	}
	
	//by pablo y lucas
	public List<Viaje> buscarViajes(JSONObject busqueda) throws ExceptionViajesCompartidos{
		//crear query (campos obligatorios: origen, destino,fecha_desde)
		// otros campos: fecha hasta, conductor, estado_viaje
		
		Integer id_origen = (Integer) busqueda.get("origen");
		if(id_origen==null){
			throw new ExceptionViajesCompartidos("ERROR: FALTA EL ORIGEN");
		}
		Localidad origen = (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_origen);
		if(origen==null){
			throw new ExceptionViajesCompartidos("ERROR: El ORIGEN NO SE ENCUENTRA EN EL SISTEMA");
		}
		Integer id_destino = (Integer) busqueda.get("destino");
		if(id_origen==null){
			throw new ExceptionViajesCompartidos("ERROR: FALTA EL DESTINO");
		}
		Localidad destino = (Localidad) this.buscarPorPrimaryKey(new Localidad(), id_destino);
		if(destino==null){
			throw new ExceptionViajesCompartidos("ERROR: El DESTINO NO SE ENCUENTRA EN EL SISTEMA");
		}
		Timestamp fecha_desde = (Timestamp) busqueda.get("fecha_desde");
		if(fecha_desde==null){
			throw new ExceptionViajesCompartidos("ERROR: FALTA LA FECHA DESDE");
		}else{
			if(fecha_desde.before( new Timestamp((new java.util.Date()).getTime()) ) ){
				//throw new ExceptionViajesCompartidos("ERROR: FECHA DESDE NO PUEDE SER ANTERIOR A LA FECHA ACTUAL");
			}
		}
		
		Timestamp fecha_hasta =(Timestamp) busqueda.get("fecha_hasta");
		boolean b_fecha_hasta=false;
		if(fecha_hasta!=null){
			if(fecha_hasta.before( new Timestamp((new java.util.Date()).getTime()) ) ){
				throw new ExceptionViajesCompartidos("ERROR: FECHA HASTA NO PUEDE SER ANTERIOR A LA FECHA ACTUAL");
			}
			b_fecha_hasta=true;
		}
		
		String conductor = (String) busqueda.get("conductor");
		boolean b_conductor=false;
		if(conductor!=null){
			b_conductor=true;
		}
		
		String estado = (String) busqueda.get("estado");
		if(estado==null){
			estado="ambas";
		}
		if( !(estado.equals("ambas") ||estado.equals("iniciado") || estado.equals("no_iniciado")) ){
			//si el estado no es iniciado, ambas o no iniciado tiro error
			throw new ExceptionViajesCompartidos("ERROR: ESTADO DEL VIAJE INCORRECTO (ESTADOS POSIBLES: INICIADO, NO INICIADO, AMBOS)");
		}
		// esta es la query base

		String query="SELECT v from Viaje v "
			+"WHERE :fecha_desde < v.fecha_inicio "
			+"AND EXISTS (SELECT l1 FROM LocalidadViaje l1 WHERE l1.viaje=v AND l1.localidad=:origen "
			+"AND EXISTS (SELECT l2 FROM LocalidadViaje l2 WHERE l2.viaje=v AND l2.localidad=:destino "
			+"AND l1.ordinal < l2.ordinal))";
		//ahora le agrego a la query los otros campos de busqueda a medida q los hay
		if(b_fecha_hasta){
			query.concat("AND v.fecha_inicio < :fecha_hasta ");
		}
		if(b_conductor){
			//query.concat("AND v LIKE :nombre_usuario_conductor ");
		}
		String query_estado = "";
		if(estado.equals("ambas")){
			query_estado= "AND v.estado=:estado1 OR v.estado=:estado2 ";
		}else{
			query_estado ="AND v.estado= :estado1";
		}
		
		//armo la query y pongo los parametros minimos
		Query q=this.entitymanager.createQuery(query+query_estado);
		q.setParameter("fecha_desde", fecha_desde);
		q.setParameter("origen", origen);
		q.setParameter("destino", destino);
		
		//ahora agrego el resto de los parametros
		if(b_fecha_hasta){
			q.setParameter("fecha_hasta",fecha_hasta);
		}
		if(b_conductor){
			//query.setParameter("nombre_usuario_conductor",conductor);
		}
		if(estado.equals("ambas")){
			q.setParameter("estado1", EstadoViaje.iniciado);
			q.setParameter("estado2", EstadoViaje.no_iniciado);
		}else{
			if(estado.equals("iniciado")){
				q.setParameter("estado1", EstadoViaje.iniciado);
			}else{
				q.setParameter("estado1", EstadoViaje.no_iniciado);
			}
		}
		List<Viaje> viajes= q.getResultList();
		return viajes;
	}
	
	public List<PasajeroViaje> listarPasajerosPorViaje(Integer id_viaje) {
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		return viaje.getPasajeros();
	}

	public List<Viaje> listarViajesPorConductor(Integer id_conductor) {
		Cliente conductor = (Cliente) this.buscarPorPrimaryKey (new Cliente(), id_conductor);
		Query qry = entitymanager.createNamedQuery("Viaje.SearchByConductor");
    	qry.setParameter("conductor", conductor);
		return (List<Viaje>)qry.getResultList();
	}

	//by mufa
	public String nombreAmigablePorViaje(Integer id_viaje) {
		Viaje v=(Viaje)this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(v==null){
			return null;
		}else{
			return  v.getNombre_amigable();
		}
	}

	//by jasmin y luz
	public boolean aceptarPasajero(Integer id_cliente_postulante, Integer id_viaje) throws ExceptionViajesCompartidos {
		/*
		 * recupero viaje
		 * recupero pasajero viaje
		 * pasajero viaje estado=aceptado/rechazado
		 * notificar al pasajero
		 */
		Viaje viaje = (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if (viaje == null) {
			throw new ExceptionViajesCompartidos("ERROR: EL VIAJE NO EXISTE");
		}
		if(viaje.getEstado() == EstadoViaje.cancelado || viaje.getEstado() == EstadoViaje.finalizado){
			throw new ExceptionViajesCompartidos("ERROR: EL VIAJE ESTA CANCELADO O YA FINALIZO");
		}
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente_postulante);
		if(cliente == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
		PasajeroViaje pasajero = viaje.recuperar_pasajeroViaje_por_cliente(cliente);
		if (pasajero == null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO PARTICIPA DE ESE VIAJE");
		}
		if(pasajero.getEstado() != EstadoPasajeroViaje.postulado){
			throw new ExceptionViajesCompartidos("ERROR: SOLO PODES ACEPTAR A UN PASAJERO CUYO ESTADO SEA POSTULADO");
		}
		Integer asientos = viaje.getAsientos_disponibles();
		LocalidadViaje bajada = pasajero.getLocalidad_bajada();
		LocalidadViaje subida = pasajero.getLocalidad_subida();
		List<LocalidadViaje> lista = viaje.getLocalidades();
		Integer i = 0;
		while (lista.get(i) != subida) {//WHILE HASTA QUE ENCUENTRA LA LOCALIDAD DE SUBIDA Y TENGO LA POSICION CON I
			i++;
		}
		while (lista.get(i) != bajada) { //WHILE PARA RECORRER DESDE SUBIDA HASTA QUE SEA BAJADA
			if ((asientos - lista.get(i).getCantidad_pasajeros()) == 0) { // SI NO HAY ASIENTOS DISPONIBLES
				//NO SE LO PUEDE ACEPTAR 
				throw new ExceptionViajesCompartidos("ERROR: NO HAY SUFICIENTES ASIENTOS DISPONIBLES PARA EL TRAMO");
			}
			i++;
		} 
		//COMENTO PORQUE CREO QUE COMO EN LA BAJADA NO SE SUMA PASAJERO NO HACE FALTA CHEQUEARLA
		/*if ((asientos - lista.get(i).getCantidad_pasajeros()) == 0) { //SE VERIFICAN LOS ASIENTOS EN LA BAJADA
			//NO SE LO PUEDE ACEPTAR
			throw new ExceptionViajesCompartidos("ERROR: NO HAY SUFICIENTES ASIENTOS DISPONIBLES PARA EL TRAMO");
		}*/ 
		//ACEPTAR
		this.entitymanager.getTransaction().begin();
		pasajero.setEstado(EstadoPasajeroViaje.aceptado);
		i = 0;
		while (lista.get(i) != subida) {//WHILE HASTA QUE ENCUENTRA LA LOCALIDAD DE SUBIDA Y TENGO LA POSICION CON I
			i++;
		}
		while (lista.get(i) != bajada) { //WHILE PARA RECORRER DESDE SUBIDA HASTA QUE SEA BAJADA
			Integer c=lista.get(i).getCantidad_pasajeros();
			c++;
			lista.get(i).setCantidad_pasajeros(c);
			i++;
		} 
		pasajero.getComision().setEstado(EstadoComisionCobrada.pendiente);
		//SE CREA LA NOTIFICACION QUE LE VA A LLEGAR AL PASAJERO, SOBRE QUE FUE ACEPTADO
		Notificacion notificacion= new Notificacion();
		notificacion.setCliente(cliente);
		notificacion.setEstado(EstadoNotificacion.no_leido);
		notificacion.setFecha(new Timestamp((new java.util.Date()).getTime()) ); 
		notificacion.setTexto("El conductor: "+ viaje.getConductor() +
				" lo ha aceptado al viaje: "+viaje.getNombre_amigable());
		this.entitymanager.persist(notificacion);
		try{
		 	entitymanager.getTransaction( ).commit( );	
		}catch(RollbackException e){
		  	String error= ManejadorErrores.parsearRollback(e);
		 	throw new ExceptionViajesCompartidos("ERROR: "+error);
		}
		return true;
	}

	//by mufa
	public boolean rechazarPasajero(Integer id_cliente_postulante, Integer id_viaje) throws ExceptionViajesCompartidos {
		Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(viaje==null){
			throw new ExceptionViajesCompartidos("ERROR: EL VIAJE NO EXISTE");
		}
		if(viaje.getEstado()==EstadoViaje.cancelado || viaje.getEstado()==EstadoViaje.finalizado){
			throw new ExceptionViajesCompartidos("ERROR: EL VIAJE ESTA CANCELADO O YA FINALIZO");
		}
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente_postulante);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE");
		}
		PasajeroViaje pasajero = viaje.recuperar_pasajeroViaje_por_cliente(cliente);
		if (pasajero==null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO PARTICIPA DE ESE VIAJE");
		}
		if(pasajero.getEstado()!=EstadoPasajeroViaje.postulado){	//no podria rechazar a un cliente que ya acepte
			throw new ExceptionViajesCompartidos("ERROR: SOLO PODES RECHAZAR A UN PASAJERO CUYO ESTADO SEA POSTULADO");
		}
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		pasajero.setEstado(EstadoPasajeroViaje.rechazado);
		pasajero.getComision().setEstado(EstadoComisionCobrada.desestimada);
		try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
		return true;
	}
	
	// by mufa
	public boolean clienteNoManejaVehiculo(Integer id_cliente, Integer id_vehiculo) throws ExceptionViajesCompartidos{
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: NO EXISTE EL CLIENTE");
		}
		Vehiculo vehiculo = (Vehiculo) this.buscarPorPrimaryKey(new Vehiculo(), id_vehiculo);
		if(vehiculo==null){
			throw new ExceptionViajesCompartidos("ERROR: EL VEHICULO NO EXISTE");
		}
		Maneja maneja = this.buscarManeja(cliente, vehiculo);
		if(maneja==null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO ESTA RELACIONADO CON LE VEHICULO");
		}
		if(maneja.getFecha_fin()!=null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO PODIA MANEJAR EL VEHICULO");
		}
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		maneja.setFecha_fin( new Timestamp((new java.util.Date()).getTime()) );
		try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
		return true;
	}
	
	//by mufa
	@SuppressWarnings("unchecked")
	public List<Vehiculo> getVehiculosPorCliente(Integer id_cliente) throws ExceptionViajesCompartidos{
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: EL CLIENTE NO EXISTE EN EL SISTEMA");
		}
		Query q= this.entitymanager.createQuery("SELECT v FROM Vehiculo v JOIN v.conductores maneja"
				+ " JOIN maneja.cliente cliente WHERE maneja.fecha_fin = null AND cliente=:cliente ");
		q.setParameter("cliente", cliente);
		return q.getResultList();
	//	return cliente.getVehiculosQueManeja();
	}
	
	//by mufa
	public List<Localidad> getOrigenYDestinoDeViaje(Integer id_viaje){
		Viaje v=(Viaje)this.buscarPorPrimaryKey(new Viaje(), id_viaje);
		if(v==null){
			return null;
		}else{
			return  v.recuperarOrigenYDestino();
		}
	}
	
	public boolean finalizarViaje(Integer id_cliente, Integer id_viaje) throws ExceptionViajesCompartidos{	//sin test TODO
		Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: NO EXISTE EL CLIENTE");
		}
		Viaje viaje = (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_cliente);
		if(cliente==null){
			throw new ExceptionViajesCompartidos("ERROR: NO EXISTE EL VIAJE");
		}
		PasajeroViaje pv= viaje.recuperar_pasajeroViaje_por_cliente(cliente);
		if(pv==null){
			//si el cliente no esta en el viaje, pregunto si es el conductor
			if(viaje.getConductor().getId_usuario()!=cliente.getId_usuario()){
				throw new ExceptionViajesCompartidos("ERROR: NO PARTICIPAS DEL VIAJE!");
			}else{
				//si soy el chofer hago voy a este metodo y termino
				return this.finalizarViaje(viaje);
			}
		}else{
			if(pv.getEstado()!=EstadoPasajeroViaje.aceptado){
				throw new ExceptionViajesCompartidos("ERROR: NO ESTAS ACEPTADO EN EL VIAJE");
			}
		}
		if(viaje.getEstado()!=EstadoViaje.iniciado || viaje.getEstado()!=EstadoViaje.finalizado ){
			throw new ExceptionViajesCompartidos("ERROR: NO PUEDES FINALIZAR TU PARTICIPACION EN UN VIAJE QUE NO ESTA INICIADO O FINALIZADO");
		}
		
		//GUARDO EL CAMBIO DE ESTADO
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		pv.setEstado(EstadoPasajeroViaje.finalizo_viaje);
		try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
		
		return true;
	}

	private boolean finalizarViaje(Viaje viaje) throws ExceptionViajesCompartidos {
		//entro a este metodo solo si quiero finalizar un viaje y soy el conductor
		//este metodo es llamado por el otro finalizar viaje
		
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		viaje.setEstado(EstadoViaje.finalizado);		
		try{
    		entitymanager.getTransaction( ).commit( );	
    	}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
		return true;
	}
	
}
