package gestionPuntos.modelo;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import gestionUsuarios.modelo.Sponsor;
import otros.JSONable;


@NamedQueries({
	
})
@Entity
@Table(name="beneficio")
public class Beneficio implements JSONable {
	
	@Id
	protected Integer id_beneficio;
	
	protected Date fecha_caduca;
	
	protected String producto;
	
	protected Integer puntos_necesarios;
	
	
	protected Sponsor sponsor;
	
	protected List<Cupon> cupones;
	
	public Beneficio(){
		
	}
	
	public Integer getId_beneficio() {
		return id_beneficio;
	}

	public void setId_beneficio(Integer id_beneficio) {
		this.id_beneficio = id_beneficio;
	}

	public Date getFecha_caduca() {
		return fecha_caduca;
	}

	public void setFecha_caduca(Date fecha_caduca) {
		this.fecha_caduca = fecha_caduca;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Integer getPuntos_necesarios() {
		return puntos_necesarios;
	}

	public void setPuntos_necesarios(Integer puntos_necesarios) {
		this.puntos_necesarios = puntos_necesarios;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public List<Cupon> getCupones() {
		return cupones;
	}

	public void setCupones(List<Cupon> cupones) {
		this.cupones = cupones;
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
