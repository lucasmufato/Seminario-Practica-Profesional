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
		JSONObject jsonResponse;
		boolean rst = false;

		PrintWriter writer = response.getWriter();

		action = request.getParameter("action");
		entity = request.getParameter("entity");

		if (entity.equals("persona")) {
			rst = updatePersona(request);
		} else if (entity.equals("usuario")) {
			rst = updateUsuario(request);
		} else if (entity.equals("rol")) {
			rst = updateRol(request);
		}


		jsonResponse = new JSONObject();
		jsonResponse.put("result", rst?1:0);
		
		writer.println(jsonResponse);
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

	private boolean updatePersona (HttpServletRequest request) {
		JSONObject recibida;
		Persona persona;

		recibida = new JSONObject();
		recibida.put("id_persona", Integer.parseInt(request.getParameter("id_persona")));
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
		return persona.guardar();
	}

	private boolean updateUsuario (HttpServletRequest request) {
		return true;
	}

	private boolean updateRol (HttpServletRequest request) {
		return true;
	}
}
