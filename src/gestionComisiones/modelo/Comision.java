package gestionComisiones.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.json.simple.JSONObject;

import otros.JSONable;

@NamedQueries({
	
})
@Entity
@Table(name="COMISION")
public class Comision implements JSONable {

	@Id
	@Column(nullable=false,name="ID_COMISION")
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGeneratorComision")
	@SequenceGenerator(allocationSize=1, schema="seminario",  name="MySequenceGeneratorComision", sequenceName = "sequence")
	protected Integer id_comision;
	@Column(nullable=false,name="LIMITE_SUPERIOR")
	protected float limite_superior;
	@Column(nullable=false,name="LIMITE_INFERIOR")
	protected float limite_inferior;
	
	@JoinColumn(name="PRECIO_COMISION")
	@ManyToOne(cascade=CascadeType.PERSIST)
	protected PrecioComision precio_comision;
	@OneToMany(mappedBy="comision", cascade=CascadeType.PERSIST)
	protected List<ComisionCobrada> comisiones_cobradas;
	
	public Comision(){
		
	}
	
	public Integer getId() {
		return id_comision;
	}

	public void setId(Integer id_comision) {
		this.id_comision = id_comision;
	}

	public float getLimite_superior() {
		return limite_superior;
	}

	public void setLimite_superior(float limite_superior) {
		this.limite_superior = limite_superior;
	}

	public float getLimite_inferior() {
		return limite_inferior;
	}

	public void setLimite_inferior(float limite_inferior) {
		this.limite_inferior = limite_inferior;
	}

	public PrecioComision getPrecio_comision() {
		return precio_comision;
	}

	public void setPrecio_comision(PrecioComision precio_comision) {
		this.precio_comision = precio_comision;
	}

	public Integer getId_comision() {
		return id_comision;
	}

	public void setId_comision(Integer id_comision) {
		this.id_comision = id_comision;
	}

	public List<ComisionCobrada> getComisiones_cobradas() {
		return comisiones_cobradas;
	}

	public void setComisiones_cobradas(List<ComisionCobrada> comisiones_cobradas) {
		this.comisiones_cobradas = comisiones_cobradas;
	}

	@Override
	public void SetJSONObject(JSONObject json) {
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

	public static ComisionCobrada NuevaComisionCobrada(Double km) {
		ComisionCobrada cc= new ComisionCobrada();
		cc.setMonto(30);
		cc.setComision(null);
		// TODO Auto-generated method stub
		return 	cc;
	}

}
