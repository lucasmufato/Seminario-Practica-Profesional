package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;

import org.json.simple.JSONObject;

import pruebas.Prueba;

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
		String consulta="SELECT * FROM persona";
		JSONObject[] json=null;
		ResultSet r= Prueba.RealizarConsulta(consulta);
		int i=0;
		int rows=0;
		try {
			
			if (r.last()) { //voy al final de las filas, obtengo el nro de la ultima y vuelvo al principio
			    rows = r.getRow();
			    r.beforeFirst();   
			}
			System.out.println("columnas: "+rows);
			json= new JSONObject[rows];
			
			int j=0;
			while (r.next()) {
				json[j]= new JSONObject();
				while(i<cantidad_atributos){
					//System.out.println("por guardar en el json el dato leido");
					//System.out.println("las variables son J:"+j+"  i:"+i+" atributo[i]:"+vector_atributos[i]);
					
					//en el json guardo el nombre del atributo y el valor leido de la BD
					json[j].put(vector_atributos[i], r.getObject(vector_atributos[i]));
					i++;
				}
				//cuando lei todos las columnas de ese renglon paso al siguiente, pasando tambien al siguiente json
				j++;
				i=0;
			}
			
		} catch (SQLException e) {
			System.out.println("error al realizar el select");
			e.printStackTrace();
		}
		
		return json;
	}
	
	public Persona(JSONObject persona){
		
	}
	
	@Override
	public boolean guardar(){
		boolean bandera=false;
		String p = "persona";
		String c = "id_persona";
		int desplazamiento_en_vector_atributos=0;
		if(this.id==-1){
			desplazamiento_en_vector_atributos++; //si es un create no debo enviar el id en el sql
		}
		String values="(";
		for(int i=desplazamiento_en_vector_atributos;i<cantidad_atributos-1;i++){
			values=values+"'"+vector_atributos[i]+"',";
		}
		values=values+"'"+vector_atributos[cantidad_atributos-1]+"')";
		return bandera;
	}
	
	public static boolean  Eliminar(int clave_primaria_tabla){
		return true;
	}
	
	public static boolean  Dar_alta(int clave_primaria_tabla){
		return true;
	}

}
