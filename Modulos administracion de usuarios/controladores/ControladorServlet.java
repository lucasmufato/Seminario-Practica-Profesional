package controladores;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import modelo.Persona;
import modelo.Usuario;
import modelo.Rol;

public class ControladorServlet extends HttpServlet {


	public void init() throws ServletException
	{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject[] resultado;
		PrintWriter writer = response.getWriter();

		/* Asegurate de iniciar el driver de base de datos */
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			writer.println("No se pudo abrir el driver de la base de datos");
			writer.println(e.getMessage());
		}

		response.setContentType("application/json");
		this.printDbData(writer);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action, entity;
		JSONObject out = null;

		PrintWriter writer = response.getWriter();

		action = request.getParameter("action");
		entity = request.getParameter("entity");

		if (action.equals("new") ||action.equals("edit")){
			if (entity.equals("persona")) {
				out = updatePersona(request);
			} else if (entity.equals("usuario")) {
				out = updateUsuario(request);
			} else if (entity.equals("rol")) {
				out = updateRol(request);
			}
		} else if (action.equals("delete")) {
			if (entity.equals("persona")) {
				out = deletePersona(request);
			} else if (entity.equals("usuario")) {
				out = deleteUsuario(request);
			} else if (entity.equals("rol")) {
				out = deleteRol(request);
			}
		} else if (action.equals("assignRoles") && entity.equals("usuario")) {
			/* No implementado aun */
		}

		if (out == null) {
			out = new JSONObject();
			out.put ("result", false);
		}

		out.put("action", action);
		out.put("entity", entity);
		writer.println (out);
	}

	public void destroy()
	{
	}

	private void printDbData(PrintWriter writer) {
		JSONObject resultado;

		resultado = new JSONObject();
		resultado.put("personas", this.getPersonas());
		resultado.put("usuarios", this.getUsuarios());
		resultado.put("roles", this.getRoles());

		writer.println(resultado);
	}

	private JSONArray getPersonas() {
		JSONArray personas;
		JSONObject[] in;
		Object id, fechaNac;

		in = Persona.Select();
		personas = new JSONArray ();
		if (in != null) {
			for (int i = 0; i< in.length; i++) {
				id = in[i].get("id_persona");
				fechaNac = in[i].get("fecha_nacimiento");
				in[i].put("id", id);
				in[i].remove("fecha_nacimiento");
				in[i].put("fecha_nacimiento", fechaNac.toString());
				personas.add(in[i]);
			}
		}
		return personas;
	}

	private JSONArray getUsuarios() {
		JSONArray usuarios;
		JSONObject[] in;
		Object id;

		in=Usuario.Select();
		usuarios = new JSONArray ();
		if(in != null) {
			for (int i = 0; i< in.length; i++) {
				id = in[i].get("id_usuario");
				in[i].put("id", id);
				usuarios.add(in[i]);
			}
		}
		return usuarios;
	}

	private JSONArray getRoles() {
		JSONArray roles;
		JSONObject[] in;
		Object id;

		in=Rol.Select();
		roles = new JSONArray ();
		if(in != null) {
			for (int i = 0; i< in.length; i++) {
				id = in[i].get("id_rol");
				in[i].put("id", id);
				roles.add(in[i]);
			}
		}
		return roles;
	}

	private JSONObject updatePersona (HttpServletRequest request) {
		JSONObject recibida, salida;
		Persona persona;

		recibida = new JSONObject();
		salida = new JSONObject();

		if (request.getParameter("action").equals("new")) {
			recibida.put ("id_persona", -1);
		} else {
			recibida.put("id_persona", Integer.parseInt(request.getParameter("id_persona")));
		}
		recibida.put("nombres", request.getParameter("nombres"));
		recibida.put("apellidos", request.getParameter("apellidos"));
		recibida.put("descripcion", request.getParameter("descripcion"));
		recibida.put("domicilio", request.getParameter("domicilio"));
		recibida.put("estado", Integer.parseInt(request.getParameter("estado")));
		recibida.put("fecha_nacimiento", request.getParameter("fecha_nacimiento"));
		recibida.put("nro_doc", Integer.parseInt(request.getParameter("nro_doc")));
		recibida.put("tipo_doc", Integer.parseInt(request.getParameter("tipo_doc")));
		recibida.put("telefono", request.getParameter("telefono"));

		persona = new Persona (recibida);
		salida.put ("result", persona.guardar());
		/* TODO: Agregar los datos guardados en la base de datos */
		return salida;
	}

	private JSONObject updateUsuario (HttpServletRequest request) {
		JSONObject recibido, salida;
		Usuario usuario;

		recibido = new JSONObject();
		salida = new JSONObject();

		if (request.getParameter("action").equals("new")) {
			recibido.put ("id_usuario", -1);
		} else {
			recibido.put("id_usuario", Integer.parseInt(request.getParameter("id_usuario")));
		}
		recibido.put("id_persona", Integer.parseInt(request.getParameter("id_persona")));
		recibido.put("nombre_usuario", request.getParameter("nombre_usuario"));
		recibido.put("password", request.getParameter("password"));
		recibido.put("email", request.getParameter("email"));
		recibido.put("descripcion", request.getParameter("descripcion"));
		recibido.put("estado", Integer.parseInt(request.getParameter("estado")));

		usuario = new Usuario (recibido);
		salida.put ("result", usuario.guardar());
		/* TODO: Agregar los datos guardados en la base de datos */
		return salida;
	}

	private JSONObject updateRol (HttpServletRequest request) {
		JSONObject recibido, salida;
		Rol rol;

		recibido = new JSONObject();
		salida = new JSONObject();

		if (request.getParameter("action").equals("new")) {
			recibido.put ("id_rol", -1);
		} else {
			recibido.put ("id_rol", Integer.parseInt (request.getParameter("id_rol")));
		}
		recibido.put ("nombre", request.getParameter("nombre"));
		recibido.put ("nombre_amigable", request.getParameter("nombre_amigable"));
		recibido.put ("descripcion", request.getParameter("descripcion"));
		recibido.put ("estado", Integer.parseInt(request.getParameter("estado")));

		rol = new Rol(recibido);
		salida.put("result", rol.guardar());
		/* TODO: Agregar los datos guardados en la base de datos */
		return salida;
	}

	private JSONObject deletePersona (HttpServletRequest request) {
		JSONObject salida = new JSONObject ();
		int id = Integer.parseInt (request.getParameter("id"));
		salida.put ("result", Persona.Eliminar(id));
		return salida;
	}

	private JSONObject deleteUsuario (HttpServletRequest request) {
		JSONObject salida = new JSONObject ();
		int id = Integer.parseInt (request.getParameter("id"));
		salida.put ("result", Usuario.Eliminar(id));
		return salida;
	}

	private JSONObject deleteRol (HttpServletRequest request) {
		JSONObject salida = new JSONObject ();
		int id = Integer.parseInt (request.getParameter("id"));
		salida.put ("result", Rol.Eliminar(id));
		return salida;
	}
}
