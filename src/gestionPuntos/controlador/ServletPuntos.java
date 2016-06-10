package gestionPuntos.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gestionPuntos.modelo.MovimientoPuntos;
import gestionPuntos.modelo.TipoMovimientoPuntos;
import otros.ExceptionViajesCompartidos;

public class ServletPuntos extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	protected DAOPuntos daopuntos;
	
	public void init() throws ServletException {
		daopuntos= new DAOPuntos();
	}
	
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");

		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");
		
		respuesta.put("entity", entity);
		respuesta.put("action", action);
		
		
		writer.println (respuesta);
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject respuesta = new JSONObject();
		PrintWriter writer = response.getWriter();
		String action = request.getParameter("action");
		String entity = request.getParameter("entity");
		
		if (entity != null && entity.equals ("tipoMovimientoPuntos")) {
			if (action != null && action.equals("todos")) {
				respuesta = this.tipoMovimientoPuntos (request);
			}
		}else if(entity != null && entity.equals ("movimientoPuntos")){
			if (action != null && action.equals("porId")) {
				respuesta = this.movimientoPuntos (request);
			}
		}else {
			respuesta = new JSONObject();
			respuesta.put ("result", false);
			respuesta.put ("msg", "No implementado");
		}
		respuesta.put("entity", entity);
		respuesta.put("action", action);

		System.out.println (respuesta);
		writer.println (respuesta);
	}

	private JSONObject tipoMovimientoPuntos(HttpServletRequest request) {
		
		ArrayList<String> err = new ArrayList<String>();
		JSONObject salida = new JSONObject();
		String descripcion=null;
		try {
			descripcion = request.getParameter("descripcion");
		} catch (Exception e) {
			err.add("id_comision no es valido");
		}
		if(err.size() > 0) {
			salida.put("result", false);
			salida.put("msg", err);
			return salida;
		}
		
		TipoMovimientoPuntos tmp=this.daopuntos.getTipoMovimientoPuntos(descripcion);
		
		salida.put("result", true);
		salida.put("TipoMovimientoPuntos", tmp.toJSON());
		return salida;
	}

	private JSONObject movimientoPuntos(HttpServletRequest request) {
		
		ArrayList<String> err = new ArrayList<String>();
		JSONObject salida = new JSONObject();
		Integer id_cliente=null;
		try {
			id_cliente = Integer.parseInt( request.getParameter("descripcion") );
		} catch (Exception e) {
			err.add("id_comision no es valido");
		}
		if(err.size() > 0) {
			salida.put("result", false);
			salida.put("msg", err);
			return salida;
		}
		
		List<MovimientoPuntos> movimientos =this.daopuntos.getMovimientoPuntos(id_cliente);
		JSONArray array = new JSONArray();
		for(MovimientoPuntos mp: movimientos){
			array.add(mp.toJSON());
		}
		
		salida.put("result", true);
		salida.put("MovimientosPuntos", array);
		return salida;
	}
}
