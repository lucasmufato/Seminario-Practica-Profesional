package gestionComisiones.controlador;

import gestionComisiones.modelo.EstadoComisionCobrada;
import gestionComisiones.modelo.Pago;
import gestionUsuarios.controlador.DAOAdministracionUsuarios;
import gestionUsuarios.modelo.Cliente;
import gestionUsuarios.modelo.Persona;
import gestionViajes.controlador.DAOViajes;
import gestionViajes.controlador.TestViaje;
import static gestionViajes.controlador.TestViaje.crearVehiculo;
import gestionViajes.modelo.EstadoPasajeroViaje;
import gestionViajes.modelo.PasajeroViaje;
import gestionViajes.modelo.Viaje;
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


public class TestComision extends TestCase{
    
        protected DAOViajes daoviajes = new DAOViajes();
	protected DAOComisiones daocomisiones = new DAOComisiones();
	
		
            
            @Before
	public void setUp() throws Exception {
		//este metodo se ejecuta antes de cada "parte" del test, osea antes de cada metodo
		//sirve para inicializar variables asi todos los test arrancan en el mismo entorno 
		
		//esto q sigue es codigo para vaciar la BD y que todas las pruebas corran en el mismo entorno
		this.daoviajes.vaciarTabla("Calificacion");
		this.daoviajes.vaciarTabla("Sancion");
                this.daoviajes.vaciarTabla("MovimientoPuntos");
                this.daoviajes.vaciarTabla("PasajeroViaje");
		this.daoviajes.vaciarTabla("ComisionCobrada");
		this.daoviajes.vaciarTabla("LocalidadViaje");
		//this.daoviajes.borrarRelacionesEntreViajes();
		this.daoviajes.vaciarTabla("Viaje");
		this.daoviajes.vaciarTabla("Maneja");
		this.daoviajes.vaciarTabla("Vehiculo");
        }
        
                @Test
		public void testCobrarComisionCorrecto(){
			//datos del vehiculo y cliente, para crear el vehiculo
			JSONObject json= crearVehiculo();
			try {
				//creo los datos en la tabla maneja
				assertTrue(this.daoviajes.NuevoVehiculo(json) );
			}catch(ExceptionViajesCompartidos E){
				fail(E.getMessage());
			}
				JSONObject json2 = TestViaje.crearViaje2();
			try {
				assertTrue( this.daoviajes.nuevoViaje(json2) );
			} catch (ExceptionViajesCompartidos e) {
				fail(e.getMessage());
			}
			
			JSONObject json3= this.crearPostulante5();
			JSONObject json4= this.crearPostulante6();
			
			try {
				assertTrue( this.daoviajes.Cliente_se_postula_en_viaje(json3) );
				assertTrue( this.daoviajes.Cliente_se_postula_en_viaje(json4) );
				
			} catch (ExceptionViajesCompartidos e) {
				fail(e.getMessage());
			}
			
			List viajes=this.daoviajes.selectAll("Viaje");
			Viaje viaje=(Viaje) viajes.get(0);
			try { 
				assertTrue( this.daoviajes.aceptarPasajero(8, viaje.getId_viaje()));
				assertTrue( this.daoviajes.aceptarPasajero(9, viaje.getId_viaje()));
			} catch (ExceptionViajesCompartidos e) {
				fail(e.getMessage());
			}
			//compruebo estados
			Cliente cliente = (Cliente) this.daoviajes.buscarPorPrimaryKey(new Cliente(), 8);
			PasajeroViaje pv = viaje.recuperar_pasajeroViaje_por_cliente(cliente);
			assertEquals(pv.getEstado(),EstadoPasajeroViaje.aceptado);
			assertEquals(pv.getComision().getEstado(),EstadoComisionCobrada.pendiente);
			boolean bandera = false ;
                        //verifico si le cobré la comision
                        if( (pv.getEstado().equals(EstadoPasajeroViaje.aceptado))	&& (pv.getComision().getEstado().equals(EstadoComisionCobrada.pendiente)) ){
                            this.daocomisiones.cobrarComision(pv);
                            if(pv.getComision().getEstado().equals(EstadoComisionCobrada.pagado)){
                                 bandera = true;
                            }else{ bandera= false;}
                        }
                        assertTrue(bandera);
			//Cliente cliente1 = (Cliente) this.daoviajes.buscarPorPrimaryKey(new Cliente(), 9);
			//PasajeroViaje pv1=viaje.recuperar_pasajeroViaje_por_cliente(cliente1);
			//assertEquals(pv1.getEstado(),EstadoPasajeroViaje.aceptado);
			//assertEquals(pv1.getComision().getEstado(),EstadoComisionCobrada.pendiente);
			
		}
        
                  @Test
		public void testAcreditarPago(){
			
                        /*
                        DAOAdministracionUsuarios daousr = new DAOAdministracionUsuarios();
                        Persona persona = new Persona();
                        JSONObject jsonP = new JSONObject();
                        jsonP.put("Id_persona", 1);
                        jsonP.put("tipo_dni", "dni");
                        jsonP.put("num_dni",35535238);
                        
                        JSONObject jsonC = new JSONObject();
                        jsonC.put("id_cliente", 2);
                        jsonC.put("nombre_usuario", "prueba_saldo");
                        daousr.nuevoCliente(jsonP, jsonC);
                        */
                        Pago pago = new Pago();
                        //Cliente cliente_busco = (Cliente) daousr.buscarUsuarioPorNombre("prueba_saldo");
                        Cliente cliente_busco= (Cliente) this.daoviajes.buscarPorPrimaryKey(new Cliente(), 2);
                        daocomisiones.sumarSaldo(cliente_busco.getId_usuario(), 100);
                        //recupero con saldo nuevo
                        Cliente cliente_actualizado = (Cliente) this.daoviajes.buscarPorPrimaryKey(new Cliente(), 2);
                        float nuevo_saldo= cliente_actualizado.getSaldo();
                        assertEquals(nuevo_saldo, 105);
		}
        
        
        
        
        
        
        
        //---------------------------------------------------------------------------//
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject crearPostulante1() {
		JSONObject json =new JSONObject();
		json.put("cliente", 5);
		List viajes= this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427201);
		json.put("localidad_bajada", 3427203);
		return json;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject crearPostulante2() {
		JSONObject json =new JSONObject();
		json.put("cliente", 6);
		List viajes=this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427202);
		json.put("localidad_bajada", 3427204);
		return json;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject crearPostulante3() {
		JSONObject json =new JSONObject();
		json.put("cliente", 4);
		List viajes=this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427200);
		json.put("localidad_bajada", 3427202);
		return json;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject crearPostulante4() {
		JSONObject json =new JSONObject();
		json.put("cliente", 7);
		List viajes=this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427204);
		json.put("localidad_bajada", 3427205);
		return json;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject crearPostulante5() {
		JSONObject json =new JSONObject();
		json.put("cliente", 8);
		List viajes=this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427204);
		json.put("localidad_bajada", 3427205);
		return json;
	}
	
           @SuppressWarnings({ "unchecked", "rawtypes" })
	private JSONObject crearPostulante() {
		/*
		 * JSON{
		 * "CLIENTE":ID_CLIENTE,
		 * "VIAJE":ID_VIAJE,
		 * "LOCALIDAD_SUBIDA":ID_LOCALIDAD,
		 * "LOCALIDAD_BAJADA": ID_LOCALIDAD
		 * } 
		 */
		JSONObject json =new JSONObject();
		json.put("cliente", 3);
		List viajes= this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427200);
		json.put("localidad_bajada", 3427205);
		return json;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JSONObject crearPostulante6() {
		JSONObject json =new JSONObject();
		json.put("cliente", 9);
		List viajes=this.daoviajes.selectAll("Viaje");
		Viaje viaje=(Viaje) viajes.get(0);
		json.put("viaje", viaje.getId_viaje());
		json.put("localidad_subida", 3427202);
		json.put("localidad_bajada", 3427204);
		return json;
	}
        
        @SuppressWarnings("unchecked")
	public static JSONObject crearVehiculo(){
		JSONObject json= new JSONObject();
		json.put("conductor", 2);
		JSONObject vehiculo= new JSONObject();
		vehiculo.put("patente", "abd123");
		vehiculo.put("anio", 1992);
		vehiculo.put("modelo", "viejo");
		vehiculo.put("marca", "mondeo");
		vehiculo.put("seguro", 'C');
		vehiculo.put("color","arcoiris");
		vehiculo.put("aire", 'S');
		vehiculo.put("asientos",5);
		json.put("vehiculo", vehiculo);
		return json;
	}
        
        
        @SuppressWarnings("unchecked")
	public static JSONObject crearViaje(){
		
		JSONObject viaje = new JSONObject();
		JSONObject json2 = new JSONObject();
		json2.put("vehiculo", "abd123");
		json2.put("cliente", 2);
		Timestamp fecha = new Timestamp((new java.util.Date()).getTime());
		fecha.setMinutes(59);
		viaje.put("fecha_inicio", fecha);
		viaje.put("cantidad_asientos", 2);
		viaje.put("precio", new Float(50.0));
		viaje.put("nombre_amigable", "prueba viaje");
		json2.put("viaje", viaje);
		JSONObject localidades= new JSONObject();
		localidades.put("origen",3427200 );
		localidades.put("destino",3427205 );
		JSONArray intermedio= new JSONArray();
		intermedio.add(3427201);
		intermedio.add(3427202);
		intermedio.add(3427203);
		intermedio.add(3427204);
		localidades.put("intermedios", intermedio);
		json2.put("localidades", localidades);
		return json2;
	}
}

