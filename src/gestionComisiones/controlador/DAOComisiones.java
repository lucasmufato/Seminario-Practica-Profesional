package gestionComisiones.controlador;

import gestionComisiones.modelo.ComisionCobrada;
import otros.DataAccesObject;

public class DAOComisiones extends DataAccesObject {

	public ComisionCobrada NuevaComisionCobrada(Double km) {
		ComisionCobrada cc= new ComisionCobrada();
		cc.setMonto(30);
		cc.setComision(null);
		// TODO este metodo tendria que buscar la comision actual para esos KM, y asignarle el monto a la ComisionCobrada
		return 	cc;
	}

	//vacio por ahora
}
