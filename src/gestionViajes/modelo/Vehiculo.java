package gestionViajes.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="vehiculo")
public class Vehiculo implements JSONable {

	@Id
	protected Integer id;
	protected Integer anio;
	protected String marca;
	protected String patente;
	protected Character verificado;
	
	protected List<Maneja> conductores;
	
	public Vehiculo(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public Character getVerificado() {
		return verificado;
	}

	public void setVerificado(Character verificado) {
		this.verificado = verificado;
	}

	public List<Maneja> getConductores() {
		return conductores;
	}

	public void setConductores(List<Maneja> conductores) {
		this.conductores = conductores;
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
