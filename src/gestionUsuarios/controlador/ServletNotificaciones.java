package gestionUsuarios.controlador;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

class ServletNotificaciones extends HttpServlet {

	@Override
	public void init() {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}

	private JSONObject verNoLeidas(HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		
		return salida;
	}

	private JSONObject marcarComoLeida(HttpServletRequest request) {
		JSONObject salida = new JSONObject();
		
		return salida;
	}
}