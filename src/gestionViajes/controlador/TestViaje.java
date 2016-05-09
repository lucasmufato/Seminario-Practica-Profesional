package gestionViajes.controlador;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		
		//PODRIA IR CON CODIGO QUE LIMPIE LA BD PARA Q PARA TODOS LOS TEST QUEDE DE LA MISMA FORMA
		//ASI SI CORRES 2 VECES UN TEST Q CREA UN AUTO, NO VA A EXPLOTAR POR QUE EL AUTO YA EXISTE
		//QUE EL AUTO EXPLOTE POR Q YA HAY OTRO SERIA OTRO TEST, QUE HAGA LOS 2 AUTOS Y LOS QUIERA GUARDAR
	}

	@After
	public void tearDown() throws Exception {
		//este metodo se ejecuta despues de cada parte del test
		
		//O SE PUEDE PONER ACA EL CODIGO PARA VACIAR LA BD
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

	@Test
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
	public void testNuevoViajeCorrecto() {
		//test q envie un json correcto y tendria q andar bien
		JSONObject json= new JSONObject();
		this.daoviajes.nuevoViaje(json);
	}
	
	@Test
	public void testNuevoViajeDatosIncorrecto() {
		//teste que envia un json incorrecto y tendria q mostrar un error de alguna forma
		JSONObject json= new JSONObject();
		this.daoviajes.nuevoViaje(json);
	}
	
	@Test
	public void testgetConductorViajeCorrecto() {
		//test q envie un viaje que existe
		
	}
	
	@Test
	public void testgetConductorViajeIncorrecto() {
		//test q envie un viaje que no existe
		JSONObject json= new JSONObject();
		this.daoviajes.nuevoViaje(json);
	}
	
	@Test
	public void testNuevoAutoCorrecto() {	//test q envie un usuario q existe, y vehiculo con datos bien.
		
		//lleno el json con datos q son correctos
		JSONObject json= new JSONObject();
		json.put("conductor", 2);
		JSONObject vehiculo= new JSONObject();
		vehiculo.put("patente", "abd123");
		vehiculo.put("anio", 1992);
		vehiculo.put("modelo", "viejo");
		vehiculo.put("marca", "mondeo");
		json.put("vehiculo", vehiculo);
		
		try {
			//pruebo que el metodo devuelva true
			assertTrue(this.daoviajes.NuevoVehiculo(json) );
			
		} catch (ExceptionViajesCompartidos e) {
			System.out.println( e.getMessage() );
			fail(e.getMessage());
		}
	}
	
}
