package gestionViajes.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="localidad")
public class Localidad implements JSONable {

	@Id
	protected Integer id;
	@Column(nullable=false)
	protected Double latitud;
	@Column(nullable=false)
	protected Double longuitud;
	
	public Localidad(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLonguitud() {
		return longuitud;
	}

	public void setLonguitud(Double longuitud) {
		this.longuitud = longuitud;
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
