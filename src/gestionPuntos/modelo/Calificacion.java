package gestionPuntos.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import gestionViajes.modelo.PasajeroViaje;
import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="calificacion")
public class Calificacion implements JSONable {

	@Id
	protected Integer id_calificacion;
	protected Integer calificacion_otorgada;
	protected Integer calificacion_recibida;
	protected EstadoClasificacion participo;
	
	protected MovimientoPuntos movimiento_puntos;
	
	protected PasajeroViaje pasajero_viaje;
	
	public Calificacion(){
		
	}
	
	
	public Integer getId() {
		return id_calificacion;
	}


	public void setId(Integer id) {
		this.id_calificacion = id;
	}


	public Integer getCalificacion_otorgada() {
		return calificacion_otorgada;
	}


	public void setCalificacion_otorgada(Integer calificacion_otorgada) {
		this.calificacion_otorgada = calificacion_otorgada;
	}


	public Integer getCalificacion_recibida() {
		return calificacion_recibida;
	}


	public void setCalificacion_recibida(Integer calificacion_recibida) {
		this.calificacion_recibida = calificacion_recibida;
	}


	public EstadoClasificacion getParticipo() {
		return participo;
	}


	public void setParticipo(EstadoClasificacion participo) {
		this.participo = participo;
	}


	public MovimientoPuntos getMovimiento_puntos() {
		return movimiento_puntos;
	}


	public void setMovimiento_puntos(MovimientoPuntos movimiento_puntos) {
		this.movimiento_puntos = movimiento_puntos;
	}


	public PasajeroViaje getPasajero_viaje() {
		return pasajero_viaje;
	}


	public void setPasajero_viaje(PasajeroViaje pasajero_viaje) {
		this.pasajero_viaje = pasajero_viaje;
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
