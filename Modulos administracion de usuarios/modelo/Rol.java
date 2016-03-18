package modelo;

import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Rol extends BaseDatos {
	
	protected int id;
	protected String nombre;
	protected String nombre_amigable;
	protected String descripcion;
	protected boolean estado;	
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
	protected static String vector_atributos[]={"id_rol","nombre","nombre_amigable","descripcion","estado"};
	protected static int cantidad_atributos=5;
	
	public static JSONObject[] Select(){
		return null;
	}
	
	public Rol(JSONObject[] rol){
		
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
