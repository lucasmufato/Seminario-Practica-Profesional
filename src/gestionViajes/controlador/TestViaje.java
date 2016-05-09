package gestionViajes.controlador;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;

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
		
		//antes de cada parte del test creo el DAO
		//this.daoviajes= new DAOViajes();
		//TARDA MUCHO EN HACER VARIOS TEST, ASI Q LO CREO AL INICIAR EL TEST Y LISTO
	}

	@After
	public void tearDown() throws Exception {
		//este metodo se ejecuta despues de cada parte del test
		
		//despues de cada parte del test cierro el dao y borro el apuntador (asi el garbage collector lo borra)
		/*
		if(this.daoviajes!=null){
			this.daoviajes.cerrarConexiones();
			this.daoviajes=null;
		}
		*/
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
		JSONObject json= new JSONObject();
		this.daoviajes.nuevoViaje(json);
	}
	
	@Test
	public void testgetConductorViajeIncorrecto() {
		//test q envie un viaje que no existe
		JSONObject json= new JSONObject();
		this.daoviajes.nuevoViaje(json);
	}
	
}
