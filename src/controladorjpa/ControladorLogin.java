package controladorjpa;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;


@SuppressWarnings("serial")
public class ControladorLogin extends HttpServlet {
	
	DAOAdministracioUsuarios dao= new DAOAdministracioUsuarios();

	public void init() throws ServletException
	{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doGet de ControladorLogin");
		
		// Cuando se desloguea
		if (AccessManager.EstaLogueado(request)){
			if (AccessManager.EliminarCookie(request, response)){
				System.out.println("Log out: cookie eliminada");
				response.sendRedirect("index.html");
			}
		}
	}

	@SuppressWarnings("unchecked")
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
		
		String accion = request.getParameter("accion");

		if (accion.equals("login")){
			if (getCookieUsuario(request) == null){//pregunto si esta logueado
				out = login(request,response);
			}
		}
		
		if (out == null) {
			out = new JSONObject();
			out.put ("result", false);
		}
		
		out.put("action", accion);
		System.out.println("Lo que mando al js: "+out);
		writer.println (out);

	}

	private Cookie getCookieUsuario(HttpServletRequest request) {
		Cookie[] cookie = request.getCookies();
		if (cookie != null){
			for (Cookie c: cookie){
				if (c.getName().equals("nombre_usuario")){
					return c;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private JSONObject login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Parametros del formulario
		String user = request.getParameter("usuario");
		String pass = request.getParameter("pass");
		JSONObject salida = new JSONObject ();		
		if (dao.isUsuarioPass(user,pass)) {
			//agrego cookie
			AccessManager.SetearCookie(response,user);
			System.out.println("Usuario logueado");
			salida.put ("result", true); 
			salida.put ("msg", "El usuario se ha logueado correctamente.");
			salida.put("redirect", "home.html");
		}else{
			salida.put("result", false);
			salida.put ("msg", "<p>Usuario o contrase&ntilde;a incorrectas.</p>");
		}
		return salida;
	}

	public void destroy()
	{
	}

		
}
