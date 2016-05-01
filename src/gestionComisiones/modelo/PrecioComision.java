package gestionComisiones.modelo;

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
@Table(name="comision")
public class PrecioComision implements JSONable {

	@Id
	protected Integer id;
	protected Date fecha;
	protected float Monto;
	
	protected Comision comision;
	
	public PrecioComision(){
		
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
