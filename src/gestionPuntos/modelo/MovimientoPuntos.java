package gestionPuntos.modelo;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import gestionUsuarios.modelo.Cliente;

@NamedQueries({
	
})
@Entity
@Table(name="movimiento_puntos")
public class MovimientoPuntos {
	
	@Id
	protected Integer id_movimiento_puntos;
	protected Cliente cliente;
	protected Date fecha;
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
