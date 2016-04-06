package controladores;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import modelo.Usuario;


public class ControladorLogin extends HttpServlet {


	public void init() throws ServletException
	{
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doPost de ControladorLogin!");
		String user = request.getParameter("usuario");
		String pass = request.getParameter("contrasena");
		if (user.equals("a") && pass.equals("a")) {
			response(response, "login ok");
		} else {
			response(response, "invalid login");
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
		out.println("<t1>" + msg + "</t1>");
		out.println("</body>");
		out.println("</html>");
	}
}
