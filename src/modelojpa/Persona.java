package modelojpa;

import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.simple.JSONObject;

@Table(name="persona")
@NamedQueries({
	@NamedQuery(name="Persona.todos",query="SELECT p FROM Persona p"),
	@NamedQuery(name="Persona.porNombre",query="SELECT p FROM Persona p	WHERE p.nombres LIKE :nombre"),
	@NamedQuery(name="Persona.SearchById",query="SELECT p FROM Persona p WHERE p.id_persona = :id"),
	@NamedQuery(name="Persona.porNombreYApellido", query= "SELECT p FROM Persona p WHERE p.nombres LIKE :nombre and p.apellidos LIKE :apellidos"),
	@NamedQuery(name="Persona.porNroDocumento",query="SELECT p FROM Persona p WHERE p.nro_doc = :nro_doc"),
	@NamedQuery(name="Persona.porEstado",query="SELECT p FROM Persona p WHERE p.estado = :estado"),
	
})
@Entity
public class Persona implements JSONable {
	
	@Id
	@Column(nullable=false)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGeneratorPersona")
	@SequenceGenerator(allocationSize=1, schema="pruebajpa",  name="MySequenceGeneratorPersona", sequenceName = "sequence")
	protected Integer id_persona;
	@Column(nullable=false,length=60)
	protected String nombres;
	@Column(nullable=false,length=60)
	protected String apellidos;
	@Column(nullable=false)
	protected Integer tipo_doc;
	@Column(nullable=false)
	protected BigInteger nro_doc;
	@Column(nullable=false)
	protected Date fecha_nacimiento;
	@Column(nullable=false,length=30)
	protected Character sexo;
	@Column(nullable=true,length=120)
	protected String foto;
	@Column(nullable=false,length=120)
	protected String domicilio;
	@Column(nullable=false,length=16)
	protected String telefono;
	@Column(nullable=true)
	protected String descripcion;
	@Column(nullable=false,length=1)
	protected Character estado;
	@Column(nullable=true,length=120)
	protected String foto_registro;
	
	
	public Persona(){
		this.nombres="lucas";
		this.apellidos="mufato";
		this.tipo_doc=1;
		this.descripcion="yoyoyoyoyoo";
		this.domicilio="dr muñiz 1482";
		this.estado='A';
		this.fecha_nacimiento= new java.sql.Date( (new java.util.Date()).getTime() );
		this.foto="url://fefdsefsefsefes";
		this.foto_registro="URL://efdsefesfsef";
		this.telefono="425790";
		this.id_persona=1;
		this.nro_doc= BigInteger.valueOf(new Integer(38842784).intValue());
		this.sexo='M';
	}

	public Persona(JSONObject json){ 
		this.id_persona=Integer.parseInt(json.get("id_persona").toString());
		this.nombres=(String)json.get("nombres");
		this.apellidos=(String)json.get("apellidos");
		this.tipo_doc=Integer.parseInt(json.get("tipo_doc").toString());
		this.nro_doc=new BigInteger(json.get("nro_doc").toString());
		String fn=(String)json.get("fecha_nacimiento");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed=null;
		try {
			parsed = format.parse(fn);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        this.fecha_nacimiento = new java.sql.Date(parsed.getTime());
		this.sexo=json.get("sexo").toString().charAt(0);
		this.foto=(String)json.get("foto");
		this.domicilio=(String)json.get("domicilio");
		this.telefono=(String)json.get("telefono");
		this.descripcion=(String)json.get("descripcion");
		this.estado=json.get("estado").toString().charAt(0);
		this.foto_registro=(String)json.get("foto_registro");
	}

	public Integer getId_persona() {
		return id_persona;
	}


	public void setId_persona(Integer id_persona) {
		this.id_persona = id_persona;
	}


	public String getNombres() {
		return nombres;
	}


	public void setNombres(String nombres) {
		this.nombres = nombres;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public Integer getTipo_doc() {
		return tipo_doc;
	}


	public void setTipo_doc(Integer tipo_doc) {
		this.tipo_doc = tipo_doc;
	}


	public BigInteger getNro_doc() {
		return nro_doc;
	}


	public void setNro_doc(BigInteger nro_doc) {
		this.nro_doc = nro_doc;
	}


	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}


	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}


	public Character getSexo() {
		return sexo;
	}


	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}


	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}


	public String getDomicilio() {
		return domicilio;
	}


	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Character getEstado() {
		return estado;
	}


	public void setEstado(Character estado) {
		this.estado = estado;
	}


	public String getFoto_registro() {
		return foto_registro;
	}


	public void setFoto_registro(String foto_registro) {
		this.foto_registro = foto_registro;
	}	
	
	public String toString(){
		return "Persona: ["+this.id_persona+" , "+this.nombres+" , "+this.apellidos+this.descripcion+" , "+this.domicilio+" , "+
					this.foto+" , "+this.foto_registro+" , "+this.telefono+" , "+this.sexo+" , "+this.tipo_doc+" , "+this.nro_doc+" , "+
					this.fecha_nacimiento.toString()+" , "+this.estado+" ]";
	}
	
	
	@SuppressWarnings("unchecked")  //esta anotacion es para q no rompa las bolas con los warnings
	@Override
	public JSONObject toJSON(){
		JSONObject json=new JSONObject();
		json.put("id_persona", this.id_persona);
		json.put("nombres", this.nombres);
		json.put("apellidos", this.apellidos);
		json.put("domicilio", this.domicilio);
		json.put("descripcion", this.descripcion);
		json.put("foto", this.foto);
		json.put("foto_registro", this.foto_registro);
		json.put("telefono", this.telefono);
		json.put("sexo", this.sexo.toString());
		json.put("tipo_doc", this.tipo_doc);
		json.put("nro_doc", this.nro_doc);
		json.put("fecha_nacimiento", this.fecha_nacimiento);
		json.put("estado",this.estado.toString());
		return json;
	}
	
	@Override
	public void SetJSONObject(JSONObject json) {
		this.id_persona=Integer.parseInt(json.get("id_persona").toString());
		this.nombres=(String)json.get("nombres");
		this.apellidos=(String)json.get("apellidos");
		this.tipo_doc=Integer.parseInt(json.get("tipo_doc").toString());
		this.nro_doc=new BigInteger(json.get("nro_doc").toString());
		this.fecha_nacimiento=(Date)json.get("fecha_nacimiento");
		this.sexo=json.get("sexo").toString().charAt(0);
		this.foto=(String)json.get("foto");
		this.domicilio=(String)json.get("domicilio");
		this.telefono=(String)json.get("telefono");
		this.descripcion=(String)json.get("descripcion");
		this.estado=json.get("estado").toString().charAt(0);
		this.foto_registro=(String)json.get("foto_registro");
	}

	@Override
	public Object getPrimaryKey() {
		return this.id_persona;
	}
}
