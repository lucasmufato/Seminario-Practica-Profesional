package gestionPuntos.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@NamedQueries({
	
})
@Entity
@Table(name="tipo_sancion")
public class TipoSancion {

	@Id
	protected Integer id;
	protected String descripcion;
	protected Integer dias_sancion;
	
	protected List<Sancion> sanciones;
	
	public TipoSancion(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDias_sancion() {
		return dias_sancion;
	}

	public void setDias_sancion(Integer dias_sancion) {
		this.dias_sancion = dias_sancion;
	}

	public List<Sancion> getSanciones() {
		return sanciones;
	}

	public void setSanciones(List<Sancion> sanciones) {
		this.sanciones = sanciones;
	}
}
