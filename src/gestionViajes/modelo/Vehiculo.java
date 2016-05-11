package gestionViajes.modelo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;

@NamedQueries({
@NamedQuery(name="Vehiculo.SearchById",query="SELECT veh FROM Vehiculo veh WHERE ( (veh.id_vehiculo= :idveh) )"),//agregada por fede	
@NamedQuery(name="Vehiculo.PorPatente",query="SELECT veh FROM Vehiculo veh WHERE ( (veh.patente= :patente) )"),//agregada por fede	
@NamedQuery(name="Vehiculo.buscarPorClaveCandidata",query="SELECT veh FROM Vehiculo veh WHERE ( (veh.patente = :clave_candidata) )")//agregada por lucas
})
@Entity
@Table(name="vehiculo")
public class Vehiculo implements JSONable {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGeneratorVehiculo")
	@SequenceGenerator(allocationSize=1, schema="seminario",  name="MySequenceGeneratorVehiculo", sequenceName = "sequence")
	protected Integer id_vehiculo;
	@Column(name="anio",nullable=false,length=10)
	protected Integer anio;
	@Column(nullable=false,length=30)
	protected String marca;
	@Column(nullable=false,length=30)
	protected String modelo;
	@Column(nullable=false,length=15)
	protected String patente;
	@Column(nullable=false,length=1)
	protected Character verificado;
	@Column(nullable=false)
	protected Character estado;	//falta hacer el enum
	@Column(nullable=true)
	protected Date fecha_verificacion;
	
	@OneToMany(mappedBy="vehiculo", cascade=CascadeType.PERSIST)
	protected List<Maneja> conductores = new ArrayList<Maneja>();
	
	public Vehiculo(){
		
	}
	
	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Date getFecha_verificacion() {
		return fecha_verificacion;
	}

	public void setFecha_verificacion(Date fecha_verificacion) {
		this.fecha_verificacion = fecha_verificacion;
	}
	
	public Integer getId() {
		return id_vehiculo;
	}

	public void setId(Integer id) {
		this.id_vehiculo = id;
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
