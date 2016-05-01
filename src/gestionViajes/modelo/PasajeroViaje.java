package gestionViajes.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import gestionComisiones.modelo.ComisionCobrada;
import gestionPuntos.modelo.Calificacion;
import gestionUsuarios.modelo.Cliente;
import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="pasajeroviaje")
public class PasajeroViaje implements JSONable {

	@Id
	protected Integer id;
	
	protected float kilometros;

	protected Calificacion calificacion;
	protected Cliente cliente;
	protected ComisionCobrada comision;
	protected Localidad localidad_bajada;
	protected Localidad localidad_subida;
	protected Viaje viaje;
	
	public PasajeroViaje(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getKilometros() {
		return kilometros;
	}

	public void setKilometros(float kilometros) {
		this.kilometros = kilometros;
	}

	public Calificacion getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Calificacion calificacion) {
		this.calificacion = calificacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ComisionCobrada getComision() {
		return comision;
	}

	public void setComision(ComisionCobrada comision) {
		this.comision = comision;
	}

	public Localidad getLocalidad_bajada() {
		return localidad_bajada;
	}

	public void setLocalidad_bajada(Localidad localidad_bajada) {
		this.localidad_bajada = localidad_bajada;
	}

	public Localidad getLocalidad_subida() {
		return localidad_subida;
	}

	public void setLocalidad_subida(Localidad localidad_subida) {
		this.localidad_subida = localidad_subida;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	@Override
	public void SetJSONObject(JSONObject json) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
