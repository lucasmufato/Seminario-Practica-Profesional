package controladorjpa;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;

import modelojpa.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import controladorjpa.AccessManager;

public class Registro extends HttpServlet {

	DAOAdministracioUsuarios dao;

	public void init() throws ServletException
	{
		dao= new DAOAdministracioUsuarios();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doGet de ControladorRegistro");
		
		PrintWriter writer = response.getWriter();

		response.setContentType("application/json");

		if (AccessManager.EstaLogueado(request)){
			this.printDeniedRedirect(writer);
		}else{
			this.printAccept(writer);
		}
	}
	private void printAccept(PrintWriter writer) {
		JSONObject resultado;

		resultado = new JSONObject();
		resultado.put("result", true);

		writer.println(resultado);
	}
	private void printDeniedRedirect (PrintWriter writer) {
		JSONObject resultado;

		resultado = new JSONObject();
		resultado.put("result", false);
		resultado.put("redirect", "home.html");

		writer.println(resultado);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("En doPost de ControladorRegistro!");
		JSONObject out = null;

		// Setear driver
		PrintWriter writer = response.getWriter();
		
		String accion = request.getParameter("action");

		if (accion.equals("usuario_existe")){
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
		Usuario u = dao.buscarUsuarioPorNombre(user);
		if (u != null) {
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
		//JSONObject persona = this.cargarPersona(request);
		JSONObject cliente = this.cargarCliente(request);
		/*
		if (dao.nuevoCliente(persona,cliente)){
			out.put ("result", true);
			out.put ("msg", "El usuario ha sido registrado correctamente");
		}else{
			out.put ("result", false);
			out.put ("msg", "Error en registro de usuario");
		}
		*/
		return out;
	}
	private JSONObject cargarPersona(HttpServletRequest request){
		JSONObject persona = new JSONObject();
		
		persona.put ("id_persona", -1);
		persona.put("nombres", request.getParameter("persona[nombres]"));
		persona.put("apellidos", request.getParameter("persona[apellidos]"));
		persona.put("tipo_doc", Integer.parseInt(request.getParameter("persona[tipo_doc]")));
		persona.put("nro_doc", Integer.parseInt(request.getParameter("persona[nro_doc]")));
		persona.put("fecha_nacimiento", request.getParameter("persona[fecha_nacimiento]"));
		persona.put("sexo", request.getParameter("persona[sexo]"));
		persona.put("domicilio", request.getParameter("persona[domicilio]"));
		persona.put("telefono", request.getParameter("persona[telefono]"));
		persona.put("estado", "A");		
		
		return persona;
	}

	private JSONObject cargarCliente(HttpServletRequest request){
		JSONObject cliente = new JSONObject();
		/*
		cliente.put("id_usuario", -1);
		cliente.put("nombre_usuario", request.getParameter("usuario[nombre_usuario]"));
		cliente.put("password", request.getParameter("usuario[password]"));
		cliente.put("email", request.getParameter("usuario[email]"));
		cliente.put("descripcion", request.getParameter("usuario[descripcion]"));
		cliente.put("estado", "A");
		cliente.put("foto_registro", request.getParameter("cliente[foto_registro]"));
		*/
		String foto_uri = request.getParameter("cliente[foto_registro]") ;
		String foto_bytes = foto_uri.substring(foto_uri.indexOf(",")+1);
		
		byte[] data = Base64.getDecoder().decode(foto_bytes);

		try {
			PrintWriter a = new PrintWriter("C:\\Users\\Juan\\Desktop\\filename.txt");
			System.out.println("path info: "+request.getPathInfo());
			System.out.println("path translated: "+request.getPathTranslated());
			System.out.println("servlet path: "+request.getServletPath());
			System.out.println("real path servlet info: "+request.getRealPath(getServletInfo()));
			
			ServletContext servletContext = this.getServletContext();
			try {
				String s = servletContext.getRealPath("upload");
				try (OutputStream stream = new FileOutputStream(s+"/imagen.png")) {
				    stream.write(data);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} finally{
				
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		



		

		return cliente;
	}
	public void destroy()
	{
	}

}
