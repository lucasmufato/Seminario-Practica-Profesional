package modelo;

import java.util.Hashtable;

import org.json.simple.JSONObject;

import java.sql.*;

public class BaseDatos {
	
	//atributos para la conexion a la BD
	protected static String usuario_BD="root";
	protected static String contraseña_BD="root";
	protected static String IP_BD="localhost";
	protected static int puerto_BD=3306;
	protected static String nombre_BD="seminario";
	
	//variables de uso de la BD
	protected Connection conexion = null;
	
	
	
	public static JSONObject[] Select(){
		System.out.println("Select de la superclase BaseDatos");
		return null;
	}
	
	public boolean guardar(){
		
		return true;
	}
	
	public static boolean Eliminar(int clave_primaria_tabla){
		System.out.println("Eliminar de la superclase BaseDatos");
		return true;
	}
	
	public static boolean Dar_alta(int clave_primaria_tabla){
		System.out.println("Dar_Arta de la superclase BaseDatos");
		return true;
	}
	
	protected boolean Conectarse_BD(){
		
		try {
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		    System.out.println("Obtuve bien la instacia del driver");
		} catch (Exception e) {
			System.out.println("error al conectarse con la BD");
		    System.out.println(e.toString());
		}
		
		try {
			String datosBD= "jdbc:mysql://"+this.IP_BD+"/"+this.nombre_BD;
			//String user_password="user="+this.usuario_BD+"password="+contraseña_BD;
			conexion = DriverManager.getConnection(datosBD,this.usuario_BD, this.contraseña_BD);
			System.out.println("me pude conectar a la BD: "+this.nombre_BD);
		} catch (SQLException ex) {
			System.out.println("no se pudo conectar");
		    System.out.println("SQLException: " + ex.getMessage());
		}
		
		return true;
	}
	
	protected boolean Desconectarse_BD(){
		if(conexion!=null){
			try {
				conexion.close();
				System.out.println("me pude desconectar");
			} catch (SQLException e) {
				System.out.println("Error al cerrar la conexion");
				e.printStackTrace();
			}
		}
		return true;
	}
	
	protected boolean EnviarQuery(String query){
		ResultSet rs = null;
		try {
			Statement cmd = conexion.createStatement();
			//rs = cmd.executeQuery(query);
			cmd.executeUpdate(query);
			System.out.println("el query salio bien! :D");
		} catch (SQLException e) {
			System.out.println("Error al enviar query:");
			System.out.println(query);
			e.printStackTrace();
		}
		
		return false;
	}

	protected static ResultSet RealizarConsulta(String consulta){
		ResultSet rs = null;
		
		try {
			String datosBD= "jdbc:mysql://"+IP_BD+"/"+nombre_BD;
			Connection conexion = DriverManager.getConnection(datosBD,usuario_BD, contraseña_BD);
			System.out.println("me pude conectar a la BD: "+nombre_BD);

			Statement cmd = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = cmd.executeQuery(consulta);
			System.out.println("la consulta salio bien! :D");
			
			//conexion.close();
			//System.out.println("me pude desconectar");
		} catch (SQLException e) {
			System.out.println("Error al enviar consulta:");
			System.out.println(consulta);
			e.printStackTrace();
		}
		
		return rs;
	}
	
	protected boolean idExistente(String tabla, String campo, int id){
		boolean existe = false;
		ResultSet rs;
		String consulta = "SELECT * FROM " + tabla + " WHERE " + tabla +"."+ campo + " = " + id;
		rs = RealizarConsulta(consulta);
		try {
			if (rs.next()) {
				existe = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}
	
}
