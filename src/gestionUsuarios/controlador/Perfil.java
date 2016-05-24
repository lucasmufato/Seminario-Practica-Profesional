package gestionUsuarios.controlador;

import java.io.*;
import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		usuario.put("pass", u.getPassword());
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

		
		if (accion.equals("modificar_imagen")){
			out = modificarImagen(request);
		}else if(accion.equals("mail_existe")){
			out = validarMail(request);
		}else if (accion.equals("modificar_cliente")){
			out = modificarCliente(request);
		}else if (accion.equals("desactivar_cuenta")){
			out = desactivarCuenta(request,response);
		}
		
		if (out == null) {
			out = new JSONObject();
			out.put ("result", false);
		}
		
		out.put("action", accion);
		System.out.println("Lo que mando al js: "+out);
		writer.println (out);
	}

	private JSONObject desactivarCuenta(HttpServletRequest request, HttpServletResponse response) {
		JSONObject respuesta = new JSONObject();
		String nombreUsuario = request.getParameter("nombre_usuario");
		if (nombreUsuario == null || nombreUsuario.equals("")){
			respuesta.put("result", false);
			respuesta.put("msg", "El nombre de usuario se encuentra vacio o es nulo");
			return respuesta;
		}
		Usuario u = dao.buscarUsuarioPorNombre(nombreUsuario);
		if (u == null){
			respuesta.put("result", false);
			respuesta.put("msg", "El usuario ha desactivar no se encuentra registrado en el sistema");
			return respuesta;
		}
		if (!dao.isUsuarioActivo(nombreUsuario)){
			respuesta.put("result", false);
			respuesta.put("msg", "El usuario ya se encuentra desactivado");
			return respuesta;
		}
		Character inactivo = "B".charAt(0);
		u.setEstado(inactivo);
		try {
			dao.persistir(u);
		} catch (ExceptionViajesCompartidos e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			respuesta.put("result", false);
			respuesta.put("msg", "Su cuenta no ha podido ser desactivada. Vuelva a intentarlo más tarde.");
			return respuesta;
		}
		if (!AccessManager.EliminarCookie(request, response)){
			respuesta.put("result", false);
			respuesta.put("msg", "Usted no se encuentra logueado.");
			return respuesta;
		}
		
		// Esta linea podria solucionar el bug pero ocasiona un comportamiento aun mas particular
	    //request.getSession().invalidate();
		
		respuesta.put("redirect", "index.html");
		respuesta.put("result", true);
		respuesta.put("msg", "Su cuenta ha sido desactivado correctamente");
		return respuesta;
	}

	private JSONObject modificarCliente(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();
		if (setPersona(request)){
			if (setUsuario(request)){
				if (setCliente(request)){
					respuesta.put("result",true);
					respuesta.put("msg","El perfil ha sido modificado correctamente");
				}else{
					respuesta.put("result",false);
					respuesta.put("msg","Error al cargar datos de cliente");
				}
			}else{
				respuesta.put("result",false);
				respuesta.put("msg","Error al cargar datos de usuario");
			}
		}else{
			respuesta.put("result",false);
			respuesta.put("msg","Error al cargar datos de persona");
		}
		return respuesta;
	}

	private boolean setCliente(HttpServletRequest request) {
		// AUN NO SE MODIFICAN DATOS DE CLIENTE
		return true;
	}

	private boolean setUsuario(HttpServletRequest request) {
		String mail = request.getParameter("usuario[mail]");
		String nombreUsuario = request.getParameter("nombre_usuario");
		String pass = request.getParameter("usuario[pass]");
		Usuario u = dao.buscarUsuarioPorNombre(nombreUsuario);
		if (mail != null && nombreUsuario!=null){
			u.setEmail(mail);
			u.setPassword(pass);;
			try {
				dao.persistir(u);
				return true;
			} catch (ExceptionViajesCompartidos e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private boolean setPersona(HttpServletRequest request) {
		String nombreUsuario = request.getParameter("nombre_usuario");
		String apellidos = request.getParameter("persona[apellidos]");
		String nombres = request.getParameter("persona[nombres]");
		String domicilio = request.getParameter("persona[domicilio]");
		String telefono = request.getParameter("persona[telefono]");
		String fecha_nacimiento = request.getParameter("persona[fecha_nacimiento]");
		Character sexo = request.getParameter("persona[sexo]").charAt(0);
		if (nombreUsuario!=null){
			Persona p = dao.buscarUsuarioPorNombre(nombreUsuario).getPersona();
			if (p != null){
				if (domicilio!=null) p.setDomicilio(domicilio);
				if (telefono!=null) p.setTelefono(telefono);
				if (apellidos!=null) p.setApellidos(apellidos);
				if (nombres!=null) p.setNombres(nombres);
				if (fecha_nacimiento!=null) p.setFecha_nacimiento(toDate(fecha_nacimiento));
				if (sexo!=null) p.setSexo(sexo);
				try {
					dao.persistir(p);
					return true;
				} catch (ExceptionViajesCompartidos e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	private Date toDate(String fecha_nacimiento) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed=null;
		try {
			parsed = format.parse(fecha_nacimiento);
		} catch (ParseException e) {
			//this.fecha_nacimiento=null;
			e.printStackTrace();
		}
        return new java.sql.Date(parsed.getTime());
	}

	private JSONObject validarMail(HttpServletRequest request) throws IOException {
		JSONObject salida = new JSONObject ();	
		String mail = request.getParameter("mail");
		String mailActual = dao.buscarUsuarioPorNombre(AccessManager.nombreUsuario(request)).getEmail();
		if (mail.equals(mailActual) || !dao.mailExiste(mail)){
			salida.put ("es_valido", true);
			salida.put("result", true);
		}else{
			salida.put ("es_valido", false); 
			salida.put("result", true);
		}
		return salida;
	}
	
	private JSONObject modificarImagen(HttpServletRequest request) {
		JSONObject respuesta = new JSONObject();
		// nombre de usuario a quiene se le modifica la imagen
		String nombreUsuario = request.getParameter("nombre_usuario");
		
		// tomo cliente para guardarme la foto vieja y eliminarla
		Cliente c = dao.clientePorNombre(nombreUsuario);
		
		// foto de perfil nueva
		String foto = request.getParameter("foto");
		
		// foto de registro nueva
		String foto_registro = request.getParameter("foto_registro");

		//objeto que mando al dao para subir la imagen
		JSONObject objetoImagen = new JSONObject();
		boolean bandera=false;
		
		// chequeo qué imagen se desea modificar
		if (foto != null){
			//antes de subir archivo guardo url de imagen anterior para eliminar si operacion es exitosa
			String anteriorImagen = c.getFoto();
			
			objetoImagen.put("imagen", this.subirArchivo(foto));
			objetoImagen.put("usuario", nombreUsuario);
			
			//subo imagen
			bandera = dao.subirFotoCliente(objetoImagen);
			
			//elimino imagen anterior
			if (bandera){
				if (anteriorImagen != null){
					this.eliminarArchivo(anteriorImagen);
				}
			}
		}
		if (foto_registro != null){
			//antes de subir archivo guardo url de imagen anterior para eliminar si operacion es exitosa
			String anteriorImagen = c.getFoto_registro();
			
			objetoImagen.put("imagen", this.subirArchivo(foto_registro));
			objetoImagen.put("usuario", nombreUsuario);
			
			//subo imagen
			bandera = dao.subirFotoRegistro(objetoImagen);
			
			//elimino imagen anterior
			if (bandera){
				if (anteriorImagen != null){
					this.eliminarArchivo(anteriorImagen);
				}
			}
		}
		
		if (bandera){
			respuesta.put ("result", true);
			respuesta.put ("msg", "Imagen guardada");
		}else{
			respuesta.put ("result", false);
			respuesta.put ("msg", "Error al guardar la imagen");
		}
		
		return respuesta;
	}
	
	private String subirArchivo(String archivo) {
		if (!archivo.isEmpty() && archivo != null){
			archivo = FileManager.uploadImage(getServletContext().getRealPath("/"), archivo);
		}
		return archivo;
	}
	private boolean eliminarArchivo(String archivo) {
		boolean bandera = false;
		if (archivo != null && !archivo.isEmpty()){
			FileManager.modifyImage(getServletContext().getRealPath("/"), archivo);
		}
		return bandera;
	}

	public void destroy()
	{
	}
	
}
