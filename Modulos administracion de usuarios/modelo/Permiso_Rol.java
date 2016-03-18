package modelo;

import java.util.Date;
import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Permiso_Rol extends BaseDatos {
	
	protected int id_permiso;
	protected int id_rol;
	protected Date fecha_modificicacion;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
		protected static String vector_atributos[]={"id_permiso","id_rol","fecha_modificacion"};
		protected static int cantidad_atributos=3;
	
	public static JSONObject[] Select(){
		return null;
	}
	
	public Permiso_Rol (JSONObject[] permiso_rol){
		
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
