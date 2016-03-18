package modelo;

import java.util.Date;
import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Persona extends BaseDatos {
	
	protected int id;
	protected String nombres;
	protected String apellidos;
	protected int tipo_doc;
	protected long nro_doc;
	protected Date fecha_nacimiento;
	protected String domicilio;
	protected String telefono;
	protected String descripcion;
	protected boolean estado;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
		protected static String vector_atributos[]={"id","nombres","apellidos","tipo_doc","nro_doc","fecha_nacimiento","domicilio","telefono","descripcion","estado"};
		protected static int cantidad_atributos=10;
	
	public static JSONObject[] Select(){
		return null;
	}
	
	public Persona(JSONObject[] persona){
		
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
