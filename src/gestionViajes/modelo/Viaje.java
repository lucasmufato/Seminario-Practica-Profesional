package gestionViajes.modelo;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import gestionUsuarios.modelo.Cliente;
import javax.persistence.NamedQuery;
import otros.JSONable;

@NamedQueries({
@NamedQuery(name="Viaje.SearchById",query="SELECT v FROM Viaje v WHERE v.id_viaje = :id"),//agregada por fede
    
})

@Entity
@Table(name="viaje")
public class Viaje implements JSONable {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGeneratorViaje")
	@SequenceGenerator(allocationSize=1, schema="seminario",  name="MySequenceGeneratorViaje", sequenceName = "sequence")
	protected Integer id;
	@Column(nullable=false,length=30)
	protected String nombre_amigable;
	@Column(nullable=false)
	protected Integer asientos_disponibles;
	@Column(nullable=false)
	protected Character estado; 		//falta hacer el enum
	@Column(nullable=false)
	protected Date fecha_inicio;
	@Column(nullable=false)
	protected Date fecha_alta;
	@Column(nullable=true)
	protected Date fecha_finalizacion;
	@Column(nullable=true)
	protected Date fecha_cancelacion;

	@JoinColumn(name="id_maneja")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Maneja conductor_vehiculo;
	@OneToMany(mappedBy="viaje", cascade=CascadeType.PERSIST)
	protected List<LocalidadViaje> localidades;
	@OneToMany(mappedBy="viaje", cascade=CascadeType.PERSIST)
	protected List<PasajeroViaje> pasajeros;
	
	public void aniadir_localidad( Localidad loc1, Localidad loc2){
		
	}
	
	public void aniadir_pasajeroViaje (Integer id_cliente, Localidad subida, Localidad bajada){
		//el pasajeroViaje se crea con estado POSTULADO
	}
	
	public List<Localidad> devolver_recorrido_desde_hasta(Localidad desde, Localidad hasta){
		return null;
	}
	
	public PasajeroViaje recuperar_pasajeroViaje_por_cliente(Cliente cliente){
		return null;
	}
	
	public Viaje(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public List<PasajeroViaje> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<PasajeroViaje> pasajeros) {
		this.pasajeros = pasajeros;
	}

	public String getNombre_amigable() {
		return nombre_amigable;
	}

	public void setNombre_amigable(String nombre_amigable) {
		this.nombre_amigable = nombre_amigable;
	}

	public Integer getAsientos_disponibles() {
		return asientos_disponibles;
	}

	public void setAsientos_disponibles(Integer asientos_disponibles) {
		this.asientos_disponibles = asientos_disponibles;
	}

	public Date getFecha_alta() {
		return fecha_alta;
	}

	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}

	public Date getFecha_finalizacion() {
		return fecha_finalizacion;
	}

	public void setFecha_finalizacion(Date fecha_finalizacion) {
		this.fecha_finalizacion = fecha_finalizacion;
	}

	public Date getFecha_cancelacion() {
		return fecha_cancelacion;
	}

	public void setFecha_cancelacion(Date fecha_cancelacion) {
		this.fecha_cancelacion = fecha_cancelacion;
	}

	public Maneja getConductor_vehiculo() {
		return conductor_vehiculo;
	}

	public void setConductor_vehiculo(Maneja conductor_vehiculo) {
		this.conductor_vehiculo = conductor_vehiculo;
	}

	public List<LocalidadViaje> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<LocalidadViaje> localidades) {
		this.localidades = localidades;
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
