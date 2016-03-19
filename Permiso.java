package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.json.simple.JSONObject;

public class Permiso extends BaseDatos {
	
	protected int id;
	protected String recurso;
	protected boolean lectura;
	protected boolean escritura;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
		protected static String vector_atributos[]={"id_permiso","recurso","lectura","escritura"};
		protected static int cantidad_atributos=4;
		protected static String tabla ="permiso";
		protected static String campo_pk = "id_permiso";
	
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
	
	public Permiso(JSONObject json){
		this.id = (int) json.get("id_permiso");
		this.recurso = (String) json.get("recurso");
		this.lectura = (boolean) json.get("lectura");
		this.escritura = (boolean) json.get("escritura");
	}
	
	public Permiso(){
		this.id = 3;
		this.recurso = "RECURSO";
		this.lectura = true;
		this.escritura = false;
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
			query="INSERT INTO "+tabla+values+"VALUES ('"+this.recurso+"','"+this.lectura+
					"','"+this.escritura+"');";
			
		}else{
			query="UPDATE "+tabla+" SET "
					+"recurso = '"+this.recurso+"',"
					+"lectura = '"+this.lectura+"',"
					+"escritura = "+this.escritura+"' WHERE id_persona="+this.id+";";
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
	
	public static boolean   Dar_alta(int clave_primaria_tabla){
		boolean bandera=false;
		bandera = BaseDatos.idExistente(tabla, campo_pk, clave_primaria_tabla);
			if(bandera){
				bandera=BaseDatos.QueryDarAlta(tabla,campo_pk,clave_primaria_tabla);
			}
		
		return bandera;
	}
	
}