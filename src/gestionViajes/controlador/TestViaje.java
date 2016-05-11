package gestionViajes.controlador;

import static org.junit.Assert.*;

import java.sql.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import gestionUsuarios.modelo.Cliente;
import gestionViajes.modelo.*;
import junit.framework.TestCase;
import otros.ExceptionViajesCompartidos;

public class TestViaje extends TestCase {

	protected DAOViajes daoviajes = new DAOViajes();
	
	//ESTOY SIGUIENDO EL TUTORIAL DE http://www.tutorialspoint.com/junit/index.htm
	//VAN A TENER Q AGREGAR LA LIBRERIA JUint 4, que contiene:
	//junit.jar
	//org.hamcrest.core_1.3.0.v201303031735
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception { 
		//este metodo se ejecuta antes que arranque el test	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//este se ejecuta cuando termina todo el test
	}

	@Before
	public void setUp() throws Exception {
		//este metodo se ejecuta antes de cada "parte" del test, osea antes de cada metodo
		//sirve para inicializar variables asi todos los test arrancan en el mismo entorno
		
		//esto q sigue es codigo para vaciar la BD y que todas las pruebas corran en el mismo entorno
		this.daoviajes.vaciarTabla("LocalidadViaje");
		this.daoviajes.borrarRelacionesEntreViajes();
		this.daoviajes.vaciarTabla("Viaje");
		this.daoviajes.vaciarTabla("Maneja");
		this.daoviajes.vaciarTabla("Vehiculo");
	}

	@After
	public void tearDown() throws Exception {
		//este metodo se ejecuta despues de cada parte del test
		//O SE PUEDE PONER ACA EL CODIGO PARA VACIAR LA BD
	}

	@Ignore		//ignoro el test, si lo quiere probar reemplazenlo por @Test
	public void testEjemplo() {
		//test data
		int num= 5;
		String temp= null;
		String str= "Junit is working fine";
		
		//SI ALGO DA FALLO A MITAD DEL METODO DEL TEST, ESTE DEJA DE EJECUTARSE Y QUEDA COMO FALLO
		//SE PUEDE PROBAR CAMBIADO UNA LETRA DE LA FRASE DE ABAJO
		
		//check for equality
		assertEquals("Junit is working fine", str);
		
		//check for false condition
		assertFalse(num > 6);
		
		//check for not null value
		assertNotNull(str);
		
		//count the number of test cases
		System.out.println("No of Test Case = "+ this.countTestCases());
		
		//test getName 
		String name= this.getName();
		System.out.println("Test Case Name = "+ name);
		
		//test setName
		this.setName("testNewAdd");
		String newName= this.getName();
		System.out.println("Updated Test Case Name = "+ newName);
	}
	
	@Test
	public void testNuevoViajeCorrectoSINVUELTA() {
		//test q envie un json correcto y tendria q andar bien
		//datos del vehiculo y cliente, para crear el vehiculo
		JSONObject json= crearVehiculo();
		try {
			//creo los datos en la tabla maneja
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
		}catch(ExceptionViajesCompartidos E){
			fail(E.getMessage());
		}

		JSONObject json2 = this.crearViaje();
		try {
			assertTrue( this.daoviajes.nuevoViaje(json2) );
		} catch (ExceptionViajesCompartidos e) {
			fail(e.getMessage());
		}
		int i=0;
		i++;
	}
	
	@Test
	public void testNuevoViajeCorrectoCONVUELTA() {
		//test q envie un json correcto y tendria q andar bien
		//datos del vehiculo y cliente, para crear el vehiculo
		JSONObject json= crearVehiculo();
		try {
			//creo los datos en la tabla maneja
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
		}catch(ExceptionViajesCompartidos E){
			fail(E.getMessage());
		}

		JSONObject json2 = this.crearViaje();
		JSONObject vuelta = new JSONObject();
		vuelta.put("fecha_inicio",new Date((new java.util.Date()).getTime()) );
		vuelta.put("cantidad_asientos", 2);
		vuelta.put("nombre_amigable", "prueba viaje");
		json2.put("vuelta", vuelta);
		
		try {
			assertTrue( this.daoviajes.nuevoViaje(json2) );
		} catch (ExceptionViajesCompartidos e) {
			fail(e.getMessage());
		}
		int i=0;
		i++;
	}
	
	@Test
	public void testNuevoViajeINcorrectoSINVUELTA(){
		//el viaje se crea sin localidades
		boolean bandera=false;
		JSONObject json= crearVehiculo();
		try {
			//creo los datos en la tabla maneja
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
		}catch(ExceptionViajesCompartidos E){
			fail(E.getMessage());
		}
		JSONObject json2 = this.crearViaje();
		json2.remove("localidades");
		try {
			this.daoviajes.nuevoViaje(json2);
			fail("no tiro exception");
		} catch (ExceptionViajesCompartidos e) {
			bandera=true;
		}
		assertTrue(bandera);
	}
	
	@Test
	public void testNuevoViajeINcorrectoSINVUELTA3(){
		//el viaje se crea sin destino
		boolean bandera=false;
		JSONObject json= crearVehiculo();
		try {
			//creo los datos en la tabla maneja
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
		}catch(ExceptionViajesCompartidos E){
			fail(E.getMessage());
		}
		JSONObject json2 = this.crearViaje();
		JSONObject json3= (JSONObject) json2.get("localidades");
		json3.remove("destino");
		try {
			this.daoviajes.nuevoViaje(json2);
			fail("no tiro exception");
		} catch (ExceptionViajesCompartidos e) {
			String msj_error=e.getMessage();
			if(msj_error.contains("error no parseado")){
				fail("no se parseo bien el error");
			}else{
				bandera=true;				
			}
			
		}
		assertTrue(bandera);
	}
	
	@Test
	public void testNuevoViajeINcorrectoSINVUELTA2(){
		//el viaje se crea con id_destino= 1 (no existe esa localidad) 
		boolean bandera=false;
		JSONObject json= crearVehiculo();
		try {
			//creo los datos en la tabla maneja
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
		}catch(ExceptionViajesCompartidos E){
			fail(E.getMessage());
		}
		JSONObject json2 = this.crearViaje();
		JSONObject json3= (JSONObject) json2.get("localidades");
		json3.remove("destino");
		json3.put("destino", 1);
		try {
			this.daoviajes.nuevoViaje(json2);
			fail("no tiro exception");
		} catch (ExceptionViajesCompartidos e) {
			String msj_error=e.getMessage();
			if(msj_error.contains("error no parseado")){
				fail("no se parseo bien el error");
			}else{
				bandera=true;				
			}
			
		}
		assertTrue(bandera);
	}
	/*
	@Test
	public void testNuevoViajeDatosIncorrecto() {
		//teste que envia un json incorrecto y tendria q mostrar un error de alguna forma
		JSONObject json= new JSONObject();
		try {
			this.daoviajes.nuevoViaje(json);
		} catch (ExceptionViajesCompartidos e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	@Test
	public void testgetConductorViajeCorrecto() {
		//test q envie un viaje que existe
		
	}
	
	/*
	@Test
	public void testgetConductorViajeIncorrecto() {
		//test q envie un viaje que no existe
		JSONObject json= new JSONObject();
		try {
			this.daoviajes.nuevoViaje(json);
		} catch (ExceptionViajesCompartidos e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	@Test
	public void testbuscarManejar() {	//test q buscar en la tabla Maneja
		
		//json con datos de vehiculo
		JSONObject json= crearVehiculo();
		try {
			//creo los datos en la tabla maneja
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
			//recupero el vehiculo
			Cliente c= (Cliente) this.daoviajes.buscarPorPrimaryKey(new Cliente(), 2);
			Vehiculo auto=(Vehiculo)this.daoviajes.buscarPorClaveCandidata("Vehiculo", "abd123");
			Integer id_auto=auto.getId();
			//ahora pruebo el metodo 
			Maneja maneja=(Maneja)this.daoviajes.buscarPorIDCompuesta("Maneja", c, auto);
			assertNotNull(maneja);
			System.out.println(maneja);
		} catch (ExceptionViajesCompartidos e) {
			fail(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNuevoAutoCorrecto() {	//test q envie un usuario q existe, y vehiculo con datos bien.
		
		//json con datos de vehiculo
		JSONObject json= crearVehiculo();
		
		try {
			//pruebo que el metodo devuelva true
			assertTrue(this.daoviajes.NuevoVehiculo(json) );	
		} catch (ExceptionViajesCompartidos e) {
			fail(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNuevoAutoINCorrecto1() {	
		//test q envia 2 veces un mismo auto (pantente repetida)
		//el sistema debe responder con una exceptcion propia		
		boolean bandera= false;
		JSONObject json= new JSONObject();
		json.put("conductor", 2);
		JSONObject vehiculo= crearVehiculo();
		json.put("vehiculo", vehiculo);
		
		try {
			this.daoviajes.NuevoVehiculo(json);
			this.daoviajes.NuevoVehiculo(json);	//esta vez tiene q excplotar por patente duplicada
			fail("no tiro la exceptio de viajes compartidos");
		} catch (ExceptionViajesCompartidos e) {
			bandera=true;
		}
		assertTrue(bandera);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNuevoAutoINCorrecto2() {	
		//test q envia un usuarios q no existe
		boolean bandera=false;
		JSONObject json= crearVehiculo();
		json.remove("conductor");
		json.put("conductor", -1);
		
		try {
			this.daoviajes.NuevoVehiculo(json);
			fail("no tiro la exceptio de viajes compartidos");
		} catch (ExceptionViajesCompartidos e) {
			bandera=true;
		}
		assertTrue(bandera);
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject crearVehiculo(){
		JSONObject json= new JSONObject();
		json.put("conductor", 2);
		JSONObject vehiculo= new JSONObject();
		vehiculo.put("patente", "abd123");
		vehiculo.put("anio", 1992);
		vehiculo.put("modelo", "viejo");
		vehiculo.put("marca", "mondeo");
		json.put("vehiculo", vehiculo);
		return json;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject crearViaje(){
		
		/*
		{ "LOCALIDADES": {"ORIGEN":"ID_LOCALIDAD","INTERMEDIO":ID_LOCALIDAD,.....,"DESTINO":ID_LOCALIDAD},
			 "VEHICULO": ID_VEHICULO,
			 "VIAJE": {FECHA_inicio, HS_SALIDA, CANTIDAD_ASIENTOS, NOMBRE_AMIGABLE, COSTO_VIAJE},
			 "VUELTA": {FECHA_SALIDA,HS_SALIDA,CANTIDAD_ASIENTOS, NOMBRE_AMIGABLE},
			 "CLIENTE":ID_CLIENTE
			 }
		*/
		
		JSONObject json2 = new JSONObject();
		json2.put("vehiculo", "abd123");
		json2.put("cliente", 2);
		json2.put("fecha_inicio",new Date((new java.util.Date()).getTime()) );
		json2.put("cantidad_asientos", 2);
		json2.put("nombre_amigable", "prueba viaje");
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
