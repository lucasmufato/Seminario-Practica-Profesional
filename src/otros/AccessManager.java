package otros;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import gestionUsuarios.controlador.*;
import gestionUsuarios.modelo.Usuario;
public class AccessManager {
	
	
	
	public AccessManager(){

	}
	
	public static boolean hasRol(HttpServletRequest request, String nombreRol){
		DAOAdministracionUsuarios dao= DAOAdministracionUsuarios.getInstance();
		if (EstaLogueado(request)) {
			JSONArray roles = dao.NombreRolUsuario(nombreUsuario(request));
			return tieneValor(roles,"nombre_rol",nombreRol);
		}
		return false;
	}
	
	public static boolean HasPermiso(HttpServletRequest request, String nombrePermiso) {
		DAOAdministracionUsuarios dao= DAOAdministracionUsuarios.getInstance();
		System.out.println("En AccessManager - HasPermiso");
		//esta logueado?
		if (EstaLogueado(request)) {
			String nombre = ValorCookie(request);
			//tiene rol super usuario (id=0)? 
			JSONArray roles = dao.NombreRolUsuario(nombre);
			System.out.println("Usuario "+nombre+" tiene roles: "+roles);
			if (tieneRol(roles,0)){
				System.out.println("Es superusuario");
				return true;
			}else{
				//tiene permiso?
				JSONArray permisos = 	dao.NombrePermisosDeUnUsuario(nombre);//Usuario.GetNombrePermisosUsuario(c.getValue());
				System.out.println("Permisos: "+permisos);
				return tieneValor(permisos,"nombre_permiso",nombrePermiso);
			}

		}
		return false;
	}
	
	public static boolean EstaLogueado(HttpServletRequest request){
		return getCookieUsuario(request) != null;
	}
	public static void SetearCookie(HttpServletResponse response, String user) {
		Cookie cookie = new Cookie("nombre_usuario",user);//Seteo cookie con parametros: nombre y valor
		cookie.setMaxAge(60*60*24*365); // tiempo de vida de cookie en segundos
		response.addCookie(cookie);
	}

	public static String nombreUsuario(HttpServletRequest request) {
		return ValorCookie(request);
	}

	public static int getIdUsuario(HttpServletRequest request) {
		DAOAdministracionUsuarios dao= DAOAdministracionUsuarios.getInstance();
		Usuario usuario = (Usuario) dao.buscarPorClaveCandidata("Usuario", nombreUsuario(request));
		return usuario.getId_usuario();
	}

	public static String ValorCookie(HttpServletRequest request) {
		return getCookieUsuario(request).getValue();
	}

	public static boolean EliminarCookie(HttpServletRequest request, HttpServletResponse response){
		Cookie c = getCookieUsuario(request);
		if (c!=null){
			//borro cookie
			c.setMaxAge(0);
			response.addCookie(c); 
			return true;
		}else{
			return false;
		}
	}
	private static boolean tieneValor(JSONArray lista, String propiedad, String valor){
		for (int i=0; i<lista.size(); i++){
			JSONObject p = (JSONObject) lista.get(i);
			if (p.containsKey(propiedad)){
				if (p.containsValue(valor)){
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * PARCHE
	 * si el jsonarray solo tiene un integer (como es el caso de la lista de roles), 
	 * no me permite la siguiente linea:
	 * JSONObject p = (JSONObject) lista.get(i);
	 * ya que no permite castear de entero a JSONObject
	 * como parche arme la funcion siguiente:
	 */
	private static boolean tieneRol(JSONArray roles, int rol){
		JSONObject objetoRol;
		System.out.println("entre");
		for (int i=0; i<roles.size(); i++){
			objetoRol = (JSONObject) roles.get(i);
			int r = (Integer)objetoRol.get("id_rol");
 			if (r==rol){
				System.out.println("valor: "+r);
				return true;
			}
		}
		return false;
	}
	private static Cookie getCookieUsuario(HttpServletRequest request) {
		Cookie[] cookie = request.getCookies();
		if (cookie != null){
			for (Cookie c: cookie){
				if (c.getName().equals("nombre_usuario")){
					return c;
				}
			}
		}
		return null;
	}


}


