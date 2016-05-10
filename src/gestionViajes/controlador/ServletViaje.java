package gestionViajes.controlador;

import java.util.List;

import javax.servlet.http.HttpServlet;

import org.json.simple.JSONObject;

import gestionUsuarios.controlador.DAOAdministracionUsuarios;
import gestionUsuarios.modelo.*;
import gestionViajes.modelo.*;
import otros.ExceptionViajesCompartidos;
public class ServletViaje extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected DAOAdministracionUsuarios daoUsuarios;
	protected DAOViajes daoViajes;
	
	public List<Cliente> autocompletar_conductor(String clientes){
		return null;
	}
	
	public List<Localidad> autocompletar_localidad(String localidad){
		return null;
	}
	
	public List<Vehiculo> conseguir_vehiculos_cliente(Integer id_cliente){
		return null;
	}
	
	public JSONObject nuevo_viaje(JSONObject datos){
		/*
		 
		 EL JSON QUE RECIBE EL METODO TENDRIA LA SIGUIENTE FORMA:
		 { "LOCALIDADES": {"ORIGEN":"ID_LOCALIDAD","INTERMEDIO":ID_LOCALIDAD,.....,"DESTINO":ID_LOCALIDAD},
		 "VEHICULO": ID_VEHICULO,
		 "VIAJE": {FECHA_SALIDA, HS_SALIDA, CANTIDAD_ASIENTOS, NOMBRE_AMIGABLE, COSTO_VIAJE},
		 "VUELTA": {FECHA_SALIDA,HS_SALIDA,CANTIDAD_ASIENTOS, NOMBRE_AMIGABLE}
		 }
		 EL SERVLET MEDIANTE LA COOKIE AGREGA AL JSON EL DATO
		 "CLIENTE":ID_CLIENTE
		 		 
		 */
		try {
			daoViajes.nuevoViaje(datos);
		} catch (ExceptionViajesCompartidos e) {
			//ENVIA MENSAJE DE ERROR CON e.getMessage();
		}
		return null;
	}
	
	public JSONObject buscar_viaje(JSONObject datos){
		/*
		EL JSON QUE RECIBE LE METODO TENDRIA LA FORMA:
		{ "ORIGEN":ID_LOCALIDAD, "DESTINO":ID_LOCALIDAD, DESDE, HASTA, CONDUCTOR,ASIENTOS_LIBRES,ESTADO}	
		 */
		return null;
	}
	
	public JSONObject ver_viaje_detallado(Integer id_viaje){
		daoViajes.getConductorViaje( id_viaje);
		daoViajes.getAutoViaje( id_viaje);
		/*
		 EL JSON DE LA RESPUESTA DEBERIA TENER:
		 
		 Nota de Juan: contrastar este json con el de 'detalle_viaje.js' funcion: 'simular()'
		 {
		 	"CONDUCTOR":{NOMBREUSUARIO, FOTOPERFIL, FOTO_REGISTRO, REPUTACION},
		 	"VEHICULO":{FOTO,MARCA,MODELO, PATENTE,AÑO,AIRE_ACOND,SEGURO,VERIFICADO},
		 	"VIAJE":{FECHA,HORA,NOMBRE_AMIGABLE,IDA/VUELTA,ID_VIAJECOMPLEMENTO(es el id_ida o id_vuelta), COSTO,ESTADO,ASIENTOS_LIBRES
		 			"RECORRIDO":[ ORIGEN,INTERMDIO,....,DESTINO]
			//esto es para mostrarle funcionalidades(botones)
		 	"usuario_logueado":{es_conductor,es_pasajero,es_seguidor}
		 }
		 
		 */
		return null;
	}
	
	public JSONObject cliente_se_postula_a_viaje(Integer id_viaje, Integer id_localidad_subida, Integer id_localidad_bajada){
		//saco el id del cliente mediante la cookie
		return null;
	}
	
	public JSONObject comision_por_recorrido(Integer id_localidad_subida, Integer id_localidad_bajada, Integer id_viaje){
		return null;
	}
	
	public JSONObject ver_lista_pasajeros(Integer id_viaje){
		daoViajes.listarPasajerosPorViaje(id_viaje);
		daoViajes.nombreAmigablePorViaje(id_viaje);
		return null;
	}
	
	public JSONObject aceptar_rechazar_postulantes(Integer decision, Integer id_cliente_postulante, Integer id_viaje){
		if(decision==1){
			daoViajes.aceptarPasajero(id_cliente_postulante,id_viaje);
		}else{
			if(decision==2){
				daoViajes.rechazarPasajero(id_cliente_postulante,id_viaje);
			}
		}
		return null;
	}
}
