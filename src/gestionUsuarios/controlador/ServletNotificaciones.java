package gestionUsuarios.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionUsuarios.modelo.Cliente;
import gestionUsuarios.modelo.Notificacion;
import gestionViajes.modelo.Vehiculo;
import otros.ExceptionViajesCompartidos;

class ServletNotificaciones extends HttpServlet {
	
	protected DAONotificaciones daoNotificaciones;

	@Override
	public void init() {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");
		if (entity != null && entity.equals ("notificaciones")) {
			if (action != null && action.equals("ver_no_leidas")) {
				respuesta = this.verNoLeidas(request);
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");
		
		if (entity != null && entity.equals("notificaciones")) {
			if (action != null && action.equals ("marcar_leida")) {
				respuesta = this.marcarComoLeida(request);
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

	private JSONObject verNoLeidas(HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		JSONArray json_notificaciones = new JSONArray();
		JSONObject json_notificacion;
		Integer idCliente = Integer.parseInt(request.getParameter("idCliente"));
		try {
			ArrayList<Notificacion> notificaciones = (ArrayList<Notificacion>) daoNotificaciones.getNotificacionesNoLeidas(idCliente);
			for (Notificacion notificacion: notificaciones) {
				json_notificacion = notificacion.toJSON();
				json_notificaciones.add (json_notificacion);
			}
			salida.put("notificaciones", json_notificaciones);
		} catch (ExceptionViajesCompartidos e) {
			salida.put("result", false);
			salida.put("msg", "Error interno del servidor: ");
			e.printStackTrace();
			return salida;
		}
		salida.put("result", true);
		return salida;
	}

	private JSONObject marcarComoLeida(HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		Integer idNotificacion = Integer.parseInt(request.getParameter("idNotificacion"));
		daoNotificaciones.marcarLeida(idNotificacion);
		salida.put("result", true);
		return salida;
	}
}