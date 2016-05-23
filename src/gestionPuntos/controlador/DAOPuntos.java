package gestionPuntos.controlador;

import java.sql.Timestamp;
import otros.DataAccesObject;

public class DAOPuntos extends DataAccesObject {
		//vacio por ahora
    
    public boolean evaluarSancion(Integer id_cliente, Integer id_viaje, Timestamp fechaYHoraCancelacion){
        //TODO
        System.out.println("Entro a DAOPUNTOS Con:\n IdCli:"+id_cliente+"\n IdViaje:"+id_viaje+"\n Time:"+fechaYHoraCancelacion+"");
        return true;
    }
}
