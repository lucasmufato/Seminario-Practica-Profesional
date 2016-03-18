package pruebas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import modelo.BaseDatos;
import modelo.Usuario;
import org.json.simple.JSONObject;

public class Prueba extends BaseDatos {
	
	//atributos a persistir y que tendrian que ser enviados atraves de JSON
	protected int idprueba;
	protected String palabra;
	protected int estado;
	
	//atributos de la clase para su funcionamiento y facilidad de codigo
	protected static String vector_atributos[]={"idprueba","palabra","estado"};
	protected static int cantidad_atributos=3;
	
	public static void main(String[] args) {
		Prueba p = new Prueba();
		
		//Prueba.Select(); PARTE DE LUZ
				String query="";
				p.mandar_query(query);
		
		/*
		String query="INSERT INTO prueba(palabra,estado) VALUES('primer',1)";
		p.mandar_query(query);
		query="INSERT INTO prueba(palabra,estado) VALUES('segundo',1)";
		p.mandar_query(query);
		query="INSERT INTO prueba(palabra,estado) VALUES('tercer',1)";
		p.mandar_query(query);
		*/
		
		/*
		JSONObject[] j=Prueba.Select1();
		for(int i=0; i<j.length;i++){
			for(int w=0;w<cantidad_atributos;w++){
				System.out.println(vector_atributos[w]+" "+j[i].get(vector_atributos[w]));
			}
			System.out.println("el json tiene la forma:");
			System.out.println(j[i].toString());
		}
		*/
	}
	
	public static JSONObject[] Select(){		
		String consulta="SELECT * FROM prueba";
		JSONObject[] json=null;
		ResultSet r= Prueba.RealizarConsulta(consulta);
		int i=0;
		int rows=0;
		try {
			
			if (r.last()) { //cuento la cantidad de filas del resultado
			    rows = r.getRow();
			    // Move to beginning
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
	
	public Prueba(){
		
	}
	
	public Prueba (JSONObject[] usuario){
		
	}
	
	//este metodo es inventado para probar nomas
	public void mandar_query(String query){
		this.Conectarse_BD();
		//this.EnviarQuery(query);
		//System.out.println("Llamando a guardar");
				System.out.println(this.guardar());
		this.Desconectarse_BD();
	}
	
	@Override
	public boolean guardar(){
		boolean bandera;
		String p = "prueba";
		String c = "idPrueba";
		int identificador = 92;
		bandera = idExistente(p, c, identificador);
		return bandera;
	}
	
	public static boolean  Eliminar(int clave_primaria_tabla){
		return true;
	}
	
	public static boolean   Dar_alta(int clave_primaria_tabla){
		return true;
	}
	
}
