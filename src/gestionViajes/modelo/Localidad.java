package gestionViajes.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;

@Entity
@Table(name="localidad")
@NamedQueries({
	@NamedQuery(name="Localidad.autocompletar",query="SELECT l FROM Localidad l WHERE l.nombre LIKE :busqueda AND 'l.clasificacion'='P'")
})
public class Localidad implements JSONable {

	@Id
	protected Integer id_localidad;
	@Column(nullable=false)
	protected Double lat;
	@Column(nullable=false)
	protected Double lng;
	@Column(nullable=false,length=200)
	protected String nombre;
	@Column(nullable=true,length=200)
	protected String nombre_ascii;
	@Column(nullable=true)
	protected Character clasificacion;
	
	public Localidad(){
		
	}
	
	public Integer getId() {
		return id_localidad;
	}

	public void setId(Integer id) {
		this.id_localidad = id;
	}

	public Double getLatitud() {
		return lat;
	}

	public void setLatitud(Double latitud) {
		this.lat = latitud;
	}

	public Double getLongitud() {
		return lng;
	}

	public void setLongitud(Double longitud) {
		this.lng = longitud;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreAscii() {
		return nombre_ascii;
	}

	public void setNombreAscii(String nombre_ascii) {
		this.nombre_ascii = nombre_ascii;
	}

	public Character getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(Character clasificacion) {
		this.clasificacion = clasificacion;
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
