package gestionUsuarios.modelo;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="notificacion")
public class Notificacion {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)		//pruebo como es el tema con generationType.auto
	@Column(name="ID_NOTIFICACION")
	protected Integer id_notificacion;
	@Column(name="FECHA",nullable=false,length=30)
	protected Date fecha;
	@Column(name="TEXTO",nullable=false,length=200)
	protected String texto;
	@Column(name="ESTADO",nullable=false,length=10)
	protected EstadoNotificacion estado;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Cliente cliente;
	
	public Notificacion(){
		
	}

	public Integer getId_notificacion() {
		return id_notificacion;
	}

	public void setId_notificacion(Integer id_notificacion) {
		this.id_notificacion = id_notificacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public EstadoNotificacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoNotificacion estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
