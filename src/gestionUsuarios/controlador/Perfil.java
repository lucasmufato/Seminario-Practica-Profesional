package gestionUsuarios.controlador;

import java.io.*;
import java.math.BigInteger;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import gestionUsuarios.modelo.*;
import otros.*;

public class Perfil extends HttpServlet {

	DAOAdministracionUsuarios dao;

	public void init() throws ServletException
	{
		dao= new DAOAdministracionUsuarios();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doGet de Perfil");

		PrintWriter writer = response.getWriter();

		response.setContentType("application/json");

		if (AccessManager.EstaLogueado(request)){
			this.printAccept(writer, request);
		}else{
			this.printDeniedRedirect(writer);
		}
	}
	
	private JSONObject cargarPerfil(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();
		
		String usuario_perfil = request.getParameter("usuario_perfil");
		String usuario_logueado_nombre = AccessManager.nombreUsuario(request); 
		
		// Si no se establecio que perfil se visita, se visita el perfil propio del usuario logueado
		usuario_perfil = (usuario_perfil != null)? usuario_perfil : usuario_logueado_nombre;
		
		//Cargo respuesta con data del usuario logueado
		JSONObject usuario_logueado = new JSONObject(); 
		usuario_logueado.put("es_perfil_propio", usuario_perfil == usuario_logueado_nombre);
		respuesta.put("usuario_logueado", usuario_logueado);
		
		// Data del perfil del cliente visitado
		Cliente c = dao.clientePorNombre(usuario_perfil);
		JSONObject cliente = new JSONObject();
		cliente.put("mail", c.getEmail());
		cliente.put("nombre_usuario", c.getNombre_usuario());
		cliente.put("foto", c.getFoto());
		cliente.put("foto_registro", c.getFoto_registro());
		cliente.put("reputacion", c.getReputacion());
		
		// Data de la persona
		Persona p = c.getPersona();
		cliente.put("nombres", p.getNombres());
		cliente.put("apellidos", p.getApellidos());
		cliente.put("tipo_doc", p.getTipo_doc());
		cliente.put("nro_doc", p.getNro_doc());
		cliente.put("telefono", p.getTelefono());
		cliente.put("domicilio", p.getDomicilio());
		cliente.put("sexo", p.getSexo().toString());
		cliente.put("fecha_nacimiento", p.getFecha_nacimiento().toString());

		respuesta.put("cliente", cliente);
		return respuesta;
	}
	
	private void printAccept(PrintWriter writer, HttpServletRequest request) {
		JSONObject resultado;

		resultado = cargarPerfil(request);
		resultado.put("result", true);

		writer.println(resultado);
	}


	private void printDeniedRedirect (PrintWriter writer) {
		JSONObject resultado;

		resultado = new JSONObject();
		resultado.put("result", false);
		resultado.put("redirect", "acceso_denegado.html");

		writer.println(resultado);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("En doPost de Perfil!");
		JSONObject out = null;

		// Setear driver
		PrintWriter writer = response.getWriter();
		String accion = request.getParameter("action");

		/*
		if (accion.equals("cargar_perfil")){
			out = cargarPerfil(request,response);
		}
		*/
		
		if (out == null) {
			out = new JSONObject();
			out.put ("result", false);
		}
		
		out.put("action", accion);
		System.out.println("Lo que mando al js: "+out);
		writer.println (out);
	}

	public void destroy()
	{
	}
	
}
