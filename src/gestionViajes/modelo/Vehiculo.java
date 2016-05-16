package gestionViajes.modelo;

import java.sql.Timestamp;
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
	protected Timestamp fecha_verificacion;
	@Column(nullable=true)
	protected String color;
	@Column(nullable=false)
	protected Integer cantidad_asientos;
	@Column(nullable=true)
	protected Character aire_acondicionado;
	@Column(nullable=false)
	protected Character seguro;
	@Column(nullable=true)
	protected String foto;
	
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

	public Timestamp getFecha_verificacion() {
		return fecha_verificacion;
	}

	public void setFecha_verificacion(Timestamp fecha_verificacion) {
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

	public Integer getId_vehiculo() {
		return id_vehiculo;
	}

	public void setId_vehiculo(Integer id_vehiculo) {
		this.id_vehiculo = id_vehiculo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getCantidad_asientos() {
		return cantidad_asientos;
	}

	public void setCantidad_asientos(Integer cantidad_asientos) {
		this.cantidad_asientos = cantidad_asientos;
	}

	public Character getAire_acondicionado() {
		return aire_acondicionado;
	}

	public void setAire_acondicionado(Character aire_acondicionado) {
		this.aire_acondicionado = aire_acondicionado;
	}

	public Character getSeguro() {
		return seguro;
	}

	public void setSeguro(Character seguro) {
		this.seguro = seguro;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Override
	public void SetJSONObject(JSONObject json) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", this.getId());
		json.put("marca", this.getMarca());
		json.put("modelo", this.getModelo());
		json.put("anio", this.getAnio());
		json.put("patente", this.getPatente());
		json.put("aire", this.getAire_acondicionado());
		json.put("seguro", this.getSeguro());
		json.put("verificado", this.getVerificado().toString());
		json.put("foto", this.getFoto());
		return json;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
