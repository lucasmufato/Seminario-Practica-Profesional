package otros;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

public abstract class DataAccesObject {
	
	protected EntityManagerFactory emfactory;
	protected EntityManager entitymanager;
	
	public DataAccesObject(){
		this.emfactory= Persistence.createEntityManagerFactory( "Viajes Compartidos" ); 
    	this.entitymanager = emfactory.createEntityManager( );
	}
	
	public void vaciarTabla(String nombre_tabla){
		try{
			this.entitymanager.getTransaction().begin();
			Query q=this.entitymanager.createQuery("DELETE FROM "+nombre_tabla+" e ");
			q.executeUpdate();
			this.entitymanager.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    public Object buscarPorClaveCandidata(String clase, Object clave_candidata){
    	try{
    		Query qry = entitymanager.createNamedQuery(clase+".buscarPorClaveCandidata");
    		qry.setParameter("clave_candidata", clave_candidata);
    		return qry.getSingleResult();
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
	public boolean persistir(Object o) throws ExceptionViajesCompartidos {
		
		 entitymanager.getTransaction( ).begin( );
		 entitymanager.persist(o);
		 try{
	    		entitymanager.getTransaction( ).commit( );
	    		return true;
	    	}catch(RollbackException e){
	    		String error= ManejadorErrores.parsearRollback(e);
	    		throw new ExceptionViajesCompartidos("ERROR: "+error);
	    	}
		
	}
	
	//borra de la BD el registro con esa PK, la PK puede ser compuesta, como la de PermisoRol
	//en ese caso su primarkey es un objeto del tipo PermiosRolID seteado con los ID correctos
	//en caso de claves comunes, se puede pasar un integer y funca bien.
	public boolean deletePorPrimaryKey(Object o, Object primaryKey) throws ExceptionViajesCompartidos{
		try{
			Object r= entitymanager.find((o).getClass(), primaryKey);
		    entitymanager.getTransaction( ).begin( );
		    entitymanager.remove(r);
		    entitymanager.getTransaction( ).commit( );	
		}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
    	}
	    return true;
	}	

	// este metodo recibe un objeto, este debe sobreescribir otro en la BD. primero se busca el original por la clave primaria,
	//y despues al original se le cambian los valores con los del objeto recibido.
	public boolean actualizar(JSONable o) throws ExceptionViajesCompartidos {
		entitymanager.getTransaction( ).begin( );
		try{
			Query q2 = entitymanager.createNamedQuery((o).getClass().getSimpleName()+".SearchById");
		    q2.setParameter("id", o.getPrimaryKey());
		    JSONable original=(JSONable)q2.getSingleResult();		//tengo el valor original
		    original.SetJSONObject(o.toJSON());						//reemplazo los DATOS del original por los nuevo, el original sigue teniendo su ID
		    entitymanager.getTransaction( ).commit( );
		}catch(RollbackException e){
    		String error= ManejadorErrores.parsearRollback(e);
    		throw new ExceptionViajesCompartidos("ERROR: "+error);
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
	
	public boolean cerrarConexiones(){
		try{
			this.emfactory.close();
			this.entitymanager.close();
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
}
