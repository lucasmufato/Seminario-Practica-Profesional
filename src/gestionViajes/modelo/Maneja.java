package gestionViajes.modelo;

import java.sql.Date;

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

@NamedQueries({
//@NamedQuery(name="Maneja.SearchById",query="SELECT m FROM Maneja m WHERE ( (m.id_conductor= :idc) AND (m.id_vehiculo= :idveh) )"),//agregada por fede	
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
	
	@Column(nullable=false)
	protected Date fecha_inicio;
	@Column(nullable=true)
	protected Date fecha_fin;
	
	
	
	public Maneja(){
		
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
