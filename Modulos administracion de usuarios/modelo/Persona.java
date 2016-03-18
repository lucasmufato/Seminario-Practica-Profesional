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
	protected String fecha_nacimiento;
	protected String domicilio;
	protected String telefono;
	protected String descripcion;
	protected int estado;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
		protected static String vector_atributos[]={"id_persona","nombres","apellidos","tipo_doc","nro_doc","fecha_nacimiento","domicilio","telefono","descripcion","estado"};
		protected static int cantidad_atributos=10;
		protected static String tabla ="PERSONA";
		protected static String campo_pk = "id_persona";
	
	public static JSONObject[] Select(){
		String consulta="SELECT * FROM "+tabla;
		JSONObject[] json=null;
		ResultSet r= BaseDatos.RealizarConsulta(consulta);
		int i=0;
		int rows=0;
		try {
			
			if (r.last()) { //cuento la cantidad de filas del resultado
			    rows = r.getRow();
			    // Move to beginning
			    r.beforeFirst();   
			}
			//System.out.println("columnas: "+rows);
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
	
	public Persona(JSONObject json){ 
		this.id=(int)json.get("id_persona");
		this.nombres=(String)json.get("nombres");
		this.apellidos=(String)json.get("apellidos");
		this.descripcion=(String)json.get("descripcion");
		this.domicilio=(String)json.get("domicilio");
		this.estado=(int)json.get("estado");
		this.fecha_nacimiento=(String)json.get("fecha_nacimiento");
		this.nro_doc=(int)json.get("nro_doc");
		this.tipo_doc=(int)json.get("tipo_doc");
		this.telefono=(String)json.get("telefono");
		
	}
	
	public Persona(){
		this.id=2;
		this.nombres="pablo";
		this.apellidos="cabrera";
		this.descripcion="voy a tener el id=2";
		this.domicilio="cardales";
		this.estado=1;
		this.fecha_nacimiento="current_date()";
		this.nro_doc=33333333;
		this.tipo_doc=1;
		this.telefono="33333";
	}
	@Override
	public boolean guardar(){
boolean bandera=false;	
		
		//creo un pedaso del codigo automaticamente con la informacion en el vector_atributos
		String values="(";
		for(int i=1;i<cantidad_atributos-1;i++){
			values=values+vector_atributos[i]+",";
		}
		values=values+vector_atributos[cantidad_atributos-1]+")";
		
		//creo el query para ser enviado dependiendo si id de esta instacia es -1 para insert, u otro nro para update
		String query="";
		if(this.id==-1){
			query="INSERT INTO "+tabla+values+"VALUES ('"+this.nombres+"','"+this.apellidos+"','"+this.tipo_doc+"','"+
					this.nro_doc+"',"+this.fecha_nacimiento+",'"+this.domicilio+"','"+this.telefono
					+"','"+this.descripcion+"','"+this.estado+"');";
			
		}else{
			query="UPDATE "+tabla+" SET "
					+"nombres = '"+this.nombres+"',"+ "apellidos= '"+this.apellidos+"',"
					+"tipo_doc = '"+this.tipo_doc+"',"+"nro_doc = '"+this.nro_doc+"',"
					+"fecha_nacimiento = "+this.fecha_nacimiento+","+"domicilio = '"+this.domicilio+"',"
					+"telefono = '"+this.telefono+"',"+"descripcion = '"+this.descripcion+"',"
					+"estado = '"+this.estado+"' WHERE id_persona="+this.id+";";
		}
		this.Conectarse_BD();
		bandera=this.EnviarQuery(query);
		this.Desconectarse_BD();
		return bandera;
	}
	
	public static boolean  Eliminar(int clave_primaria_tabla){
		boolean bandera=false;
		bandera = BaseDatos.idExistente(tabla, campo_pk, clave_primaria_tabla);
			if(bandera){
				bandera=BaseDatos.QueryEliminar(tabla,campo_pk,clave_primaria_tabla);
			}
		
		return bandera;
	}
	
	public static boolean  Dar_alta(int clave_primaria_tabla){
		boolean bandera=false;
		bandera = BaseDatos.idExistente(tabla, campo_pk, clave_primaria_tabla);
			if(bandera){
				bandera=BaseDatos.QueryDarAlta(tabla,campo_pk,clave_primaria_tabla);
			}
		
		return bandera;
	}

}
