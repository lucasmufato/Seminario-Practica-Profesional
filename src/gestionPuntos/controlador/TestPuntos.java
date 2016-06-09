package gestionPuntos.controlador;

import gestionPuntos.controlador.*;
import gestionPuntos.controlador.*;
import gestionComisiones.controlador.*;
import gestionComisiones.controlador.*;
import gestionComisiones.modelo.EstadoComisionCobrada;
import gestionComisiones.modelo.Pago;
import gestionUsuarios.controlador.DAOAdministracionUsuarios;
import gestionUsuarios.modelo.Cliente;
import gestionUsuarios.modelo.Persona;
import gestionUsuarios.modelo.Sponsor;
import gestionViajes.controlador.DAOViajes;
import gestionViajes.controlador.TestViaje;
import static gestionViajes.controlador.TestViaje.crearVehiculo;
import gestionViajes.modelo.EstadoPasajeroViaje;
import gestionViajes.modelo.PasajeroViaje;
import gestionViajes.modelo.Viaje;
import java.sql.Date;
import java.sql.Timestamp;
import junit.framework.TestCase;
import otros.ExceptionViajesCompartidos;
import java.util.List;
import javax.persistence.EntityManager;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;


public class TestPuntos extends TestCase{
    
        protected DAOViajes daoviajes = new DAOViajes();
	protected DAOComisiones daocomisiones = new DAOComisiones();
	protected DAOAdministracionUsuarios daoAdmUsu = new DAOAdministracionUsuarios();
        protected DAOPuntos daopuntos = new DAOPuntos();
        
        @Test
        public void testcrearBeneficio() throws ExceptionViajesCompartidos{
            Sponsor sponsor = nuevoSponsor();
            JSONObject json_beneficio = new JSONObject();
            json_beneficio.put("nombre_usuario", "juan_cardona_sponsor");
            json_beneficio.put("puntos_necesarios", 100);
            json_beneficio.put("producto", "Un auto");
            json_beneficio.put("fecha_caduca", (Date) Date.valueOf("2016-08-25"));
            try {            
                assertTrue(this.daopuntos.nuevoBeneficio(json_beneficio));
            }catch(ExceptionViajesCompartidos E){
			fail(E.getMessage());
            }
        }
    
		
		
        
        
        
        //-------------------------------------------------------------------------------//
	@SuppressWarnings("unchecked")
        public Sponsor nuevoSponsor() throws ExceptionViajesCompartidos{
            
            
            Persona p = (Persona) this.daoAdmUsu.buscarPorPrimaryKey(new Persona(), 2);
            JSONObject persona = new JSONObject();
            persona.put("id_persona", p.getId_persona());
            persona.put("nombres", p.getNombres());
            persona.put("apellidos", p.getApellidos());
            persona.put("tipo_doc", p.getTipo_doc());
            persona.put("nro_doc", p.getNro_doc());
            
            Date fn=   p.getFecha_nacimiento();
            String fn_str = String.valueOf(fn);
            persona.put("fecha_nacimiento", fn_str);
            persona.put("sexo", "M");            
            persona.put("domicilio", "aaaaaa 1111");
            persona.put("telefono", "99999");
            persona.put("descripcion", "descripcion!!!");
            persona.put("estado", "activo");
	
            Sponsor sponsor = new Sponsor();
            sponsor.setCuit(355436607);
            sponsor.setRubro("Informática");
            sponsor.setNombre_usuario("juan_cardona_sponsor");
            sponsor.setPassword("sponsorpass");
            sponsor.setEmail("juancardona@informatica.com");
            sponsor.setId_usuario(Integer.MIN_VALUE);
            
            JSONObject sp = new JSONObject();
            sp.put("rubro", sponsor.getRubro());
            sp.put("CUIT", sponsor.getCuit());
            sp.put("estado", "activo");
            sp.put("email", sponsor.getEmail());
            sp.put("nombre_usuario", sponsor.getNombre_usuario());
            sp.put("password", sponsor.getPassword());
            sp.put("id_usuario", sponsor.getId_usuario());
            
            
            JSONObject json = new JSONObject();
            json.put("persona",persona );
            json.put("sponsor",sp);            
            
            try {
				this.daoAdmUsu.nuevoSponsor(json);
			} catch (ExceptionViajesCompartidos e) {
				fail(e.getMessage());
			}
            Sponsor sp_recuperado = (Sponsor) this.daoAdmUsu.buscarPorClaveCandidata("Sponsor", "juan_cardona_sponsor");

        return sp_recuperado;
        }
        
        
        
        
        
}

