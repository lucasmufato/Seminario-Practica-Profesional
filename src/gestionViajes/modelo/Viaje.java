package gestionViajes.modelo;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import gestionUsuarios.modelo.Cliente;
import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="viaje")
public class Viaje implements JSONable {

	@Id
	protected Integer id;
	protected Date fecha_inicio;

	
	protected Maneja conductor;
	protected List<Localidad> localidades;
	protected List<PasajeroViaje> pasajeros;
	
	public void añadir_localidad( Localidad loc1, Localidad loc2){
		
	}
	
	public void añadir_pasajeroViaje (Integer id_cliente, Localidad subida, Localidad bajada){
		//el pasajeroViaje se crea con estado POSTULADO
	}
	
	public List<Localidad> devolver_recorrido_desde_hasta(Localidad desde, Localidad hasta){
		return null;
	}
	
	public PasajeroViaje recuperar_pasajeroViaje_por_cliente(Cliente cliente){
		return null;
	}
	
	public Viaje(){
		
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

	public Maneja getConductor() {
		return conductor;
	}

	public void setConductor(Maneja conductor) {
		this.conductor = conductor;
	}

	public List<Localidad> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidad> localidades) {
		this.localidades = localidades;
	}

	public List<PasajeroViaje> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<PasajeroViaje> pasajeros) {
		this.pasajeros = pasajeros;
	}

	@Override
	public void SetJSONObject(JSONObject json) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
