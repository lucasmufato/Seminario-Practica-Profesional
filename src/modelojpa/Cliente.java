package modelojpa;

import java.util.List;

import javax.persistence.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
@Table(name="cliente")

@DiscriminatorValue("C")		//valor que va en la tabla de usuario, por el cual JPA distingue que tipo de clase hijo es
public class Cliente extends Usuario implements JSONable {
	@Column(nullable=true)
	protected Integer reputacion;
	@Column(nullable=true)
	protected Integer puntos;
	@Column(nullable=true,length=120)
	protected String foto_registro;
	
	public Cliente(){
		super();
		this.reputacion=7;
		this.puntos=7;
		this.foto_registro="no tiene";
	}

	public Cliente(JSONObject cliente) {
		super(cliente);
		this.reputacion =(Integer) cliente.get("reputacion");
		this.puntos= (Integer) cliente.get("puntos");
		this.foto_registro=(String) cliente.get("foto_registro");
		this.tipo='C';
	}

	public Integer getReputacion() {
		return reputacion;
	}

	public void setReputacion(Integer reputacion) {
		this.reputacion = reputacion;
	}

	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON(){
		JSONArray idroles;
		List<UsuarioRol> listaroles;

		JSONObject json= new JSONObject();
		json.put("id_usuario", this.id_usuario);
		json.put("nombre_usuario", this.nombre_usuario);
		json.put("password", this.password);
		json.put("descripcion", this.descripcion);
		json.put("email", this.email);
		json.put("estado", this.estado.toString());
		json.put("tipo", this.tipo.toString());
		json.put("puntos", this.puntos);
		json.put("reputacion", this.reputacion);
		//envio el id de la persona con la q esta relacionada
		if(this.persona!=null){
			json.put("id_persona", this.persona.getId_persona());
		}else{
			json.put("id_persona", -1);
		}

		idroles = new JSONArray ();
		listaroles = this.getRoles();
		if (listaroles != null) {
			for (Object rol: listaroles) {
				idroles.add (((UsuarioRol)rol).getRol().getId_rol());
			}
		}
		json.put("roles", idroles);
		
		
		return json;
	}
	
	@Override
	public void SetJSONObject(JSONObject json) {
		this.id_usuario=(Integer) json.get("id_usuario");
		this.nombre_usuario= (String) json.get("nombre_usuario");
		this.password= (String) json.get("password");
		this.email= (String) json.get("email");
		this.descripcion= (String) json.get("descripcion");
		String estado=(String) json.get("estado");
		if(estado!=null){
			this.estado= json.get("estado").toString().charAt(0);
		}else{
			this.estado=null;
		}
		this.reputacion =(Integer) json.get("reputacion");
		this.puntos= (Integer) json.get("puntos");
		this.tipo='C';
	}

	@Override
	public Object getPrimaryKey() {
		return this.id_usuario;
	}
	
}
