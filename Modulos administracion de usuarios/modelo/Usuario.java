package modelo;

import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Usuario extends BaseDatos {
	
	protected int id;
	protected int id_persona;
	protected String nombre;
	protected String password;
	protected String email;
	protected String descripcion;
	protected boolean estado;	
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
	protected static String vector_atributos[]={"id_usuario","id_persona","nombre_usuario","password","email","descripcion","estado"};
	protected static int cantidad_atributos=7;
	
	public static JSONObject[] Select(){
		return null;
	}
	
	public Usuario(){
		
	}
	
	public Usuario (JSONObject[] usuario){
		
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
