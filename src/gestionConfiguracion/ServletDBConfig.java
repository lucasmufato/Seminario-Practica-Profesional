package gestionConfiguracion;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import org.json.simple.JSONObject;


class ServletDBConfig extends HttpServlet {

	// Asegurate de que este objeto tenga exclusion mutua
	private EstadoCfg estado;

	@Override
	public void init () {
		this.estado = new EstadoCfg();
	}

	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Este se usa para pedir el estado de configuracion
		PrintWriter writer = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");

		synchronized (this.estado) {
			writer.print(this.estado.toJSON());
		}
	}

	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Este se usa para enviar los parametros de configuracion
		ConfiguracionDB cfg = new ConfiguracionDB();
		PrintWriter writer = response.getWriter();
		JSONObject salida = new JSONObject();
		String action, mode;

		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");

		if (!this.yaConfigurado()) {

			mode = request.getParameter("mode");
			cfg.host = request.getParameter("host");
			cfg.dbname = request.getParameter("dbname");
			cfg.username = request.getParameter("username");
			cfg.password = request.getParameter("password");
			cfg.port = Integer.parseInt(request.getParameter("port"));

			salida.put("result", true);

			// Iniciar thread que va a hacer todo

		} else {
			salida.put("result", false);
			salida.put("msg", "El servidor ya esta configurado");
		}
	}

	private boolean yaConfigurado() {
		// Devuelve true si ya esta configurado.
		File persxml = new File ("persistence.xml");

		if (persxml.exists()) {
			synchronized (this.estado) {
				estado.setEstado (EstadoCfg.COMPLETO);
				estado.setStep (6); // Ultimo paso
			}
			return true;
		} else {
			return false;
		}
	}

	public void setEstadoCfg (EstadoCfg est1) {
		synchronized (this.estado) {
			this.estado.setEstado(est1.getEstado());
			this.estado.setStep(est1.getStep());
		}
	}

}