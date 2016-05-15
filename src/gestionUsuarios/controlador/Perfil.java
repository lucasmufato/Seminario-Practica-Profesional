package gestionUsuarios.controlador;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

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
		if (!AccessManager.EstaLogueado(request)){
			this.printDeniedRedirect(writer,"acceso_denegado.html");
		} else if (!perfilAceptado(request)){
			this.printDeniedRedirect(writer,"perfil.html");
		}else{
			System.out.println("Entre!");
			this.printAccept(writer, request);
		}
	}
	
	/*
	 * Funcion para aceptar o no a un usuario visitando perfil de otro.
	 * Si es admin, puede visitar cualquier perfil, se lo acepta siempre.
	 * Si es Cliente, solo puede ver perfil de otros clientes.
	 * Si es Sponsor no puede ver perfiles de nadie salvo de si mismo.
	 */
	private boolean perfilAceptado(HttpServletRequest request) {
		String perfilVisitado = request.getParameter("usuario_perfil");
		if (perfilVisitado == null){ // cuando ingresa a perfil.html se lo acepta siempre
			return true;
		}else if (dao.buscarUsuarioPorNombre(perfilVisitado) == null){ // si no existe usuario, arafue
			return false;
		}else if (AccessManager.hasRol(request, "super_usuario")) { // el super usuario puede visitar cualquier perfil
			return true;
		} else if (AccessManager.hasRol(request,"sponsor")){ // sponsor solo es aceptado si visita su propio perfil
			return AccessManager.nombreUsuario(request).equals(perfilVisitado);
		}else if (AccessManager.hasRol(request, "cliente")){ //se lo acepta solo si es cliente y visita a otro cliente
			return dao.usuarioHasRol(perfilVisitado, "cliente");
		}			
		return false;
	}
	
	private void printAccept(PrintWriter writer, HttpServletRequest request) {
		JSONObject resultado = new JSONObject();
		
		//Tomo el nombre de usuario del perfil que se esta visitando
		String usuarioVisitado = getNombreUsuarioVisitado(request);
		
		//Cargo data de la persona del perfil visitado
		resultado.put("persona",this.getPersona(usuarioVisitado));
		
		//Cargo data del usuario del perfil visitado
		resultado.put("usuario",this.getUsuario(usuarioVisitado));
		
		//Cargo data del usuario que visita perfil
		resultado.put("usuario_logueado", getUsuarioLogueado(request));
		
		// Cargo data segun el rol del perfil visitado
		if (dao.usuarioHasRol(usuarioVisitado, "super_usuario")){
			// NADA, SOLO TIENE DATOS DE PERSONA Y CLIENTE POR AHORa
			// LE pongo un json para que no me de null en el js
			
			resultado.put("super_usuario",new JSONObject());
		}
		
		// Asumo que no se es cliente y sponsor a la vez.
		if (dao.usuarioHasRol(usuarioVisitado, "cliente")){
			resultado.put("cliente",this.getCliente(usuarioVisitado));
		}else if (dao.usuarioHasRol(usuarioVisitado, "sponsor")){
			resultado.put("sponsor",this.getSponsor(usuarioVisitado));
		}
		


		resultado.put("result", true);

		writer.println(resultado);
	}

	private String getNombreUsuarioVisitado(HttpServletRequest request) {
		String u = request.getParameter("usuario_perfil");
		// Si no se establecio que perfil se visita, se visita el perfil propio del usuario logueado
		return (u == null)? AccessManager.nombreUsuario(request) : u;
	}

	private JSONObject getSponsor(String perfilUsuario) {
		JSONObject sponsor = new JSONObject();
		//SIN IMPLEMENTAR
		return sponsor;
	}

	private void printDeniedRedirect (PrintWriter writer, String url) {
		JSONObject resultado;

		resultado = new JSONObject();
		resultado.put("result", false);
		resultado.put("redirect", url);

		writer.println(resultado);
	}
	
	private JSONObject getUsuarioLogueado(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();
		
		String usuario_perfil = this.getNombreUsuarioVisitado(request);
		String usuario_logueado_nombre = AccessManager.nombreUsuario(request); 
		
		respuesta.put ("es_perfil_propio",usuario_perfil.equals(usuario_logueado_nombre));
		
		return respuesta;
	}

	private JSONObject getCliente(String perfilUsuario) {
		Cliente c = dao.clientePorNombre(perfilUsuario);
		JSONObject cliente = new JSONObject();
		cliente.put("foto", c.getFoto());
		cliente.put("foto_registro", c.getFoto_registro());
		cliente.put("reputacion", c.getReputacion());
		return cliente;
	}

	private JSONObject getUsuario(String perfilUsuario) {
		Usuario u = dao.buscarUsuarioPorNombre(perfilUsuario);
		JSONObject usuario = new JSONObject();
		usuario.put("mail", u.getEmail());
		usuario.put("nombre_usuario", u.getNombre_usuario());
		return usuario;
	}

	private JSONObject getPersona(String perfilUsuario) {
		Persona p = dao.buscarUsuarioPorNombre(perfilUsuario).getPersona();

		JSONObject persona = new JSONObject();
		persona.put("nombres", p.getNombres());
		persona.put("apellidos", p.getApellidos());
		persona.put("tipo_doc", p.getTipo_doc());
		persona.put("nro_doc", p.getNro_doc());
		persona.put("telefono", p.getTelefono());
		persona.put("domicilio", p.getDomicilio());
		persona.put("sexo", p.getSexo().toString());
		persona.put("fecha_nacimiento", p.getFecha_nacimiento().toString());

		return persona;
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
