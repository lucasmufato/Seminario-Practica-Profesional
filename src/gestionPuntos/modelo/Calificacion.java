package gestionPuntos.modelo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="ID_CALIFICACION")
	protected Integer id_calificacion;
	@Column(name="CALIFICACION_PARA_CONDUCTOR",nullable=true)
	protected Integer calificacion_para_conductor;
	@Column(name="CALIFICACION_PARA_PASAJERO",nullable=true)
	protected Integer calificacion_para_pasajero;
	@Column(name="PARTICIPO",nullable=false)
	protected EstadoClasificacion participo;
	
	@JoinColumn(name="ID_MOVIMIENTO_PUNTOS")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected MovimientoPuntos movimiento_puntos;
	@JoinColumn(name="ID_PASAJERO_VIAJE")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected PasajeroViaje pasajero_viaje;
	
	public Calificacion(){
		
	}
	
	
	public Integer getId_calificacion() {
		return id_calificacion;
	}


	public void setId_calificacion(Integer id_calificacion) {
		this.id_calificacion = id_calificacion;
	}


	public Integer getCalificacion_para_conductor() {
		return calificacion_para_conductor;
	}


	public void setCalificacion_para_conductor(Integer calificacion_para_conductor) {
		this.calificacion_para_conductor = calificacion_para_conductor;
	}


	public Integer getCalificacion_para_pasajero() {
		return calificacion_para_pasajero;
	}


	public void setCalificacion_para_pasajero(Integer calificacion_para_pasajero) {
		this.calificacion_para_pasajero = calificacion_para_pasajero;
	}


	public Integer getId() {
		return id_calificacion;
	}


	public void setId(Integer id) {
		this.id_calificacion = id;
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
