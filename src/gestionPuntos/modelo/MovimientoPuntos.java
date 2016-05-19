package gestionPuntos.modelo;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import gestionUsuarios.modelo.Cliente;

@NamedQueries({
	
})
@Entity
@Table(name="movimiento_puntos")
public class MovimientoPuntos {
	
	@Id
	@Column(nullable=false,name="ID_MOVIMIENTOS_PUNTOS")
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGeneratorMovimientoPuntos")
	@SequenceGenerator(allocationSize=1, schema="pruebajpa",  name="MySequenceGeneratorMovimientoPuntos", sequenceName = "sequence")
	protected Integer id_movimiento_puntos;
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="ID_CLIENTE")
	protected Cliente cliente;
	@Column(nullable=false,name="FECHA")
	protected Date fecha;
	@Column(nullable=false,name="MONTO")
	protected Integer monto;
	
	public MovimientoPuntos(){
		
	}

	public Integer getId() {
		return id_movimiento_puntos;
	}

	public void setId(Integer id) {
		this.id_movimiento_puntos = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}
	
}
