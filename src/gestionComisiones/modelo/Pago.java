package gestionComisiones.modelo;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import gestionUsuarios.modelo.Cliente;

@NamedQueries({
	
})
@Entity
@Table(name="pago")
public class Pago {
	
	@Id
	protected Integer id_pago;
	protected Date fecha;
	protected float monto;
	
	protected Cliente cliente;
	protected MovimientoSaldo movimiento_saldo;
	
	public Pago(){
		
	}

	public Integer getId() {
		return id_pago;
	}

	public void setId(Integer id) {
		this.id_pago = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public MovimientoSaldo getMovimiento_saldo() {
		return movimiento_saldo;
	}

	public void setMovimiento_saldo(MovimientoSaldo movimiento_saldo) {
		this.movimiento_saldo = movimiento_saldo;
	}
}
