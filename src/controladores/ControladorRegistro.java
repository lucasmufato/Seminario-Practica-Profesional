package controladores;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import modelo.BaseDatos;
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
		
		String accion = request.getParameter("accion");

		if (accion.equals("validar_usuario")){
			//recibo el nombre del usuario y pregunto si existe o no.
			out = validarUsuario(request,response);
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
		if ("hola".equals("hola")) {
			System.out.println("Usuario existe");
			salida.put ("result", true); 
		}else{
			System.out.println("Usuario no existe");
			salida.put("result", false);
		}
		return salida;
	}

	public void destroy()
	{
	}
		
}
