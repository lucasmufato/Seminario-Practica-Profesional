package controladores;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import modelo.Usuario;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AccessManager {

	public AccessManager(){
		
	}
	
	public static boolean HasPermiso(HttpServletRequest request, String nombrePermiso) {
		System.out.println("En AccessManager");
		Cookie c = getCookieUsuario(request);
		//esta logueado?
		if (c != null) {
			System.out.println("Esta logueado");
			String nombre = c.getValue();
			//tiene rol super usuario (id=0)? 
			JSONArray roles = Usuario.GetRolesPorNombre(nombre);
			System.out.println("Usuario "+nombre+" tiene roles: "+roles);
			if (tieneRol(roles,0)){
				System.out.println("Es superusuario");
				return true;
			}else{
				//tiene permiso?
				JSONArray permisos = Usuario.GetNombrePermisosUsuario(c.getValue());
				System.out.println("Permisos: "+permisos);
				return tieneValor(permisos,"nombre_permiso",nombrePermiso);
			}

		}
		return false;
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
		System.out.println("entre");
		for (int i=0; i<roles.size(); i++){
			int r = (Integer) roles.get(i);
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


