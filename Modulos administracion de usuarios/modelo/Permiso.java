package modelo;

import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Permiso extends BaseDatos {
	
	protected int id;
	protected String recurso;
	protected boolean lectura;
	protected boolean escritura;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
		protected static String vector_atributos[]={"id_rol","recurso","lectura","escritura"};
		protected static int cantidad_atributos=4;
	
	public static JSONObject[] Select(){
		return null;
	}
	
	public Permiso(JSONObject[] permiso){
		
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
