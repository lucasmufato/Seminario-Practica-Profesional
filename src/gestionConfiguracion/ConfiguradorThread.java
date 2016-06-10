package gestionConfiguracion;

import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import javax.servlet.ServletContext;

class ConfiguradorThread implements Runnable {
	ConfiguracionDB config;
	boolean createSchema;
	ServletDBConfig servlet;
	EstadoCfg estado;

	public ConfiguradorThread (ConfiguracionDB config, boolean createSchema, ServletDBConfig servlet) {
		this.config = config;
		this.createSchema = createSchema;
		this.servlet = servlet;
		this.estado = new EstadoCfg();
	}

	@Override
	public void run () {
		boolean continuar = true;
		servlet.setEstadoCfg(this.estado);

		this.estado.setEstado(EstadoCfg.TRABAJANDO);

		//Paso 1: Crear archivo Persistence.xml
		if(crearPersistenceXML(config, servlet)) {
			this.estado.stepUp();
			servlet.setEstadoCfg(this.estado);
		} else {
			this.estado.setEstado(EstadoCfg.FALLO);
			continuar = false;
		}

		//Paso 2: Conectarse a base de datos
		if(continuar) {
			this.estado.stepUp();
			servlet.setEstadoCfg(this.estado);
		} else {
			this.estado.setEstado(EstadoCfg.FALLO);
			continuar = false;
		}

		//Paso 3: Crear tablas en base de datos
		if(continuar) {
			this.estado.stepUp();
			servlet.setEstadoCfg(this.estado);
		} else {
			this.estado.setEstado(EstadoCfg.FALLO);
			continuar = false;
		}

		//Paso 4: Cargar datos iniciales
		if(continuar) {
			this.estado.stepUp();
			servlet.setEstadoCfg(this.estado);
		} else {
			this.estado.setEstado(EstadoCfg.FALLO);
			continuar = false;
		}

		//Paso 5: Cargar informacion geografica
		if(continuar) {
			this.estado.stepUp();
			servlet.setEstadoCfg(this.estado);
		} else {
			this.estado.setEstado(EstadoCfg.FALLO);
			continuar = false;
		}

		// Si fallo en algun paso, eliminamos el archivo de configuracion
		if(this.estado.getEstado() == EstadoCfg.FALLO) {
			this.eliminarPersistenceXML();
		}
	}

	private boolean crearPersistenceXML (ConfiguracionDB config, ServletDBConfig servlet) {
		try {
			ServletContext context = servlet.getServletConfig().getServletContext();
			String inputPath = context.getRealPath ("/configurar/template_persistence.xml");
			String outputPath = context.getRealPath ("/WEB-INF/classes/META-INF/persistence.xml");

			Scanner input = new Scanner (new File (inputPath));
			PrintWriter output = new PrintWriter (new File (outputPath));

			String linea;
			while (input.hasNextLine()) {
				linea = input.nextLine();
				if (linea.indexOf("{{") != -1){
					output.println(this.reemplazar(linea, config));
				} else {
					output.println(linea);
				}
			}
			output.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean eliminarPersistenceXML() {
		try {
			ServletContext context = servlet.getServletConfig().getServletContext();
			String path = context.getRealPath ("/WEB-INF/classes/META-INF/persistence.xml");
			File f = new File(path);
			f.delete();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private String reemplazar (String texto, ConfiguracionDB config) {
		return texto.replace("{{USERNAME}}", config.username)
			.replace("{{PASSWORD}}", config.password)
			.replace("{{HOST}}", config.host)
			.replace("{{PORT}}", (new Integer (config.port)).toString())
			.replace("{{DBNAME}}", config.dbname);
	}

}
