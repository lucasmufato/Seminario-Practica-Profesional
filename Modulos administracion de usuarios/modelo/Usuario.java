package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.json.simple.JSONObject;

import pruebas.Prueba;

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
	protected static String tabla ="usuario";
	protected static String campo_pk = "id_usuario";
	
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
	
	public Usuario(){
		this.id=1;
		this.id_persona= 1;
		this.nombre= "user_lucas";
		this.password= "maximo";
		this.email= "l.mufato@gmail.com";
		this.descripcion= "programando";
		this.estado= true;
	}
	
	public Usuario (JSONObject json){
		this.id=(int) json.get("id_usuario");
		this.id_persona= (int) json.get("id_persona");
		this.nombre= (String) json.get("nombre_persona");
		this.password= (String) json.get("password");
		this.email= (String) json.get("email");
		this.descripcion= (String) json.get("descripcion");
		this.estado= (boolean) json.get("estado");
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
		
//protected static String vector_atributos[]={"id_usuario","id_persona","nombre_usuario","password","email","descripcion","estado"};
		
		//creo el query para ser enviado dependiendo si id de esta instacia es -1 para insert, u otro nro para update
		String query="";
		if(this.id==-1){
			query="INSERT INTO "+tabla+values+"VALUES ('"+this.id_persona+"',"+this.nombre+"´,"+this.password+"´,"+this.email
					+"´,"+this.descripcion+"´,"+this.estado+");";
			
		}else{
			query="UPDATE "+tabla+" SET "
					+"id_persona = '"+this.id_persona+"',"
							+ "nombre_usuario= "+this.nombre+"password = '"+this.password+"',"
							+"email = '"+this.email+"',"+"descripcion = '"+this.descripcion+"',"
							+"estado = '"+this.estado+"'"+" WHERE id_usuario="+this.id+";";
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
