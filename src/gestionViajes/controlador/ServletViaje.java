package gestionViajes.controlador;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
			} else if (action != null && action.equals("detalle")) {
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
		return null;
	}
	
	public JSONObject nuevo_viaje(HttpServletRequest request){
		int id_origen=-1, id_destino=-1, id_conductor=-1;
		JSONArray id_intermedios=null;
		int asientos_ida=-1, asientos_vuelta=-1;
		float costo=-1f;
		String nombre_amigable=null, patente_vehiculo=null;
		Date fecha_ida=null, fecha_vuelta=null;
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
				}
			}
		} catch (NumberFormatException e) {
			err.add("Punto intermedio no es valido");
		} try {
			nombre_amigable = request.getParameter("nombre_amigable");
		} catch (Exception e) {
			err.add("Nombre amigable");
		} try {
			costo = Float.parseFloat(request.getParameter ("precio"));
		} catch (Exception e) {
			err.add("Precio no es valido");
		} try {
			fecha_ida = new Date (format.parse(request.getParameter("fecha_ida")).getTime());
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
				fecha_vuelta = new Date (format.parse(request.getParameter("fecha_vuelta")).getTime());
			} catch (Exception e) {
				err.add("Fecha de vuelta no es valida");
			} try {
				asientos_vuelta = Integer.parseInt(request.getParameter("asientos_vuelta"));
			} catch (Exception e) {
				err.add("Cantidad de asientos en viaje de vuelta no es valida");
			}
		}
			
		if(err.size() > 0) {
			salida.put("result", false);
			salida.put("msg", err);
			return salida;
		}

		params.put("cliente", AccessManager.getIdUsuario(request));
		params.put("vehiculo", patente_vehiculo);
		params.put("cantidad_asientos", asientos_ida); // Esto deberia estar en viaje y en vuelta
		params.put("fecha_inicio", fecha_ida); // Esto deberia estar en viaje y en vuelta

		JSONObject localidades = new JSONObject();
		localidades.put("origen", id_origen);
		localidades.put("intermedios", id_intermedios);
		localidades.put("destino", id_destino);
		params.put("localidades", localidades);


		JSONObject viaje = new JSONObject();
		viaje.put("fecha_inicio", fecha_ida);
		//viaje.put("cantidad_asientos", asientos_ida);
		viaje.put("nombre_amigable", nombre_amigable);
		params.put("viaje", viaje);

		if(ida_vuelta) {
			JSONObject vuelta = new JSONObject ();
			vuelta.put("fecha_inicio", fecha_vuelta);
			//vuelta.put("cantidad_asientos", asientos_vuelta);
			vuelta.put("nombre_amigable", nombre_amigable);
			params.put("vuelta", vuelta);
		}

		
		try {
			daoViajes.nuevoViaje(params);
		} catch (ExceptionViajesCompartidos e) {
			//ENVIA MENSAJE DE ERROR CON e.getMessage();
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
		{ "ORIGEN":ID_LOCALIDAD, "DESTINO":ID_LOCALIDAD, DESDE, HASTA, CONDUCTOR,ASIENTOS_LIBRES,ESTADO}	
		 */
		return null;
	}
	
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
		json_logged.put("es_pasajero", viaje.getPasajerosComoListCliente().contains(usuario_logueado)); //NO SE SI ESTO FUNCIONA
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
		 	"VEHICULO":{FOTO,MARCA,MODELO, PATENTE,A�O,AIRE_ACOND,SEGURO,VERIFICADO},
		 	"VIAJE":{FECHA,HORA,NOMBRE_AMIGABLE,IDA/VUELTA,ID_VIAJECOMPLEMENTO(es el id_ida o id_vuelta), COSTO,ESTADO,ASIENTOS_LIBRES
		 			"RECORRIDO":[ ORIGEN,INTERMDIO,....,DESTINO]
			//esto es para mostrarle funcionalidades(botones)
		 	"usuario_logueado":{es_conductor,es_pasajero,es_seguidor}
		 }
		 
		 */
	
	public JSONObject cliente_se_postula_a_viaje(Integer id_viaje, Integer id_localidad_subida, Integer id_localidad_bajada){
		//saco el id del cliente mediante la cookie
		return null;
	}
	
	public JSONObject comision_por_recorrido(Integer id_localidad_subida, Integer id_localidad_bajada, Integer id_viaje){
		return null;
	}
	
	public JSONObject ver_lista_pasajeros(Integer id_viaje){
		daoViajes.listarPasajerosPorViaje(id_viaje);
		daoViajes.nombreAmigablePorViaje(id_viaje);
		return null;
	}
	
	public JSONObject aceptar_rechazar_postulantes(Integer decision, Integer id_cliente_postulante, Integer id_viaje){
		if(decision==1){
			daoViajes.aceptarPasajero(id_cliente_postulante,id_viaje);
		}else{
			if(decision==2){
				daoViajes.rechazarPasajero(id_cliente_postulante,id_viaje);
			}
		}
		return null;
	}

	public JSONObject ver_viajes_de_usuario (HttpServletRequest request) {
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
				jtmp.put("fecha_inicio", viaje.getFecha_inicio());
				jtmp.put("conductor", viaje.getConductor().getNombre_usuario());
				jtmp.put("reputacion", viaje.getConductor().getReputacion());
//				jtmp.put("precio", viaje.getPrecio());
				jtmp.put("foto", viaje.getConductor().getFoto());

				json_viajes.add(jtmp);
			}
			salida.put("viajes", json_viajes);
			salida.put("result", true);

		/* SEGUIR */
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", "Error interno del servidor");
			return salida;
		}

		return salida;
	}
}
