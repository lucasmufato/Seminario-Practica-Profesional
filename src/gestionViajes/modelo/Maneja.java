package gestionViajes.modelo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import gestionUsuarios.modelo.Cliente;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({	
//@NamedQuery(name="Maneja.SearchById",query="SELECT m FROM Maneja m WHERE ( (m.id_conductor= :idc) AND (m.id_vehiculo= :idveh) )"),//agregada por fede	
	@NamedQuery(name="Maneja.SearchByIdCompuesto", query="SELECT m FROM Maneja m WHERE ( (m.cliente= :id_1) AND (m.vehiculo= :id_2) )")
})
@Entity
@Table(name="maneja")
@IdClass(ManejaID.class)
public class Maneja {
	
	@Id
	@JoinColumn(name="id_cliente")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Cliente cliente;
	@Id
	@JoinColumn(name="id_vehiculo")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Vehiculo vehiculo;
	@Id
	@Column(name="FECHA_INICIO",nullable=false)
	protected Date fecha_inicio;
	@Column(nullable=true)
	protected Date fecha_fin;
	
	@OneToMany(mappedBy="conductor_vehiculo", cascade=CascadeType.PERSIST)
	protected List<Viaje> viajes= new ArrayList<Viaje>();
	
	public Maneja(){
		
	}

	public Maneja(Cliente cliente2, Vehiculo vehiculo2) {
		this.cliente=cliente2;
		this.vehiculo=vehiculo2;
		this.fecha_inicio=  new Date((new java.util.Date()).getTime());
		this.fecha_fin=null;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
}
