package pruebas;

import org.json.simple.JSONObject;

import controladorjpa.AccessManager;
import controladorjpa.AdministracionUsuarios;
import controladorjpa.ControladorLogin;
import controladorjpa.ControladorServletJPA;
import controladorjpa.DAOAdministracioUsuarios;
import controladorjpa.Registro;
import modelojpa.Cliente;
import modelojpa.Permiso;
import modelojpa.PermisoRol;
import modelojpa.Persona;
import modelojpa.Rol;
import modelojpa.Usuario;
import modelojpa.UsuarioRol;
import modelojpa.UsuarioRolID;

public class Probador {
	
	DAOAdministracioUsuarios dao= new DAOAdministracioUsuarios();
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Probador p = new Probador();
		p.menu();
		//para que cree los .class
		ControladorServletJPA cs= new ControladorServletJPA();
		ControladorLogin cl= new ControladorLogin();
		AccessManager am= new AccessManager();
		Registro r11 = new Registro();
		AdministracionUsuarios au= new AdministracionUsuarios();
		
		Persona per= new Persona();
		Usuario u= new Usuario();
		Rol r= new Rol();
		UsuarioRol ur= new UsuarioRol();
		Permiso permiso= new Permiso();
		PermisoRol pr= new PermisoRol();
		Cliente c= new Cliente();

	}	
	
	public void menu(){

		//para usar inicializar es conveninte haberle hecho truncate a toda la BD
		//inicializar sirve para crear un par de personas, usuarios, roles y permisos y asignarlos
		//inicializar();
	
		//probarDAOUsuarioConPersona();		//funca 
		//probarDAOselectAll();		//funca, hay q pasarle el .class.getSimpleName() y te tira devuelve la lista con los resultados
		//probarDAOselectAllJSON();	//funca, al contrario que el metodo anterior, este devuelve un arreglo de JSONObject
		//DAOpersistir();		//funca con todos, por ahi no con usuario a menos que ya tenga una persona asociada
		//DAOActualizar();		//funcaS seguro con clases q implementan JSONable
		//DAOasignarRolAUsuario(); //funca, tiene q ser un ROL y un USUARIO que estan con estado='a'
		//DAOEliminarClaveCompuesta();	//funciona para PermisoRol y UsuarioRol
		//DAOAsignarPermisoARol();	//funca, ROl y permiso tiene q tener estado 'a'
		//DAOdesasignarPermisoARol();		//funca
		//DAOdesasignarRolAUsuario();		//funca
		//DAObuscarUsuarioPorNombre();		//funca
		//DAONombreRolUsuario();		//funca
		//DAONombrePermisosDeUnUsuario();		//funca
		//DAOisUsuarioPass(); 		//funca
		//DAOCliente();			//se puede cargar una entidad con un json vacio o incompleto, los datos quedan en null
		//ConstruirClienteConJSONVacio();		//funca
		DAOnuevoCliente();
	}
	
	public void DAOnuevoCliente(){
		JSONObject persona, cliente;
		Persona p= new Persona();
		Cliente c= new Cliente();
		persona= p.toJSON();
		persona.remove("fecha_nacimiento");
		persona.put("fecha_nacimiento", "19920411");
		cliente= c.toJSON();
		boolean b=dao.nuevoCliente(persona,cliente);
		System.out.println(b);
	}
	
	private void ConstruirClienteConJSONVacio(){
		Cliente c= new Cliente();
		JSONObject json= new JSONObject();
		c.SetJSONObject(json);
	}
	
	public void DAOCliente(){
		System.out.println("creando cliente");
		Cliente c= new Cliente();
		c.setNombre_usuario("CLiente");
		c.setEmail("cliente@prueba.com");
		c.setPassword("jajaja");
		c.setPuntos(5);
		c.setReputacion(5);
		dao.persistirUsuarioConPersona(c,1); //se puede usar este metodo por que el cliente es un usuario (cliente hereda de usuario)
	}
	
	@SuppressWarnings("unused")
	private void inicializar(){
		//este metodo sirve para llenar la BD de instancias!
		//ejecutar una sola ves por q va a explotar diciendo q ya existen las clases
		
		
		//hacer 2 personas y 2 usuarios
		Persona p1= new Persona();
		System.out.println(	dao.persistir(p1)	);
		Usuario u1 = new Usuario();
		System.out.println(	dao.persistirUsuarioConPersona(u1, p1.getId_persona())	);
		Persona p2= new Persona();
		p2.setTipo_doc(2);						//cambio el tipo de documento, por q el tipo y nro de doc son clave unica.
		System.out.println(	dao.persistir(p2)	);
		Usuario u2 = new Usuario();
		u2.setNombre_usuario("segundo_user");	//cambio el username por q es clave unica.
		System.out.println(	dao.persistirUsuarioConPersona(u2, p2.getId_persona())	);
		
		//hacer varios roles y permisos
		Rol r1= new Rol();
		System.out.println(	dao.persistir(r1)	);
		Rol r2 = new Rol();
		System.out.println(	dao.persistir(r2)	);
		Rol r3 = new Rol();
		System.out.println(	dao.persistir(r3)	);
		
		Permiso pem1= new Permiso();
		System.out.println(	dao.persistir(pem1)	);
		Permiso pem2= new Permiso();
		System.out.println(	dao.persistir(pem2)	);
		Permiso pem3= new Permiso();
		System.out.println(	dao.persistir(pem3)	);
		
		//dar roles a los usuarios y permisos a los roles
							//(rol, usuario)
		
		System.out.println(	dao.asignarRolAUsuario(1, 2)	);
		System.out.println(	dao.asignarRolAUsuario(2, 2)	);
		System.out.println(	dao.asignarRolAUsuario(3, 2)	);
	
		
							//(rol, permiso)
		
		System.out.println(	dao.asignarPermisoARol(1, 3)	);
		System.out.println(	dao.asignarPermisoARol(2, 2)	);
		System.out.println(	dao.asignarPermisoARol(3, 3)	);
		
		
		//ya tendria que quedar la BD con suficientes datos como para hacer prubas y visualizar
		
	}
	
	@SuppressWarnings("unused")
	private void DAONombrePermisosDeUnUsuario() {
		System.out.println(dao.NombrePermisosDeUnUsuario("lucasmufato").toJSONString());
		
	}

	@SuppressWarnings("unused")
	private void DAOisUsuarioPass() {
		System.out.println(dao.isUsuarioPass("lucasmufato", "masterkey"));
		
	}

	@SuppressWarnings("unused")
	private void DAONombreRolUsuario() {
		System.out.println(
				dao.NombreRolUsuario("lucasmufato").toString()
				);
		
	}

	@SuppressWarnings("unused")
	private void DAObuscarUsuarioPorNombre() {
		Usuario u= dao.buscarUsuarioPorNombre("lucasmufato");
		System.out.println("usuarios recuperados");
		System.out.println(u.toString());
	}

	@SuppressWarnings("unused")
	private void DAOdesasignarRolAUsuario() {
								//(rol,usuario)
		 System.out.println(dao.desasignarRolAUsuario(4, 1));
		
	}

	@SuppressWarnings("unused")
	private void DAOdesasignarPermisoARol() {
								//(permiso,rol)
		 System.out.println(dao.desasignarPermisoARol(2, 4));
		
	}

	public void DAOAsignarPermisoARol(){
												//id_rol, id_permiso
	    System.out.println(dao.asignarPermisoARol(1,1));
	}
	
	public void DAOasignarRolAUsuario(){

	    System.out.println(dao.asignarRolAUsuario(4, 1));
	}
	
	public void DAOEliminarClaveCompuesta(){
		//para borrar un registro de la tabla intermedia entre usuario y rol
		dao.deletePorPrimaryKey(new UsuarioRol(), new UsuarioRolID(1,1));
		//para borrar un registro de la tabla intermedia entre permiso y rol
		//dao.deletePorPrimaryKey(new PermisoRol(), new PermisoRolID(1,2));		//funciona!
		
	}
	
	@SuppressWarnings("unused")
	private void probarDAOselectAllJSON(){
		JSONObject[] j=dao.selectAllJSON(Rol.class.getSimpleName());
		for(JSONObject json: j){
			System.out.println(json.toJSONString());
		}
	}
	
	@SuppressWarnings("unused")
	private void probarDAOselectAll() {
		dao.selectAll(Rol.class.getSimpleName());
		
	}

	@SuppressWarnings("unused")
	private void DAOActualizar(){ //si actualizas un registro y queda igual q antes, no lo guarda
		Rol r= new Rol();
		r.setId_rol(2);
		r.setDescripcion("mica");
		r.setEstado('b');
		r.setNombre_amigable("mica");
		r.setNombre_rol("micaela guerrero");
		dao.actualizar(r);
	}
	
	@SuppressWarnings("unused")
	private void DAOpersistir(){
		System.out.println("voy a persistir una entidad");
		Permiso p = new Permiso();
		p.setDescripcion("administrar_usuarios");
		p.setFuncionalidad("administrar_usuarios");
		p.setNombre_permiso("administrar_usuarios");
		dao.persistir(p);
	}	
	
	@SuppressWarnings("unused")
	private void probarDAOUsuarioConPersona(){
		Usuario u= new Usuario();
		u.setEmail("segundouser@yo");
		u.setNombre_usuario("segundouser");
		System.out.println(dao.persistirUsuarioConPersona(u,9));
	}
}
