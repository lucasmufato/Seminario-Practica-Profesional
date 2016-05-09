package gestionViajes.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="localidad_viaje")
@IdClass(LocalidadViajeID.class)
public class LocalidadViaje {

	@Id
	@JoinColumn(name="id_viaje")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Viaje viaje;
	@Id
	@JoinColumn(name="id_localidad")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected Localidad localidad;
	@Column(nullable=false)
	protected Integer cantidad_pasajeros;
	
	@OneToMany(mappedBy="localidad_bajada", cascade=CascadeType.PERSIST)
	protected List<PasajeroViaje> pasajeros_bajaron= new ArrayList<PasajeroViaje>();
	
	@OneToMany(mappedBy="localidad_subida", cascade=CascadeType.PERSIST)
	protected List<PasajeroViaje> pasajeros_subieron= new ArrayList<PasajeroViaje>();
	
	public LocalidadViaje(){
		
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public Integer getCantidad_pasajeros() {
		return cantidad_pasajeros;
	}

	public void setCantidad_pasajeros(Integer cantidad_pasajeros) {
		this.cantidad_pasajeros = cantidad_pasajeros;
	}
	
}
