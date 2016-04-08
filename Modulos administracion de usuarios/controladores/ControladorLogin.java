package controladores;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import modelo.BaseDatos;
import modelo.Usuario;


public class ControladorLogin extends HttpServlet {


	public void init() throws ServletException
	{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doGet de ControladorLogin");

		// Cuando se desloguea
		Cookie c = getCookieUsuario(request);
		if (c!=null){
			//borro cookie
			c.setMaxAge(0);
			response.addCookie(c); 
		}
		response.sendRedirect("index.html");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("En doPost de ControladorLogin!");
		
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
				login(request,response);
			}else{
				response(response,"ya esta logueado");
			}
		}

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

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Parametros del formulario
		String user = request.getParameter("usuario");
		String pass = request.getParameter("contrasena");
		
		if (Usuario.isUsuarioPass(user,pass)) {
			//agrego cookie
			response.addCookie(setearCookie(user));
			// if rol=admin then
			//response.sendRedirect("abm.html");
			// if rol=usuario
			response.sendRedirect("index.html");
			//response.sendRedirect("testCookie.jsp"); // un jsp que hice para ver detalle de cookies
			
		} else {
			response(response, "login invalido");
		}
		
	}

	public void destroy()
	{
	}

	private void response(HttpServletResponse resp, String msg)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<p>" + msg + "</p>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private Cookie setearCookie(String user){
		Cookie cookie = new Cookie("nombre_usuario",user);//Seteo cookie con parametros: nombre y valor
		cookie.setMaxAge(60*60); // tiempo de vida de cookie = 1 hora
		return cookie;
	}
	
}
