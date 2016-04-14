package controladores;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import modelo.Usuario;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AccessManager {

	public AccessManager(){
		
	}
	
	public static boolean hasPermiso(HttpServletRequest request, String nombrePermiso) {
		System.out.println("En AccessManager");
		Cookie c = getCookieUsuario(request);
		if (c != null) {
			JSONArray permisos = Usuario.GetNombrePermisosUsuario(c.getValue());
			for (int i=0; i<permisos.size(); i++){
				JSONObject p = (JSONObject) permisos.get(i);
				if (p.containsKey("nombre_permiso")){
					if (p.containsValue(nombrePermiso)){
						return true;
					}
				}
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


