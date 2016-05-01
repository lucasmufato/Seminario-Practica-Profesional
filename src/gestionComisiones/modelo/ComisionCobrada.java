package gestionComisiones.modelo;

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
@Table(name="comision_cobrada")
public class ComisionCobrada implements JSONable {
	
	@Id
	protected Integer id;
	protected float monto;
	
	protected Comision comision;
	protected MovimientoSaldo movimiento_saldo;
	protected PasajeroViaje pasajero_viaje;
	
	public ComisionCobrada(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public Comision getComision() {
		return comision;
	}

	public void setComision(Comision comision) {
		this.comision = comision;
	}

	public MovimientoSaldo getMovimiento_saldo() {
		return movimiento_saldo;
	}

	public void setMovimiento_saldo(MovimientoSaldo movimiento_saldo) {
		this.movimiento_saldo = movimiento_saldo;
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
