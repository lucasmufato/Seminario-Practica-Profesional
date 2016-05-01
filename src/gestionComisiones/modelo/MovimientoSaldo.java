package gestionComisiones.modelo;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@NamedQueries({
	
})
@Entity
@Table(name="movimiento_saldo")
public class MovimientoSaldo {
	
	@Id
	protected Integer id;
	protected Date fecha;
	protected float monto;
	
	protected Pago pago;
	
	protected ComisionCobrada comision_cobrada;
	
	public MovimientoSaldo(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public ComisionCobrada getComision_cobrada() {
		return comision_cobrada;
	}

	public void setComision_cobrada(ComisionCobrada comision_cobrada) {
		this.comision_cobrada = comision_cobrada;
	}
}
