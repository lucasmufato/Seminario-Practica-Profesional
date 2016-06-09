package gestionConfiguracion;

import java.util.HashMap;

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
		//Simulacion
		servlet.setEstadoCfg(this.estado);

		this.estado.setEstado(EstadoCfg.TRABAJANDO);
		try {
			for (int i =1; i< 6; i++) {
				Thread.sleep(1000);
				this.estado.setStep(i);
				servlet.setEstadoCfg(this.estado);
			}
			this.estado.setEstado(EstadoCfg.COMPLETO);
		} catch (InterruptedException e) {
			this.estado.setEstado(EstadoCfg.FALLO);
		}
		servlet.setEstadoCfg(this.estado);
	}
}
