package otros;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public abstract class DataAccesObject {
	
	protected EntityManagerFactory emfactory;
	protected EntityManager entitymanager;
	
	public DataAccesObject(){
		
	}
	

    public Object buscarPorClaveCandidata(String clase, Object clave_candidata){
    	try{
    		Query qry = entitymanager.createNamedQuery(clase+".buscarPorClaveCandidata");
    		qry.setParameter("clave_candidata", clave_candidata);
    		return qry.getResultList();
    		}catch(Exception e){
    			e.printStackTrace();
    			return null;
    		}
    }
    
    //devuelve una lista con todos los objetos de esa clase
	@SuppressWarnings("rawtypes")
	public List selectAll(String nombreDeLaClase){
		try{
		Query qry = entitymanager.createNamedQuery(nombreDeLaClase+".todos");
		return qry.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	//guarda en la BD cualquier objeto que le pases, puede explotar por FK, restricciones de la BD, etc
	public boolean persistir(Object o) {
		try{
			 entitymanager.getTransaction( ).begin( );
			 entitymanager.persist(o);
			 entitymanager.getTransaction( ).commit( );	
			 return true;
		}catch(Exception e){
			System.out.println("mensaje de causa de la excepcion: "+e.getCause());
			System.out.println("fin mjs excepcion");
			e.printStackTrace();
			return false;
		}
	}
	
	//borra de la BD el registro con esa PK, la PK puede ser compuesta, como la de PermisoRol
	//en ese caso su primarkey es un objeto del tipo PermiosRolID seteado con los ID correctos
	//en caso de claves comunes, se puede pasar un integer y funca bien.
	public boolean deletePorPrimaryKey(Object o, Object primaryKey){
		try{
			Object r= entitymanager.find((o).getClass(), primaryKey);
		    entitymanager.getTransaction( ).begin( );
		    entitymanager.remove(r);
		    entitymanager.getTransaction( ).commit( );	
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	    return true;
	}	

	// este metodo recibe un objeto, este debe sobreescribir otro en la BD. primero se busca el original por la clave primaria,
	//y despues al original se le cambian los valores con los del objeto recibido.
	public boolean actualizar(JSONable o) {
		entitymanager.getTransaction( ).begin( );
		try{
			Query q2 = entitymanager.createNamedQuery((o).getClass().getSimpleName()+".SearchById");
		    q2.setParameter("id", o.getPrimaryKey());
		    JSONable original=(JSONable)q2.getSingleResult();		//tengo el valor original
		    original.SetJSONObject(o.toJSON());						//reemplazo los DATOS del original por los nuevo, el original sigue teniendo su ID
		    entitymanager.getTransaction( ).commit( );
		}catch(Exception e){
			 entitymanager.getTransaction( ).rollback();
			e.printStackTrace();
			return false;
		}
	    return true;
	}
	
	//busca por clave primaria 
	public Object searchById(JSONable o){
		Object nuevo=null;
		try{
			Query q2 = entitymanager.createNamedQuery((o).getClass().getSimpleName()+".SearchById");
		    q2.setParameter("id", o.getPrimaryKey());
		   nuevo=q2.getSingleResult();
		}catch(Exception e){
			 entitymanager.getTransaction( ).rollback();
			e.printStackTrace();
			return null;
		}
		return nuevo;
	}
	
	//otro que busca por clave primaria, pero en este caso ya sabes la clave primaria
	public Object buscarPorPrimaryKey(Object clase, Object primaryKey){
		return entitymanager.find((clase).getClass(), primaryKey);
	}

	// Auto completado. Devuelve un JSONArray con los resultados
	public List autocompletar (String nombreDeLaClase, String busqueda) {
		try{
			Query qry = entitymanager.createNamedQuery(nombreDeLaClase+".autocompletar");
			qry.setParameter("busqueda", "%"+busqueda+"%");
			return qry.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}