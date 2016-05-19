package gestionReportes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import otros.*;

public class ControladorReportes extends HttpServlet {

	public void init() throws ServletException
	{

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("En doGet de Reportes");

		PrintWriter writer = response.getWriter();

		response.setContentType("application/json");
		if (!AccessManager.EstaLogueado(request)){
			this.printDeniedRedirect(writer,"/acceso_denegado.html");
		}else{
			this.printAccept(writer, request);
		}
	}
	
	private void printAccept(PrintWriter writer, HttpServletRequest request) {
		JSONObject resultado = new JSONObject();
		
		resultado.put("result", true);

		writer.println(resultado);
	}


	private void printDeniedRedirect (PrintWriter writer, String url) {
		JSONObject resultado;

		resultado = new JSONObject();
		resultado.put("result", false);
		resultado.put("redirect", url);

		writer.println(resultado);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("En doPost de Reportes!");
		JSONObject out = null;

		String accion = request.getParameter("action");

		
		if (accion.equals("reporte_viajes")){
			out = reporteViajes(request,response);
		}
		
		if (out == null) {
			out = new JSONObject();
			out.put ("result", false);
		}
		
		out.put("action", accion);
		System.out.println("Lo que mando al js: "+out);
		PrintWriter writer = response.getWriter();
		writer.println (out);
	}

	private JSONObject reporteViajes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		
		ServletContext context = this.getServletConfig().getServletContext();
		String path = context.getRealPath("/reportes/reports/vc_reporte_viajes.jrxml");

		//compilo reporte
		if (!this.compilarReporte(path)){
			respuesta.put("msg", "Error en compilacion de reporte de viajes");
			respuesta.put("result", false);
			return respuesta;
		}
		//creo archivo de reporte
		String reportFileName = context.getRealPath("/reportes/reports/vc_reporte_viajes.jasper");
		if (!this.existeReporteCompilado(reportFileName)){
			respuesta.put("msg", "El reporte no se encuentra compilado");
			respuesta.put("result", false);
			return respuesta;
		}
		// tomo parametros de la web y relleno reporte con datos
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		String conductor = request.getParameter("data[conductor]");
		Integer id_desde = parsearInteger(request.getParameter("data[id_desde]"));
		Integer id_hasta = parsearInteger(request.getParameter("data[id_hasta]"));
		java.sql.Date fecha_desde = parsearFecha(request.getParameter("data[fecha_desde]"));
		java.sql.Date fecha_hasta = parsearFecha(request.getParameter("data[fecha_hasta]"));
		Double km_desde = parsearDouble(request.getParameter("data[km_desde]"));
		Double km_hasta = parsearDouble(request.getParameter("data[km_hasta]"));
		Float precio_desde = parsearFloat(request.getParameter("data[precio_desde]"));
		Float precio_hasta = parsearFloat(request.getParameter("data[precio_hasta]"));
		Integer pasajeros_desde = parsearInteger(request.getParameter("data[pasajeros_desde]"));
		Integer pasajeros_hasta = parsearInteger(request.getParameter("data[pasajeros_hasta]"));

		parameters.put("id_viaje_desde", id_desde);
		parameters.put("id_viaje_hasta", id_hasta);
		parameters.put("fecha_desde", fecha_desde);
		parameters.put("fecha_hasta", fecha_hasta);
		parameters.put("conductor", conductor);
		parameters.put("precio_desde", precio_desde);
		parameters.put("precio_hasta", precio_hasta);
		parameters.put("km_desde", km_desde);
		parameters.put("km_hasta", km_hasta);
		parameters.put("pasajeros_desde", pasajeros_desde);
		parameters.put("precio_hasta", pasajeros_hasta);
		
		JasperPrint jasperPrint = this.fillReporte(reportFileName, parameters);
		if (jasperPrint == null){
			respuesta.put("msg", "No se pudo rellenar el reporte con datos");
			respuesta.put("result", false);
			return respuesta;
		}

		String pathReal = context.getRealPath("/");
		String pathToPdf = FileManager.uploadPdf(pathReal, jasperPrint);
		if (pathToPdf == "" || pathToPdf == null){
			respuesta.put("msg", "Error al guardar pdf generado");
			respuesta.put("result", false);
			return respuesta;
		}
		respuesta.put("msg", "El reporte ha sido generado correctamente");
		respuesta.put("relocate", pathToPdf);
		respuesta.put("result", true);
		
		return respuesta;
	}

	private Integer parsearInteger(String valor) {
		try{
			return Integer.parseInt(valor);
		}catch(NumberFormatException e){
			return null;
		}
	}
	private Float parsearFloat(String valor) {
		try{
			return Float.parseFloat(valor);
		}catch(NumberFormatException e){
			return null;
		}
	}
	private Double parsearDouble(String valor) {
		try{
			return Double.parseDouble(valor);
		}catch(NumberFormatException e){
			return null;
		}
	}
	private java.sql.Date parsearFecha(String valor) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date;
		try {
			date = formato.parse(valor);
		} catch (ParseException e) {
			return null;
		}
		return new java.sql.Date(date.getTime());  
	}
	private boolean existeReporteCompilado(String reportFileName) {
		File reportFile = new File(reportFileName);
		return reportFile.exists();
	}

	private JasperPrint fillReporte(String reportFileName, Map<String, Object> parameters) {

		JasperPrint jasperPrint;
		Connection conexion = this.getConexion();
		try {
			jasperPrint = JasperFillManager.fillReport(
				reportFileName, 
				parameters, 
				conexion
			);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.cerrarConexion(conexion);
			return null;
		}
		this.cerrarConexion(conexion);
		return jasperPrint;
	}

	private boolean compilarReporte(String path) {
		try {
			String string = JasperCompileManager.compileReportToFile(path);
		} catch (JRException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void destroy()
	{
	}
	
	private Connection getConexion(){
		Connection connection = null;
		
		try {
			// CARGAR CLASES DEL DRIVER EN MEMORIA
			Class.forName("com.mysql.jdbc.Driver");

			// ESTABLECE CONEXION
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminario", "root", "root");
			
		} catch(SQLException se){
			//Manejo de errores para JDBC
			System.err.println("En Excepcion de JDBC");
			se.printStackTrace();
		} catch(Exception e){
		    //Manejo de errores para Class.forName
			System.err.println("En Excepcion de Class.forName");
		    e.printStackTrace();
		} finally {
			return connection;
		}
	}
	private void cerrarConexion(Connection connection){
		if (connection != null){ 
			try {connection.close();} 
			catch (SQLException e) {e.printStackTrace();}
		}
	}
}
