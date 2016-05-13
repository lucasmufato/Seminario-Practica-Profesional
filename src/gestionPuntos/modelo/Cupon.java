package gestionPuntos.modelo;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;
@NamedQueries({
	
})
@Entity
@Table(name="cupon")
public class Cupon implements JSONable {
	
	@Id
	protected Integer id_cupon;
	protected EstadoCupon estado;
	protected Date fecha_caduca;
	
	protected Beneficio beneficio;
	
	protected MovimientoPuntos movimiento_puntos;
	
	public Cupon(){
		
	}
	
	public Integer getId() {
		return id_cupon;
	}

	public void setId(Integer id) {
		this.id_cupon = id;
	}

	public EstadoCupon getEstado() {
		return estado;
	}

	public void setEstado(EstadoCupon estado) {
		this.estado = estado;
	}

	public Date getFecha_caduca() {
		return fecha_caduca;
	}

	public void setFecha_caduca(Date fecha_caduca) {
		this.fecha_caduca = fecha_caduca;
	}

	public Beneficio getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(Beneficio beneficio) {
		this.beneficio = beneficio;
	}

	public MovimientoPuntos getMovimiento_puntos() {
		return movimiento_puntos;
	}

	public void setMovimiento_puntos(MovimientoPuntos movimiento_puntos) {
		this.movimiento_puntos = movimiento_puntos;
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
