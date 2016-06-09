package gestionConfiguracion;

import java.util.HashMap;

class ConfiguradorThread implements Runnable {
	ConfiguracionDB config;
	boolean createSchema;

	public ConfiguradorThread (ConfiguracionDB config, boolean createSchema) {
		this.config = config;
		this.createSchema = createSchema;
	}

	@Override
	public void run () {
	}
}
