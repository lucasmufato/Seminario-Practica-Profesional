package gestionViajes.controlador;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import gestionUsuarios.controlador.DAOAdministracionUsuarios;
import gestionUsuarios.modelo.*;
import gestionViajes.modelo.*;
import otros.ExceptionViajesCompartidos;
import otros.AccessManager;


public class ServletViaje extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected DAOAdministracionUsuarios daoUsuarios;
	protected DAOViajes daoViajes;

	public void init() throws ServletException {
		daoUsuarios = new DAOAdministracionUsuarios();
		daoViajes = new DAOViajes();
	}
	
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");
		/* TODO: comprobar permisos */	

		if (entity != null && entity.equals ("viaje")) {
			if (action != null && action.equals ("new")) {
				respuesta = this.nuevo_viaje (request);
			} else if (action != null && action.equals("buscar")) {
				respuesta = this.buscar_viaje (request);
			} else if (action != null && action.equals("detalle")) {
				respuesta = this.ver_viaje_detallado (request);
			} else if (action != null && action.equals("participar")){
				respuesta = this.participar_viaje(request);
			} else if(action != null && action.equals("cancelar_participacion")){
				respuesta = this.cancelar_participacion(request);
			} else if (action != null && action.equals("ver_postulantes")){
				respuesta = this.ver_postulantes(request);
			} else if (action != null && action.equals("ver_mis_viajes")) {

				respuesta = this.ver_mis_viajes (request);
			}
		} else if (entity != null && entity.equals ("vehiculo")) {
			if (action != null && action.equals ("new")) {
				respuesta = this.nuevo_vehiculo (request);
			} else if (action != null && action.equals ("ver_mis_vehiculos")) {
				respuesta = this.listar_vehiculos (request);
			}
		} else {
			respuesta = new JSONObject();
			respuesta.put ("result", false);
			respuesta.put ("msg", "No implementado");
		}
		respuesta.put("entity", entity);
		respuesta.put("action", action);

		System.out.println (respuesta);
		writer.println (respuesta);
	}

	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");

		/* TODO: comprobar permisos */	

		if (entity != null && entity.equals ("viaje")) {
			if (action != null && action.equals("detalle")) {
				respuesta = this.ver_viaje_detallado (request);
			}
		} else {
			respuesta = new JSONObject();
			respuesta.put ("result", false);
			respuesta.put ("msg", "No implementado");
		}
		respuesta.put("entity", entity);
		respuesta.put("action", action);

		System.out.println (respuesta);
		writer.println (respuesta);
	}
	
	public List<Cliente> autocompletar_conductor(String clientes){
		return null;
	}
	
	public List<Localidad> autocompletar_localidad(String localidad){
		return null;
	}
	
	public List<Vehiculo> conseguir_vehiculos_cliente(Integer id_cliente){
		List<Vehiculo> lista;
		try {
			lista = this.daoViajes.getVehiculosPorCliente(id_cliente);
		} catch (ExceptionViajesCompartidos e) {
			// Si falla devuelve una lista vacia
			lista = new ArrayList<Vehiculo>();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}

	public JSONObject listar_vehiculos(HttpServletRequest request) {
		JSONObject salida = new JSONObject ();
		JSONArray json_vehiculos = new JSONArray();
		JSONArray json_conductores = new JSONArray();
		HashSet<Cliente> conductores = new HashSet<Cliente>();

		try {
			int id_usuario = AccessManager.getIdUsuario(request);
			List <Vehiculo> lista = this.conseguir_vehiculos_cliente(id_usuario);
			JSONObject json_vehiculo;
			for (Vehiculo vehiculo: lista) {
				json_vehiculo = vehiculo.toJSON();
				for (Cliente conductor: vehiculo.getConductoresAsListCliente()) {
					conductores.add(conductor);
				} 
				json_vehiculos.add (json_vehiculo);
			}
			salida.put("vehiculos", json_vehiculos);
			
			JSONObject json_conductor;
			for (Cliente conductor: conductores) {
				json_conductor = new JSONObject();
				/* No se usa Cliente.toJSON para que no se envie la contrasenia */
				json_conductor.put("id", conductor.getId_usuario());
				json_conductor.put("nombre_usuario", conductor.getNombre_usuario());
				json_conductor.put("foto", conductor.getFoto());
				json_conductores.add(json_conductor);
			}
			salida.put("clientes", json_conductores);
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", "Error interno del servidor: ");
			e.printStackTrace();
			return salida;
		}

		salida.put("result", true);
		return salida;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject nuevo_viaje(HttpServletRequest request){
		int id_origen=-1, id_destino=-1, id_conductor=-1;
		JSONArray id_intermedios=null;
		int asientos_ida=-1, asientos_vuelta=-1;
		float precio_ida=-1f, precio_vuelta=-1f;
		String nombre_amigable=null, patente_vehiculo=null;
		Timestamp fecha_ida=null, fecha_vuelta=null;
		boolean ida_vuelta = false;
		ArrayList<String> err = new ArrayList<String>();

		JSONObject salida = new JSONObject();
		JSONObject params = new JSONObject();
		id_intermedios = new JSONArray();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			id_origen = Integer.parseInt(request.getParameter("origen"));
		} catch (Exception e) {
			err.add("Origen no es valido");
		} try {
			id_destino = Integer.parseInt(request.getParameter("destino"));
		} catch (Exception e) {
			err.add("Destino no es valido");
		} try {
			String[] locs = request.getParameterValues("intermedios");
			if (locs != null) {
				for (String loc: locs) {
					id_intermedios.add(Integer.parseInt(loc));
					System.out.println("PUNTO INTERMEDIO: ID"+loc);
				}
			}
		} catch (NumberFormatException e) {
			err.add("Punto intermedio no es valido");
		} try {
			nombre_amigable = request.getParameter("nombre_amigable");
		} catch (Exception e) {
			err.add("Nombre amigable");
		} try {
			precio_ida = Float.parseFloat(request.getParameter ("precio_ida"));
		} catch (Exception e) {
			err.add("Precio de viaje de ida no es valido");
		} try {
			fecha_ida = new Timestamp (format.parse(request.getParameter("fecha_ida")).getTime());
		} catch (Exception e) {
			err.add("Fecha de viaje de ida no es valida");
		} try {
			patente_vehiculo = request.getParameter("vehiculo_ida");
		} catch (Exception e) {
			err.add("Fecha de viaje de vuelta no es valida");
		} try {
			asientos_ida = Integer.parseInt(request.getParameter("asientos_ida"));
		} catch (Exception e) {
			err.add("Cantidad de asientos en viaje de ida no es valida");
		} try {
			ida_vuelta = request.getParameter("tipo_viaje").equals("ida_vuelta");
		} catch (Exception e) {
			err.add("Tipo de viaje no es valido");
		}

		if(ida_vuelta) {
			try {
				fecha_vuelta = new Timestamp (format.parse(request.getParameter("fecha_vuelta")).getTime());
			} catch (Exception e) {
				err.add("Fecha de vuelta no es valida");
			} try {
				asientos_vuelta = Integer.parseInt(request.getParameter("asientos_vuelta"));
			} catch (Exception e) {
				err.add("Cantidad de asientos en viaje de vuelta no es valida");
			} try {
				precio_vuelta = Float.parseFloat(request.getParameter("precio_vuelta"));
			} catch (Exception e) {
				err.add("Precio de viaje de vuelta no es valido");
			}
		}
			
		if(err.size() > 0) {
			salida.put("result", false);
			salida.put("msg", err);
			return salida;
		}

		params.put("cliente", AccessManager.getIdUsuario(request));
		params.put("vehiculo", patente_vehiculo);

		JSONObject localidades = new JSONObject();
		localidades.put("origen", id_origen);
		localidades.put("intermedios", id_intermedios);
		localidades.put("destino", id_destino);
		params.put("localidades", localidades);


		JSONObject viaje = new JSONObject();
		viaje.put("fecha_inicio", fecha_ida);
		viaje.put("cantidad_asientos", asientos_ida);
		viaje.put("nombre_amigable", nombre_amigable);
		viaje.put("precio", precio_ida);
		params.put("viaje", viaje);

		if(ida_vuelta) {
			JSONObject vuelta = new JSONObject ();
			vuelta.put("fecha_inicio", fecha_vuelta);
			vuelta.put("cantidad_asientos", asientos_vuelta);
			//vuelta.put("nombre_amigable", nombre_amigable+" (VUELTA)");
			vuelta.put("precio", precio_vuelta);
			params.put("vuelta", vuelta);
		}

		
		try {
			daoViajes.nuevoViaje(params);
		} catch (ExceptionViajesCompartidos e) {
			salida.put("result", false);
			salida.put("msg", e.getMessage());
			return salida;
		}
		salida.put("result", true);
		salida.put("msg", "Se ha creado el viaje");
		return salida;
	}
	
	public JSONObject buscar_viaje(JSONObject datos){
		/*
		EL JSON QUE RECIBE LE METODO TENDRIA LA FORMA:
		{ "ORIGEN":ID_LOCALIDAD, "DESTINO":ID_LOCALIDAD, DESDE, HASTA, CONDUCTOR,ESTADO}	
		 */
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject ver_viaje_detallado(HttpServletRequest request) {
		int id_viaje;
		JSONObject salida = new JSONObject();
		Viaje viaje = null;
		Cliente usuario_logueado = null;

		try {
			usuario_logueado = (Cliente) daoUsuarios.buscarPorClaveCandidata ("Cliente", AccessManager.nombreUsuario(request));
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", "No se ha iniciado sesion como un cliente valido");
			return salida;
		}
		
		try {
			id_viaje = Integer.parseInt(request.getParameter("id_viaje"));
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", "id_viaje invalido");
			return salida;
		}
		
		viaje = daoViajes.getViajeById(id_viaje);
		
		if (viaje == null) {
			salida.put("result", false);
			salida.put("msg", "Viaje no valido");
			return salida;
		}
		
		salida.put("viaje", viaje.toJSON());
		JSONArray localidades = new JSONArray();
		for (Localidad loc: viaje.getLocalidadesComoListLocalidad()) {
			localidades.add (loc.toJSON());
		}
		
		salida.put("localidades", localidades);
		salida.put("vehiculo", viaje.getVehiculo().toJSON());
		
		Cliente conductor = viaje.getConductor();
		JSONObject json_conductor = new JSONObject();
		json_conductor.put("id", conductor.getId_usuario());
		json_conductor.put("nombre_usuario", conductor.getNombre_usuario());
		json_conductor.put("reputacion", conductor.getReputacion());
		json_conductor.put("foto", conductor.getFoto());
		json_conductor.put("foto_registro", conductor.getFoto_registro());
		salida.put("conductor", json_conductor);
		
		JSONObject json_logged = new JSONObject();
		json_logged.put("es_conductor", (usuario_logueado.getId_usuario() == viaje.getConductor().getId_usuario()));
		json_logged.put("es_pasajero", viaje.getPasajerosComoListCliente().contains(usuario_logueado));
		json_logged.put("es_seguidor", false); //IMPLEMENTAR DESPUES
		json_logged.put("ha_calificado", false); //IMPLEMENTAR DESPUES
		salida.put("usuario_logueado", json_logged);
		salida.put("result", true);
		
		return salida;
	}
		/*
		 EL JSON DE LA RESPUESTA DEBERIA TENER:
		 
		 Nota de Juan: contrastar este json con el de 'detalle_viaje.js' funcion: 'simular()'
		 {
		 	"CONDUCTOR":{NOMBREUSUARIO, FOTOPERFIL, FOTO_REGISTRO, REPUTACION},
		 	"VEHICULO":{FOTO,MARCA,MODELO, PATENTE,AÑO,AIRE_ACOND,SEGURO,VERIFICADO},
		 	"VIAJE":{FECHA,HORA,NOMBRE_AMIGABLE,IDA/VUELTA,ID_VIAJECOMPLEMENTO(es el id_ida o id_vuelta), COSTO,ESTADO,ASIENTOS_LIBRES
		 			"RECORRIDO":[ ORIGEN,INTERMDIO,....,DESTINO]
			//esto es para mostrarle funcionalidades(botones)
		 	"usuario_logueado":{es_conductor,es_pasajero,es_seguidor}
		 }
		 
		 */
	
	private JSONObject participar_viaje(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();
		JSONObject postulacion = new JSONObject();
		
		// Chequeo que usuario es cliente
		if (!this.usuarioEsClienteValido(request)){
			respuesta.put("result", false);
			respuesta.put("msg", "No se ha iniciado sesion como un cliente válido");
			return respuesta;
		}
		postulacion.put("cliente",AccessManager.getIdUsuario(request));
		
		//Chequeo que id del viaje es valido
		int idViaje;
		try {
			idViaje = Integer.parseInt(request.getParameter("id_viaje"));
		} catch (Exception e) {
			respuesta.put("result", false);
			respuesta.put("msg", "Id del viaje no es válido");
			return respuesta;
		}
		postulacion.put("viaje",idViaje);

		//Chequeo que origen y destino sean validos
		int origen,destino;
		try {
			origen = Integer.parseInt(request.getParameter("origen"));
		} catch (Exception e) {
			respuesta.put("result", false);
			respuesta.put("msg", "Localidad de subida no es válida");
			return respuesta;
		}
		try {
			destino = Integer.parseInt(request.getParameter("destino"));
		} catch (Exception e) {
			respuesta.put("result", false);
			respuesta.put("msg", "Localidad de bajada no es válida");
			return respuesta;
		}
		postulacion.put("localidad_subida", origen);
		postulacion.put("localidad_bajada", destino);

		//invoco al gran Lucas
		try {
			daoViajes.Cliente_se_postula_en_viaje(postulacion);
		} catch (ExceptionViajesCompartidos e) {
			respuesta.put("result", false);
			respuesta.put("msg", e.getMessage());
			return respuesta;
		}
		
		// Si todo salio bien le doy al cliente la data del conductor para que se contacte
		Viaje v = daoViajes.getViajeById(idViaje);
		Cliente c = v.getConductor();
		Persona p = c.getPersona();
		JSONObject conductor = new JSONObject();
		conductor.put("persona", p.toJSON());
		conductor.put("cliente", c.toJSON());
		
		// Respondo que todo salio bien y le doy el conductor
		respuesta.put("result", true);
		respuesta.put("msg", "Usted se ha postulado correctamente");
		respuesta.put("conductor", conductor);

		return respuesta;
	}
	
	private JSONObject cancelar_participacion(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();

		// Chequeo que usuario es cliente
		if (!this.usuarioEsClienteValido(request)){
			respuesta.put("result", false);
			respuesta.put("msg", "No se ha iniciado sesion como un cliente válido");
			return respuesta;
		}
		
		//Chequeo que id del viaje es valido
		int idViaje;
		try {
			idViaje = Integer.parseInt(request.getParameter("id_viaje"));
		} catch (Exception e) {
			respuesta.put("result", false);
			respuesta.put("msg", "Id del viaje no es válido");
			return respuesta;
		}
		/*
		 * 
		 * DAO ACA
		 */
		
		respuesta.put("result", false);
		respuesta.put("msg", "No implementado");
		
		return respuesta;
	}

	public JSONObject comision_por_recorrido(Integer id_localidad_subida, Integer id_localidad_bajada, Integer id_viaje){
		return null;
	}
	

	private JSONObject ver_postulantes(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();

		// Chequeo que usuario es cliente
		if (!this.usuarioEsClienteValido(request)){
			respuesta.put("result", false);
			respuesta.put("redirect", "/home.html");
			return respuesta;
		}
		
		//Chequeo que id del viaje es valido
		int idViaje;
		try {
			idViaje = Integer.parseInt(request.getParameter("id_viaje"));
		} catch (Exception e) {
			respuesta.put("result", false);
			respuesta.put("redirect", "/home.html");
			return respuesta;
		}
		
		//Chequeo que cliente es conductor
		Viaje viaje = daoViajes.getViajeById(idViaje);
		String cliente = AccessManager.nombreUsuario(request);
		String conductor = viaje.getConductor().getNombre_usuario();
		if (!cliente.equals(conductor)){
			AccessManager.nombreUsuario(request);
			respuesta.put("result", false);
			respuesta.put("redirect", "/acceso_denegado.html");
			return respuesta;
		}
		
		// Tomo postulantes
		List<PasajeroViaje> listaPasajeros = daoViajes.listarPasajerosPorViaje(idViaje);
		
		//En el arreglo que devuelvo cargo json con la data que precisa la web
		JSONArray postulantes = new JSONArray();
		for (PasajeroViaje pv : listaPasajeros){
			postulantes.add(cargarPostulante(pv));
		}
		
		respuesta.put("result", true);
		respuesta.put("msg", "Postulantes cargados");
		respuesta.put("postulantes", postulantes);
		
		return respuesta;
	}
	
	private JSONObject cargarPostulante(PasajeroViaje pv) {
		JSONObject postulante = new JSONObject();
		Cliente c = pv.getCliente();
		Persona p = c.getPersona();
		
		postulante.put("estado_postulacion", pv.getEstado().toString());
		postulante.put("origen", pv.getLocalidad_subida().getLocalidad().getNombre());
		postulante.put("destino", pv.getLocalidad_bajada().getLocalidad().getNombre());
		postulante.put("nombre_usuario", c.getNombre_usuario());
		postulante.put("foto", c.getFoto());
		postulante.put("reputacion", c.getReputacion());
		postulante.put("apellido", p.getApellidos());
		postulante.put("nombres", p.getNombres());
		postulante.put("telefono", p.getTelefono());
		postulante.put("mail", c.getEmail());
		
		return postulante;
	}

	/* PARA QUE TOMAS EL NOMBRE AMIGABLE? 
	public JSONObject ver_lista_pasajeros(Integer id_viaje){
		daoViajes.listarPasajerosPorViaje(id_viaje);
		daoViajes.nombreAmigablePorViaje(id_viaje);
		return null;
	}
	*/
	public JSONObject aceptar_rechazar_postulantes(Integer decision, Integer id_cliente_postulante, Integer id_viaje){
		try {
			if(decision==1){
				daoViajes.aceptarPasajero(id_cliente_postulante,id_viaje);
			}else{
				if(decision==2){
					daoViajes.rechazarPasajero(id_cliente_postulante,id_viaje);
				}
			}
		} catch (ExceptionViajesCompartidos e) {
			// agregado para que compile
		}
		return null;
	}

	public JSONObject ver_mis_viajes (HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		try {
			JSONArray json_viajes = new JSONArray();
			int id_usuario = AccessManager.getIdUsuario(request);
			
			if (id_usuario < 0) {
				salida.put ("result", false);
				salida.put ("msg", "Nombre de usuario no valido");
				return salida;
			}

			List<Viaje> viajes = daoViajes.listarViajesPorConductor(id_usuario);
		
			//AGREGAR LOS VIAJES COMO PASAJERO!
			//viajes.addAll( daoViajes.listarViajesPorPasajero(id_usuario));

			for (Viaje viaje: viajes) {
				JSONObject jtmp = new JSONObject();
				jtmp.put("id", viaje.getId_viaje());
				jtmp.put("origen", viaje.getOrigen().getNombre());
				jtmp.put("destino", viaje.getDestino().getNombre());
				jtmp.put("fecha_inicio", (viaje.getFecha_inicio().toString()));
				jtmp.put("conductor", viaje.getConductor().getNombre_usuario());
				jtmp.put("reputacion", viaje.getConductor().getReputacion());
				jtmp.put("precio", viaje.getPrecio());
				jtmp.put("foto", viaje.getConductor().getFoto());

				json_viajes.add(jtmp);
			}
			salida.put("viajes", json_viajes);
			salida.put("result", true);

		/* SEGUIR */
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", "Error interno del servidor ");
			e.printStackTrace();
			return salida;
		}

		return salida;
	}

	public JSONObject nuevo_vehiculo (HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		JSONObject params = new JSONObject();
		int id_usuario = AccessManager.getIdUsuario(request);
		JSONObject json_vehiculo = new JSONObject ();

		json_vehiculo.put("anio", Integer.parseInt(request.getParameter("anio")));
		json_vehiculo.put("marca", request.getParameter("marca"));
		json_vehiculo.put("modelo", request.getParameter("modelo"));
		json_vehiculo.put("patente", request.getParameter("patente"));
		json_vehiculo.put("aire", request.getParameter("aire").toString().charAt(0));
		json_vehiculo.put("color", request.getParameter("color"));
		json_vehiculo.put("asientos", Integer.parseInt(request.getParameter("asientos")));
		json_vehiculo.put("seguro", request.getParameter("seguro").toString().charAt(0));
		//json_vehiculo.put("foto", request.getParameter(""));

		params.put("conductor", id_usuario);
		params.put("vehiculo", json_vehiculo);

		try{
			daoViajes.NuevoVehiculo(params);
			salida.put("result", true);
			salida.put("msg", "Se ha creado el vehiculo correctamente");
		} catch (ExceptionViajesCompartidos e) {
			salida.put("result", false);
			salida.put("msg", e.getMessage());
		}
		return salida;
	}
	
	public JSONObject cliente_no_maneja_mas (HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		//TODO
		//metodo para que un cliente no puede manejar mas un auto, esta chequeado en "testGetVehiculosPorCliente"
		//ponganle el nombre q quieran, soy malo para los nombres de los metodos jajajaja
		//this.daoViajes.clienteNoManejaVehiculo(id_cliente, id_vehiculo);
		return salida;
	}
	
		
	// Funcion que chequea que el usuario registrado es un cliente (solo los clientes tienen acceso a ciertas funcionalidades de viaje)
	private boolean usuarioEsClienteValido(HttpServletRequest request) {
		Cliente usuario_logueado = null;
		try {
			usuario_logueado = (Cliente) daoUsuarios.buscarPorClaveCandidata ("Cliente", AccessManager.nombreUsuario(request));
			return daoUsuarios.isUsuarioActivo(usuario_logueado.getNombre_usuario());
		} catch (Exception e) {
			return false;
		}		
	}

	public JSONObject buscar_viaje (HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		List<Viaje> resultados;
		JSONObject busqueda = new JSONObject();
		Timestamp fecha_desde=null, fecha_hasta=null;
		int origen, destino;
		String estado=null, conductor=null;
		SimpleDateFormat format = new SimpleDateFormat ("yyy-MM-dd");
		try {
			try {
				origen = Integer.parseInt(request.getParameter("origen"));
			} catch (Exception e) {
				throw new ExceptionViajesCompartidos ("Origen no valido");
			} try {
				destino = Integer.parseInt(request.getParameter("destino"));
			} catch (Exception e) {
				throw new ExceptionViajesCompartidos ("Destino no valido");
			} try {
				fecha_desde = new Timestamp ( (format.parse(request.getParameter("fecha_desde"))).getTime() );
			} catch (Exception e) {
				throw new ExceptionViajesCompartidos ("Fecha no valida");
			} try {
				fecha_hasta = new Timestamp ( (format.parse(request.getParameter("fecha_hasta"))).getTime() );
			} catch (Exception e) {
				// No hacer nada, el campo fecha_hasta no es obligatorio
			} try {
				estado = request.getParameter("estado");
			} catch (Exception e) {
				estado = "ambas";
			} try {
				conductor = request.getParameter("conductor");
			} catch (Exception e) {
				// No hacer nada, el campo conductor no es obligatorio
			}
			busqueda.put("origen", origen);
			busqueda.put("destino", destino);
			busqueda.put("fecha_desde", fecha_desde);
			if (fecha_hasta != null) {
				busqueda.put("fecha_hasta", fecha_hasta);
			}
			if (estado != null) {
				busqueda.put("estado", estado);
			}
			if (conductor != null) {
				busqueda.put("conductor", conductor);
			}
			
			resultados = daoViajes.buscarViajes(busqueda);
			
			JSONArray json_viajes = new JSONArray();
			for (Viaje viaje: resultados) {
				JSONObject json_viaje = new JSONObject();
				json_viaje.put("id", viaje.getId_viaje());
				json_viaje.put("origen", viaje.getOrigen().getNombre());
				json_viaje.put("destino", viaje.getDestino().getNombre());
				json_viaje.put("fecha_inicio", (viaje.getFecha_inicio() != null)? viaje.getFecha_inicio().toString(): null);
				json_viaje.put("precio", viaje.getPrecio());
				json_viaje.put("estado", viaje.getEstado().toString());
				json_viaje.put("conductor", viaje.getConductor().getNombre_usuario());
				json_viaje.put("reputacion", viaje.getConductor().getReputacion());
				json_viaje.put("foto", viaje.getConductor().getFoto());
				json_viajes.add (json_viaje);
			}
			salida.put("viajes", json_viajes);
		} catch (ExceptionViajesCompartidos e) {
			salida.put("result", false);
			salida.put("msg", e.getMessage());
			return salida;
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", "Error interno del servidor");
			e.printStackTrace();
			return salida;
		}
		
		salida.put("result", true);
		return salida;
	}

}
