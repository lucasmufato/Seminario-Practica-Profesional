package gestionViajes.modelo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@Column(nullable=true)
	protected float kilometros;

	@Column(nullable=false)
	protected Character estado;		//falta hacer el enum	
	
	@JoinColumn(name="id_calificacion")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Calificacion calificacion;
	
	@JoinColumn(name="id_cliente")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Cliente cliente;
	
	@JoinColumn(name="id_comision")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected ComisionCobrada comision;
	
	@JoinColumn(name="id_localidad_bajada")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected LocalidadViaje id_localidad_bajada;
	
	@JoinColumn(name="id_localidad_subida")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected LocalidadViaje id_localidad_subida;
	
	@JoinColumn(name="id_viaje")
	@ManyToOne(cascade=CascadeType.PERSIST)
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

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	public LocalidadViaje getLocalidad_bajada() {
		return localidad_bajada;
	}

	public void setLocalidad_bajada(LocalidadViaje localidad_bajada) {
		this.localidad_bajada = localidad_bajada;
	}

	public LocalidadViaje getLocalidad_subida() {
		return localidad_subida;
	}

	public void setLocalidad_subida(LocalidadViaje localidad_subida) {
		this.localidad_subida = localidad_subida;
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
