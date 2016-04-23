package controladores;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import modelo.BaseDatos;
import modelo.Persona;
import modelo.Rol;
import modelo.Usuario;


public class ControladorRegistro extends HttpServlet {


	public void init() throws ServletException
	{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doGet de ControladorLogin");

		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("En doPost de ControladorLogin!");
		JSONObject out = null;

		// Setear driver
		PrintWriter writer = response.getWriter();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			writer.println("No se pudo abrir el driver de la base de datos");
			writer.println(e.getMessage());
		}
		
		String accion = request.getParameter("action");

		if (accion.equals("validar_usuario")){
			//recibo el nombre del usuario y pregunto si existe o no.
			out = validarUsuario(request,response);
		}else if(accion.equals("new")){
			out = registrarCliente(request);
		}
		
		if (out == null) {
			out = new JSONObject();
			out.put ("result", false);
		}
		
		out.put("action", accion);
		System.out.println("Lo que mando al js: "+out);
		writer.println (out);

	}

	private JSONObject validarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String user = request.getParameter("usuario");
		JSONObject salida = new JSONObject ();		
		if ("hola".equals("chau")) {
			System.out.println("Usuario existe");
			salida.put ("result", true); 
		}else{
			System.out.println("Usuario no existe");
			salida.put("result", false);
		}
		return salida;
	}

	private JSONObject registrarCliente(HttpServletRequest request) {
		JSONObject out = new JSONObject();
		System.out.println("En registrar Cliente");
		System.out.println(request.getParameter("persona"));
		out = guardarPersona(request);
		if ((boolean) out.get("result")){
			System.out.println("Guarde persona bien, voy hacia guardar Usuario");
			out = guardarUsuario(request, out.get("id"));
		}
		return out;
	}
	
	private JSONObject guardarPersona (HttpServletRequest request) {
		JSONObject recibida, salida;
		Persona persona;
		System.out.println("En guardar Persona");
		recibida = new JSONObject();
		salida = new JSONObject();

		recibida.put ("id_persona", -1);
		recibida.put("nombres", request.getParameter("nombres"));
		recibida.put("apellidos", request.getParameter("apellidos"));
		recibida.put("tipo_doc", Integer.parseInt(request.getParameter("tipo_doc")));
		recibida.put("nro_doc", Integer.parseInt(request.getParameter("nro_doc")));
		recibida.put("fecha_nacimiento", request.getParameter("fecha_nacimiento"));
		recibida.put("sexo", request.getParameter("sexo"));
		recibida.put("domicilio", request.getParameter("domicilio"));
		recibida.put("telefono", request.getParameter("telefono"));
		System.out.println("Tome parametros");

		persona = new Persona (recibida);
		if (persona.guardar()) {
			System.out.println("Guarde persona");

			salida.put ("result", true);
			salida.put ("msg", "Se han guardado los datos de la persona");
			salida.put("id", persona.getId());
			System.out.println("guarde id");

		} else {
			salida.put ("result", false);
			salida.put ("msg", "No se ha podido guardar los datos de la persona");
		}
		return salida;
	}

	private JSONObject guardarUsuario (HttpServletRequest request, Object idPersona) {
		JSONObject recibido, salida;
		Usuario usuario;
		System.out.println("En guardar Usuario");
		recibido = new JSONObject();
		salida = new JSONObject();
		
		recibido.put("id_usuario", -1);
		recibido.put("id_persona", idPersona);
		recibido.put("nombre_usuario", request.getParameter("nombre_usuario"));
		recibido.put("password", request.getParameter("password"));
		recibido.put("email", request.getParameter("email"));
		recibido.put("descripcion", request.getParameter("descripcion"));
		recibido.put("estado", (request.getParameter("estado")));

		usuario = new Usuario (recibido);
		if (usuario.guardar()) {
			System.out.println("Usuario guardado, hacia asignar Rol");
			if (asignarRolUsuario(usuario)){
				salida.put ("result", true);
				salida.put ("msg", "El usuario ha sido registrado correctamente");
			}else{
				salida.put ("result", false);
				salida.put ("msg", "No se ha podido asignar su rol correspondiente");
			}
		} else {
			salida.put ("result", false);
			salida.put ("msg", "El usuario no ha sido registrado correctamente");
		}
		return salida;
	}
	
	private boolean asignarRolUsuario(Usuario usuario){
		// asigno rol cliente
		//en mi bd el rol cliente es el 6. Por pruebas se lo pongo a la fuerza.
		return usuario.AsignarRol(6);
	}
	
	public void destroy()
	{
	}
		
}
