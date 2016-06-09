package gestionConfiguracion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class EjecutadorQuery {
	protected ConfiguracionDB configuracion;
	protected String path_archivo;
	protected boolean conectado;
	protected Connection conn = null;
	
	public EjecutadorQuery(){
		this.conectado=false;
	}
	
	public boolean conectarse(){
		if(this.configuracion==null){
			return false;
		}
		if(this.conectado){
			return false;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");		
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
	    try {
	    	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
	    	//jdbc:mysql://localhost:3306/seminario
			conn = DriverManager.getConnection("jdbc:mysql://"+this.configuracion.host+":"+this.configuracion.port
					+"/"+this.configuracion.dbname,
					this.configuracion.username,this.configuracion.password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	    this.conectado=true;
		return true;
	}
	
	public boolean desconectarse(){
		if(conectado){
			try {
				conn.close();
				this.conectado=false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean ejecutarArchivo(String archivo){
		ScriptRunner runner = new ScriptRunner(this.conn,false , false);
		try {
			runner.runScript(new BufferedReader(new FileReader(archivo)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isConectado(){
		return conectado;
	}
	
	public ConfiguracionDB getConfiguracion() {
		return configuracion;
	}

	public void setConfiguracion(ConfiguracionDB configuracion) {
		this.configuracion = configuracion;
	}
}
