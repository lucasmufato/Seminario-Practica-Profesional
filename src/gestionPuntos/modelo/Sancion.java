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
@Table(name="sancion")
public class Sancion {
	
	@Id
	protected Integer id;
	
	protected Date fecha_inicio;
	protected Date fecha_fin;
	
	protected Cliente cliente;
	protected MovimientoPuntos movimiento_puntos;
	protected TipoSancion tipo_sancion;
	
	public Sancion(){
		
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

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public MovimientoPuntos getMovimiento_puntos() {
		return movimiento_puntos;
	}

	public void setMovimiento_puntos(MovimientoPuntos movimiento_puntos) {
		this.movimiento_puntos = movimiento_puntos;
	}

	public TipoSancion getTipo_sancion() {
		return tipo_sancion;
	}

	public void setTipo_sancion(TipoSancion tipo_sancion) {
		this.tipo_sancion = tipo_sancion;
	}
	
	
	
}
