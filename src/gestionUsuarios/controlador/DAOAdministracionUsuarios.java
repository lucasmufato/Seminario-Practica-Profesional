package gestionUsuarios.controlador;

import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.eclipse.persistence.jpa.JpaHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionUsuarios.modelo.*;
import otros.*;

@SuppressWarnings("unused")
public class DAOAdministracionUsuarios extends DataAccesObject {
    
    public DAOAdministracionUsuarios(){
    	super();
    	emfactory= Persistence.createEntityManagerFactory( "Viajes Compartidos" ); 
    	entitymanager = emfactory.createEntityManager( );
    }
    
    //-------------------------------------------------------------para la parte de administracion de usuarios------------------------------   
    
    //Metodo que devuelvo un JSONArray con:
    //permiso1, permiso3, permiso4... cada permiso en JSON
    @SuppressWarnings("unchecked")
	public JSONArray NombrePermisosDeUnUsuario(String nombre_usuario){
    	JSONArray jarray= new JSONArray();
    	Usuario lu= this.buscarUsuarioPorNombre(nombre_usuario);
		List<UsuarioRol>Userroles=lu.getRoles();
		for(UsuarioRol ur: Userroles){
			List<PermisoRol>Permroles = ur.getRol().getPermisos();
			for(PermisoRol pr: Permroles){
				jarray.add(pr.getPermiso().toJSON());
			}
		}
		return jarray;
    }
    
    //metodod que comprueba que el dado nombre de usuario y su password existan y sean el correcto.
    public boolean isUsuarioPass(String user, String pass) {
    	try{
    		Query q = this.entitymanager.createNamedQuery("Usuario.porNombrePass");
    		q.setParameter("nombre", user);
    		q.setParameter("pass", pass);
    		Usuario u = (Usuario) q.getSingleResult();
    		return u!=null;
			/*
    		Usuario u=this.buscarUsuarioPorNombre(user);
			String p= u.getPassword();
			if(p.equals(pass)){
				return true;
			}else{
				return false;
			}*/
    	}catch(Exception e){
    		return false;
    	}
	}
    
    //metodo que verficia que un usuario este activo
    public boolean isUsuarioActivo(String nombreUsuario){
    	Usuario u = this.buscarUsuarioPorNombre(nombreUsuario);
    	if (u!=null){
    		return u.getEstado().toString().equals("A");
    	}
    	return false;
    }
    
    //devuele un JSONArray con:
    // 	rol1, rol2, rol3..
    @SuppressWarnings("unchecked")
	public JSONArray NombreRolUsuario(String nombre_usuario) {
    	JSONArray jarray= new JSONArray();
    	Usuario lu= this.buscarUsuarioPorNombre(nombre_usuario);
		List<UsuarioRol>roles=lu.getRoles();
		for(UsuarioRol ur: roles){
			//ur.getRol().toJSON();
			jarray.add(ur.getRol().toJSON());
		}
		
		return jarray;
	}
    
    public boolean usuarioHasRol(String nombreUsuario, String nombreRol){
    	Usuario lu= this.buscarUsuarioPorNombre(nombreUsuario);
		List<UsuarioRol>roles=lu.getRoles();
		for(UsuarioRol ur: roles){
			if (ur.getRol().getNombre_rol().equals(nombreRol)){
				return true;
			}
		}
		return false;
    }

	public Usuario buscarUsuarioPorNombre(String nombre_usuario) {
    	try{
    		Query qry = entitymanager.createNamedQuery("Usuario.porNombreExacto");
    		qry.setParameter("nombre", nombre_usuario);
    		return (Usuario)qry.getSingleResult();
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
	}

	public boolean desasignarPermisoARol(int id_permiso, int id_rol) {
    	try{
			 this.deletePorPrimaryKey(new PermisoRol(), new PermisoRolID(id_permiso, id_rol));
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
		}
		return true;
	}
    
	public boolean desasignarRolAUsuario(int id_rol, int id_usuario) {
		try{
			this.deletePorPrimaryKey(new UsuarioRol(), new UsuarioRolID(id_usuario, id_rol));
		}catch(Exception e){
    		e.printStackTrace();
    		return false;
		}
		return true;
	}
    
    public boolean asignarPermisoARol(Integer id_rol,Integer id_permiso){
	    try{
		    //busco el rol y el permiso
		   Rol r =(Rol) this.buscarPorPrimaryKey(new Rol(), id_rol);
		   Permiso p= (Permiso) this.buscarPorPrimaryKey(new Permiso(), id_permiso);
	    	//chequeo q sus estados sean = 'a', osea q se les pueda asignar permisos
		    if(r.getEstado()!='A'){
		    	return false;
		    }
		    if(p.getEstado()!='A'){
		    	return false;
		    }
		    //al rol le asigo el permiso
		    //r.AsignarPermiso(p);
		    entitymanager.getTransaction( ).begin( );
		    r.AsignarPermiso(p);
		    //guardo la nueva info
		    entitymanager.getTransaction( ).commit( );	
	    }catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	    return true;
	}
    
    public boolean asignarRolAUsuario(Integer id_rol,Integer id_usuario){
    	try{
	    	//busca el usuario y el rol
		    Rol r =(Rol) this.buscarPorPrimaryKey(new Rol(), id_rol);
		   // Usuario u= (Usuario) this.buscarPorPrimaryKey(new Usuario(), id_usuario);
		    Usuario u1=new Usuario();
		    u1.setId_usuario(id_usuario);
		    Usuario u= (Usuario) searchById(u1);
		  //chequeo q sus estados sean = 'a', osea q se les pueda asignar rol
		    if(r.getEstado()!='A'){
		    	return false;
		    }
		    if(u.getEstado()!='A'){
		    	return false;
		    }
		    entitymanager.getTransaction( ).begin( );
		    //el usuario le asigna el rol
	    	u.asignarRol(r);
	    	//guardo la nueva info
	       	entitymanager.getTransaction( ).commit( );
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    //el metodo devuelve true aunque el estado ya estubiera dado de baja
    @SuppressWarnings("unchecked")
	public boolean darDeBaja(Integer id,String clase) {
		entitymanager.getTransaction( ).begin( );
		try{
			//busco la clase a dar de baja usando la namedquery que tendria q tener
		    Query q2 = entitymanager.createNamedQuery(clase+".SearchById");
		    //la busco por su ID
		    q2.setParameter("id",id);
		    //obtengo el resultado y lo asigno a una clase
		    JSONable original=(JSONable)q2.getSingleResult();
		    //de esa clase saco el JSON original y lo guardo en otra clase
		    JSONObject j=original.toJSON();
		    //al json original le cambio la variable estado
		    j.remove("estado");
		    j.put("estado", 'B');
		    //y ahora al original le pongo el json cambiado, lo unico distinto que tiene es q ahora tiene un estado dado de baja
		    original.SetJSONObject(j);
		    //hago el commit del cambio
		    entitymanager.getTransaction( ).commit( );
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
    
    //la persona ya debe estar persistida, sino explota
    public boolean persistirUsuarioConPersona(Usuario u, Integer id_persona){
    	try{
    		entitymanager.getTransaction( ).begin( );
    		Persona p=(Persona) this.buscarPorPrimaryKey(new Persona(), id_persona);	//obtengo la persona	
    		u.setPersona(p);															//se la asigno al usuario
			entitymanager.persist(u);													//guardo el usuario
			entitymanager.getTransaction( ).commit( );	
    		
    	}catch(Exception e){
    		return false;
    	}
    	return true;
    }
    
    //metodo que le pasas el nombre de la clase y te devuelve un arreglo de JSONObjects con todas las tuplas de la BD
    @SuppressWarnings("unchecked")
	public JSONObject[] selectAllJSON(String clase){
    	JSONObject[] json=null;
    	try{
	    	List<JSONable> lista= selectAll(clase);
	    	Integer size= lista.size();
			json=new JSONObject[size];
			Integer index=0;
			for(JSONable j: lista){
				//System.out.println(j.toString());
				json[index]= j.toJSON();
				index++;
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
		return json;		
    }
    
	//-------------------------------------------fin de la parte de administracion de usuarios----------------------------------------------
	//-------------------------------------------Registro Clientes-------------------------------------------------------------------------
	
    public Boolean nuevoCliente(JSONObject persona,JSONObject cliente) {
    	//creo los objectos a partir de los JSON recibidos
    	Persona p= new Persona(persona);
    	Cliente c= new Cliente(cliente);
    	try{
			 entitymanager.getTransaction( ).begin( );
			 c.setPersona(p);
			 System.out.println("id persona antes de insert: "+p.getId_persona());
			 entitymanager.persist(p);
			 System.out.println("id persona despues de insert: "+p.getId_persona());
			 System.out.println("id cliente antes de insert: "+c.getId_usuario());
			 entitymanager.persist(c);
			 System.out.println("id cliente despues de insert: "+c.getId_usuario());
			 Query qry = entitymanager.createNamedQuery("Rol.porNombre");
	    	 qry.setParameter("nombre", "cliente");
			 Rol r= (Rol) qry.getSingleResult();
			 c.asignarRol(r);
			 entitymanager.getTransaction( ).commit( );	
			 
			 System.out.println("id persona despues de commit: "+p.getId_persona());
			 System.out.println("id cliente despues de commit: "+c.getId_usuario());
			 Rol rol_recien_asignado= c.getRoles().get(0).getRol();
			 System.out.println("nombre del rol recien asignado: "+rol_recien_asignado.getNombre_rol());
			 
			 return true;
		}catch(Exception e){

			e.printStackTrace();
			return false;
		}    	
	}
    
	public boolean subirFotoRegistro(JSONObject foto) throws ExceptionViajesCompartidos {
		Cliente c = this.clientePorNombre(foto.get("usuario").toString());
		if (c!=null){
			if(this.entitymanager.getTransaction().isActive()){
				this.entitymanager.getTransaction().rollback();
			}
			this.entitymanager.getTransaction().begin();

			 c.setFoto_registro(foto.get("imagen").toString());
			
			try{
			 	entitymanager.getTransaction( ).commit( );	
			}catch(RollbackException e){
			  	String error= ManejadorErrores.parsearRollback(e);
			 	throw new ExceptionViajesCompartidos("ERROR: "+error);
			} 
			return true;   	
		}
		return false;
	}	
    
	public boolean subirFotoCliente(JSONObject foto) throws ExceptionViajesCompartidos {
    	Cliente c = this.clientePorNombre(foto.get("usuario").toString());
		if (c!=null){
			if(this.entitymanager.getTransaction().isActive()){
				this.entitymanager.getTransaction().rollback();
			}
			this.entitymanager.getTransaction().begin();

			 c.setFoto(foto.get("imagen").toString());
			
			try{
			 	entitymanager.getTransaction( ).commit( );	
			}catch(RollbackException e){
			  	String error= ManejadorErrores.parsearRollback(e);
			 	throw new ExceptionViajesCompartidos("ERROR: "+error);
			} 
			return true;
		}
		return false;
	}	
	
	public Cliente clientePorNombre(String nombre){
    	try{
    		Query qry = entitymanager.createNamedQuery("Cliente.buscarPorClaveCandidata");
    		qry.setParameter("clave_candidata", nombre);
    		return (Cliente) qry.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
    public boolean mailExiste(String mail){
    	try{
    		Query qry = entitymanager.createNamedQuery("Usuario.porEmail");
    		qry.setParameter("email", mail);
    		return !qry.getResultList().isEmpty();
    		}catch(Exception e){
    			e.printStackTrace();
    			return false;
    		}
    }
    
    public boolean documentoExiste(Integer tipo, BigInteger numero){
    	try{
    		Query qry = entitymanager.createNamedQuery("Persona.porNroYTipoDeDocumento");
    		qry.setParameter("nro_doc", numero);
    		qry.setParameter("tipo_doc", tipo);
    		return !qry.getResultList().isEmpty();
    		}catch(Exception e){
    			e.printStackTrace();
    			return false;
    		}
    }
    
	//-------------------------------------------fin de la parte de registro de clientes----------------------------------------------
    
	//-------------------------------------------PERFIL-------------------------------------------------------------------------------

    public boolean desactivarPerfil(String nombreUsuario) throws ExceptionViajesCompartidos{
		Usuario u = this.buscarUsuarioPorNombre(nombreUsuario);
		if (u == null){
			throw new ExceptionViajesCompartidos("El usuario ha desactivar no se encuentra registrado en el sistema");
		}
		if (!this.isUsuarioActivo(nombreUsuario)){
			throw new ExceptionViajesCompartidos("El usuario ya se encuentra desactivado");
		}
		
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();
		Character inactivo = "B".charAt(0);
		u.setEstado(inactivo);
		try{
		 	entitymanager.getTransaction( ).commit( );	
		}catch(RollbackException e){
		  	String error= ManejadorErrores.parsearRollback(e);
		 	throw new ExceptionViajesCompartidos("ERROR: "+error);
		}
		
		return true;
    }
    
    public boolean modificarPerfil(JSONObject perfil) throws ExceptionViajesCompartidos{
    	String nombre_usuario = perfil.get("nombre_usuario").toString();
    	if (nombre_usuario == null || nombre_usuario.isEmpty()){
    		throw new ExceptionViajesCompartidos("El nombre de usuario no es un valor válido");
    	}
    	Usuario u = this.buscarUsuarioPorNombre(nombre_usuario);
    	if (u==null){
    		throw new ExceptionViajesCompartidos("El usuario no se encuentra en el sistema");
    	}
    	Persona p = u.getPersona();
    	if (p==null){
    		throw new ExceptionViajesCompartidos("El usuario no tiene asignada una persona física válida");
    	}
    	String apellidos = perfil.get("apellidos").toString();
    	String nombres = perfil.get("nombres").toString();
    	String domicilio = perfil.get("domicilio").toString();
    	String telefono = perfil.get("telefono").toString();
    	java.sql.Date fecha_nacimiento = toDate(perfil.get("fecha_nacimiento").toString());
    	Character sexo = perfil.get("sexo").toString().charAt(0);
    	String mail = perfil.get("mail").toString();
    	String pass = perfil.get("pass").toString();
    	
    	if (apellidos==null || apellidos.isEmpty()){
    		throw new ExceptionViajesCompartidos("Debe completar el campo apellidos");
    	}
    	if (nombres == null || nombres.isEmpty()){
    		throw new ExceptionViajesCompartidos("Debe completar el campo nombres");
    	}    	
    	if (domicilio == null || domicilio.isEmpty() ){
    		throw new ExceptionViajesCompartidos("Debe completar el campo domicilio");
    	}
    	if (telefono == null || telefono.isEmpty()){
    		throw new ExceptionViajesCompartidos("Debe completar el campo telefono");
    	}
    	if (sexo == null){
    		throw new ExceptionViajesCompartidos("Debe completar el campo sexo");
    	}
    	if (mail == null || mail.isEmpty()){
    		throw new ExceptionViajesCompartidos("Debe completar el campo apellidos");
    	}else if (!mail.equals(u.getEmail()) && this.mailExiste(mail)){
    		throw new ExceptionViajesCompartidos("Mail ya existe en el sistema");
    	}
    	if (pass == null || pass.length()<6){
    		throw new ExceptionViajesCompartidos("Contraseña no válida");
    	}
    	
		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();

		p.setApellidos(apellidos);
		p.setDomicilio(domicilio);
		p.setFecha_nacimiento(fecha_nacimiento);
		p.setNombres(nombres);
		p.setSexo(sexo);
		p.setTelefono(telefono);

		try{
		 	entitymanager.getTransaction( ).commit( );	
		}catch(RollbackException e){
		  	String error= ManejadorErrores.parsearRollback(e);
		 	throw new ExceptionViajesCompartidos("ERROR: "+error);
		}

		if(this.entitymanager.getTransaction().isActive()){
			this.entitymanager.getTransaction().rollback();
		}
		this.entitymanager.getTransaction().begin();

		u.setPersona(p);
		u.setEmail(mail);
		u.setPassword(pass);
		try{
		 	entitymanager.getTransaction( ).commit( );	
		}catch(RollbackException e){
		  	String error= ManejadorErrores.parsearRollback(e);
		 	throw new ExceptionViajesCompartidos("ERROR: "+error);
		}
		
    	return true;
    }
    
	private Date toDate(String fecha_nacimiento) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed=null;
		try {
			parsed = format.parse(fecha_nacimiento);
		} catch (ParseException e) {
			//this.fecha_nacimiento=null;
			e.printStackTrace();
		}
        return new java.sql.Date(parsed.getTime());
	}
	//-------------------------------------------FIN de la parte de registro de PERFIL----------------------------------------------

}
