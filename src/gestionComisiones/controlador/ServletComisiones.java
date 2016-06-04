package gestionComisiones.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionUsuarios.controlador.DAOAdministracionUsuarios;
import gestionUsuarios.controlador.DAONotificaciones;
import gestionComisiones.controlador.DAOComisiones;

import gestionUsuarios.modelo.Cliente;
import gestionUsuarios.modelo.Notificacion;
import gestionViajes.modelo.Vehiculo;
import otros.AccessManager;
import otros.ExceptionViajesCompartidos;

public class ServletComisiones extends HttpServlet {
	
	protected DAOComisiones daoComisiones;
	protected DAOAdministracionUsuarios daoUsuarios;
	protected DAONotificaciones daoNotificaciones;

	@Override
	public void init() {
		daoComisiones=new DAOComisiones();
		daoUsuarios=new DAOAdministracionUsuarios();
		daoNotificaciones=new DAONotificaciones();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");
		
		if (entity != null && entity.equals("saldo")) {
			if (action != null && action.equals ("consultar")) {
				respuesta = this.consultarSaldo(request);
			}
		} else {
			respuesta = new JSONObject();
			respuesta.put ("result", false);
			respuesta.put ("msg", "No implementado");
		}
		respuesta.put("entity", entity);
		respuesta.put("action", action);

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
		
		if (entity != null && entity.equals("saldo")) {
			if (action != null && action.equals ("cargar")) {
				respuesta = this.cargarSaldo(request);
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


	private JSONObject consultarSaldo(HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		String username;
		float saldo=0f;
		try {
			username = AccessManager.nombreUsuario(request);
			if (username == null) {
				throw new ExceptionViajesCompartidos("No ha iniciado sesion como usuario valido");
			}
			Cliente cliente = daoUsuarios.clientePorNombre(username);
			if (cliente == null) {
				throw new ExceptionViajesCompartidos("El usuario con el que ha iniciado sesion no es un cliente");
			}
			saldo = cliente.getSaldo();
		} catch (Exception e) {
			salida.put("result", false);
			salida.put("msg", e.getMessage());
			salida.put("redirect", "/login.html");
			return salida;
		}

		salida.put ("result", true);
		salida.put("saldo", saldo);
		return salida;
	}


	private JSONObject cargarSaldo(HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		Integer idNotificacion = Integer.parseInt(request.getParameter("idNotificacion"));
		daoNotificaciones.marcarLeida(idNotificacion);
		salida.put("result", true);
		return salida;
	}
}