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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import gestionUsuarios.modelo.Cliente;
import javax.persistence.NamedQuery;

import otros.ExceptionViajesCompartidos;
import otros.JSONable;

@NamedQueries({
	@NamedQuery(name="Viaje.SearchById",query="SELECT v FROM Viaje v WHERE v.id_viaje= :id"),//agregada por fede
	@NamedQuery(name="Viaje.todos",query="SELECT v FROM Viaje v"),
})

@Entity
@Table(name="viaje")
public class Viaje implements JSONable {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGeneratorViaje")
	@SequenceGenerator(allocationSize=1, schema="seminario",  name="MySequenceGeneratorViaje", sequenceName = "sequence")
	@Column(name="ID_VIAJE")
	protected Integer id_viaje;
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

	@JoinColumns ({
		@JoinColumn(name="id_cliente", referencedColumnName="id_cliente"),
		@JoinColumn(name="id_vehiculo", referencedColumnName="id_vehiculo"),
		@JoinColumn(name="fecha_inicio_maneja", referencedColumnName="fecha_inicio"),
	})
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Maneja conductor_vehiculo;
	@OneToMany(mappedBy="viaje", cascade=CascadeType.PERSIST)
	protected List<LocalidadViaje> localidades= new ArrayList<LocalidadViaje>();
	@OneToMany(mappedBy="viaje", cascade=CascadeType.PERSIST)
	protected List<PasajeroViaje> pasajeros= new ArrayList<PasajeroViaje>();
	@JoinColumn(name="viaje_complementario", referencedColumnName = "id_viaje", nullable = true)
	@OneToOne(cascade=CascadeType.PERSIST)
	protected Viaje viaje_complementario;
	
	
	public boolean crearRecorrido(List<Localidad> arreglo_de_localidades){
		for(Localidad l: arreglo_de_localidades){
			this.localidades.add(new LocalidadViaje(this,l) );
		}
		return false;
	}
	
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
	
	public Integer getId_viaje() {
		return id_viaje;
	}

	public void setId_viaje(Integer id) {
		this.id_viaje = id;
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

	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
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

	public Viaje getViaje_complementario() {
		return viaje_complementario;
	}

	public void setViaje_complementario(Viaje viaje_complementario) {
		this.viaje_complementario = viaje_complementario;
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

	public void crearTuVuelta(JSONObject vuelta) throws ExceptionViajesCompartidos {
		Viaje mi_vuelta = new Viaje();
		//le pongo al viaje los datos que llegan desde el JSON
		Integer a=(Integer) vuelta.get("cantidad_asientos");
		if(a!=null){
			mi_vuelta.setAsientos_disponibles(a);
		}else{
			mi_vuelta.setAsientos_disponibles(this.asientos_disponibles);
		}
		String n= (String) vuelta.get("nombre_amigable");
		if(n!=null){
			mi_vuelta.setNombre_amigable(n);
		}else{
			mi_vuelta.setNombre_amigable(this.nombre_amigable+"_vuelta");
		}
		Date f= (Date) vuelta.get("fecha_inicio");
		if(f!=null){
			mi_vuelta.setFecha_inicio(f);
		}else{
			throw new ExceptionViajesCompartidos("ERROR: FALTA LA FECHA DE INICIO DEL VIAJE DE VUELTA");
		}
		mi_vuelta.setFecha_alta(new Date((new java.util.Date()).getTime()));
		// le pongo al viaje los datos propios que se repiten
		mi_vuelta.setConductor_vehiculo(this.getConductor_vehiculo());
		mi_vuelta.setEstado('A');
		mi_vuelta.setFecha_cancelacion(null);
		mi_vuelta.setFecha_finalizacion(null);
		//hago la relacion con sigo mismo
		this.setViaje_complementario(mi_vuelta);
		mi_vuelta.setViaje_complementario(this);
		
		//hago el recorrido invertido, para eso creo la nueva lista, y recorro la viaje de atras para adelante
		//agregando las localidades
		ArrayList<Localidad> nuevo_recorrido= new ArrayList<Localidad>();
		int index=this.localidades.size();
		index--;
		for(;index>=0;index--){
			nuevo_recorrido.add(this.localidades.get(index).getLocalidad());
		}
		mi_vuelta.crearRecorrido(nuevo_recorrido);
	}

}
