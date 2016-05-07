package gestionViajes.modelo;

import java.io.Serializable;

public class ManejaID implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer cliente;
	private Integer vehiculo;
	
	public ManejaID(){
		
	}
	
	public ManejaID(Integer id_cliente, Integer id_vehiculo){
		this.cliente=id_cliente;
		this.vehiculo=id_vehiculo;
	}
	public Integer getCliente() {
		return cliente;
	}

	public void setCliente(Integer cliente) {
		this.cliente = cliente;
	}

	public Integer getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Integer vehiculo) {
		this.vehiculo = vehiculo;
	}

	public int hashCode() {
		//este metodo no sirve para nada! lo pide la interfaz Serializable
	    return 5;
	  }

	  public boolean equals(Object object) {
		//este metodo no sirve para nada! lo pide la interfaz Serializable
	    return false;
	  }
	
}
