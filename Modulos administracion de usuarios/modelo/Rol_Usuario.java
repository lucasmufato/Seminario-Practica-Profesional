package modelo;

import java.util.Date;
import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Rol_Usuario extends BaseDatos {  //EL NOMBRE DE LA TABLA EN LA BASE DE DATOS SE LLAMA usuario_rol
	
	protected int id_usuario;
	protected int id_rol;
	protected Date fecha_modificacion;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
	protected static String vector_atributos[]={"id_usuario","id_rol","fecha_modificacion"};
	protected static int cantidad_atributos=3;
	
	public static JSONObject[] Select(){
		return null;
	}
	
	public Rol_Usuario (JSONObject rol_usuario){
		
	}
	
	
	@Override
	public boolean guardar(){
		return true;
	}
	
	public static boolean  Eliminar(int clave_primaria_tabla){
		return true;
	}
	
	public static boolean   Dar_alta(int clave_primaria_tabla){
		return true;
	}
}
