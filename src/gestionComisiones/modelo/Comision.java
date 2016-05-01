package gestionComisiones.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="comision")
public class Comision implements JSONable {

	@Id
	protected Integer id;
	protected float limite_superior;
	protected float limite_inferior;
	
	protected PrecioComision precio_comision;
	protected ComisionCobrada comision_cobrada;
	
	public Comision(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getLimite_superior() {
		return limite_superior;
	}

	public void setLimite_superior(float limite_superior) {
		this.limite_superior = limite_superior;
	}

	public float getLimite_inferior() {
		return limite_inferior;
	}

	public void setLimite_inferior(float limite_inferior) {
		this.limite_inferior = limite_inferior;
	}

	public PrecioComision getPrecio_comision() {
		return precio_comision;
	}

	public void setPrecio_comision(PrecioComision precio_comision) {
		this.precio_comision = precio_comision;
	}

	public ComisionCobrada getComision_cobrada() {
		return comision_cobrada;
	}

	public void setComision_cobrada(ComisionCobrada comision_cobrada) {
		this.comision_cobrada = comision_cobrada;
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
