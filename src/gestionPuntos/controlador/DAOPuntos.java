package gestionPuntos.controlador;

import gestionPuntos.modelo.EstadoSancion;
import gestionPuntos.modelo.MovimientoPuntos;
import gestionPuntos.modelo.Sancion;
import gestionUsuarios.controlador.DAOAdministracionUsuarios;
import gestionUsuarios.modelo.Cliente;
import gestionUsuarios.modelo.Usuario;
import gestionViajes.controlador.DAOViajes;
import gestionViajes.modelo.EstadoViaje;
import gestionViajes.modelo.Localidad;
import gestionViajes.modelo.PasajeroViaje;
import gestionViajes.modelo.Viaje;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;

import static java.time.Instant.now;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import org.json.simple.JSONObject;
import otros.DataAccesObject;
import otros.ExceptionViajesCompartidos;
import otros.ManejadorErrores;

public class DAOPuntos extends DataAccesObject {
		//vacio por ahora
        private float alfa = (float) 1.5;
        private float beta = (float) 2.5;
        
        
        public DAOPuntos(){
            super();
            
        }
        
    //byfede    
    public boolean evaluarSancion(Integer id_cliente, Integer id_viaje, Timestamp fechaYHoraCancelacion) throws ExceptionViajesCompartidos{
       
        //System.out.println("Entro a DAOPUNTOS Con:\n IdCli:"+id_cliente+"\n IdViaje:"+id_viaje+"\n Time:"+fechaYHoraCancelacion+"");
        double descuento = this.calculcarDescuentoPuntos(id_viaje, id_cliente);
        if(descuento!=0){ //sanciono si cancelo tarde 
            if(this.entitymanager.getTransaction().isActive()){
                this.entitymanager.getTransaction().rollback();
            }
              this.entitymanager.getTransaction( ).begin( );
            MovimientoPuntos mov = new MovimientoPuntos();
            Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);         
            mov.setCliente(cliente);
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date fecha = new java.sql.Date(utilDate.getTime());
            mov.setFecha((Date) fecha);
            descuento = descuento - 2*(descuento);
            Integer descuento_int = (int)descuento;
            mov.setMonto(descuento_int);
            Sancion sancion = new Sancion();
            sancion.setCliente(cliente);
            sancion.setMovimiento_puntos(mov);
            utilDate = new java.util.Date();
            fecha = new java.sql.Date(utilDate.getTime());
            sancion.setFecha_inicio(fecha);
            sancion.setFecha_fin(fecha);            
            sancion.setEstado(EstadoSancion.caduca);//le pongo caduca porque es de puntos, no es por tiempo.      
            
            
            
            
            try{    
                    this.entitymanager.persist(mov);
                    this.entitymanager.getTransaction( ).commit( );
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.persist(sancion);
                    this.entitymanager.getTransaction( ).commit( );
                    boolean bandera = this.actualizarPuntosCliente(descuento_int, cliente.getId_usuario());
            }catch(RollbackException e){
                    String error= ManejadorErrores.parsearRollback(e);
                    throw new ExceptionViajesCompartidos("ERROR: "+error);
            }     

        }
       
        return true;
    }
    
    //byfede
    public double calculcarDescuentoPuntos(Integer id_viaje, Integer id_cliente){
        double puntos=0;
           Timestamp actual= new Timestamp((new java.util.Date()).getTime());
           DAOViajes daoviajes = new DAOViajes();           
           Viaje viaje= (Viaje) this.buscarPorPrimaryKey(new Viaje(), id_viaje);
           Timestamp salida_viaje = viaje.getFecha_inicio();
           //Formula
           // puntos_a_restar = (SancionesAnteriores * Alfa) + ( (1/HorasQueFaltanHastaPartir) * Beta)
           Cliente cliente = new Cliente();
           cliente.setId_usuario(id_cliente);
            try {
                List <Sancion> sanciones =  this.buscarSanciones(cliente);          //busco cuantas tuvo
                Integer cant_sanciones_ant = sanciones.size();                      //saco el cardinal 
                double dif_horas = this.diferenciaTimestamps(salida_viaje, actual); //calculo diferencia de fechas
                
                if(dif_horas<=12){ //si cancelo con menos de 12 horas de anticipacion entro
                    float primer_termino_formula = (cant_sanciones_ant * alfa);
                    double segundo_termino_formula = ((1/dif_horas)*beta);
                    double resultado = primer_termino_formula + segundo_termino_formula;
                    puntos = resultado; 
                }//sino entro es porque cancelo con anticipacion y le descuento 0
            } catch (ExceptionViajesCompartidos ex) {
                Logger.getLogger(DAOPuntos.class.getName()).log(Level.SEVERE, null, ex);
            }          
           
         
        return puntos;
    }
    
    //byfede
    public List<Sancion> buscarSanciones(Cliente cliente) throws ExceptionViajesCompartidos{
		List<Sancion> sanciones = null;
		if(cliente.getId_usuario()==null){
			throw new ExceptionViajesCompartidos("ERROR: USUARIO INEXISTENTE");
                }		
		Query qry = entitymanager.createNamedQuery("Sancion.PorIDCliente");
    		qry.setParameter("id_cliente", cliente);
    		sanciones = (List<Sancion>)qry.getResultList();
		
		return sanciones;
	}
    
    //byfede
    public double diferenciaTimestamps(Timestamp salida_viaje, Timestamp cancelacion){
        
        double diferencia = 0;        
        long long_salida = salida_viaje.getTime();
        long long_cancelacion = cancelacion.getTime();
        long long_diferencia = long_salida - long_cancelacion;
        if(long_diferencia<0){
            diferencia= 0.000001;
        }else{
                long totalSecs = long_diferencia/1000;
                long hours = (totalSecs / 3600);
                long mins = (totalSecs / 60) % 60;
                long secs = totalSecs % 60;

                if (hours > 0){
                    diferencia = (double)diferencia + hours+(mins/60d);
                }else{
                    if (mins > 0){
                        diferencia = (double)(mins/60d);   


                    }else{
                        diferencia = (double) 0.0001;
                    }

                }
        }
        return diferencia;
    }
    
    public boolean actualizarPuntosCliente(int monto, int id_cliente) throws ExceptionViajesCompartidos{
        
        if(this.entitymanager.getTransaction().isActive()){
                this.entitymanager.getTransaction().rollback();
        }
        this.entitymanager.getTransaction( ).begin( );
        Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_cliente);     
        Integer puntos_cuenta = cliente.getPuntos();
        puntos_cuenta  = puntos_cuenta + (int)monto;
        cliente.setPuntos(puntos_cuenta);
        try{                
                this.entitymanager.getTransaction( ).commit( );
        }catch(Exception e){
		String error= ManejadorErrores.parsearRollback((RollbackException) e);
        	throw new ExceptionViajesCompartidos("ERROR: "+error);
		
        }        
        return true;
    }
    
    public boolean sancionarChofer(int id_viaje, int id_chofer) throws ExceptionViajesCompartidos{
        //formula
        // puntos = (CantPas * 50 ) + (1/hsfaltan * beta)
        this.entitymanager.getTransaction().begin();
        Viaje viaje = new Viaje();
        Integer id_viaje2 = id_viaje;
        viaje = (Viaje) this.buscarPorPrimaryKey(viaje, id_viaje);
        Timestamp fecha_inicio = viaje.getFecha_inicio();
        Timestamp actual= new Timestamp((new java.util.Date()).getTime());
        double dif_horas = this.diferenciaTimestamps(fecha_inicio, actual); //calculo diferencia de fechas
        double segundo_termino = (1/dif_horas)*beta;
        List<PasajeroViaje> lista = viaje.getPasajeros();
        int cantidad_pasajeros = lista.size();
        double primer_termino = cantidad_pasajeros * 50;
        double resultado = primer_termino + segundo_termino;
        MovimientoPuntos mov = new MovimientoPuntos();
        Cliente cliente = (Cliente) this.buscarPorPrimaryKey(new Cliente(), id_chofer);         
        mov.setCliente(cliente);
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date fecha = new java.sql.Date(utilDate.getTime());
        mov.setFecha((Date) fecha);
        resultado = resultado - 2*(resultado);
        Integer resultado_int = (int)resultado;
        mov.setMonto(resultado_int);
        Sancion sancion = new Sancion();
        sancion.setCliente(cliente);
        sancion.setMovimiento_puntos(mov);
        utilDate = new java.util.Date();
        fecha = new java.sql.Date(utilDate.getTime());
        sancion.setFecha_inicio(fecha);
        sancion.setFecha_fin(fecha);            
        sancion.setEstado(EstadoSancion.caduca);//le pongo caduca porque es de puntos, no es por tiempo.
        try{    
                    this.entitymanager.persist(mov);
                    this.entitymanager.getTransaction().commit();
                    this.entitymanager.getTransaction().begin();
                    this.entitymanager.persist(sancion);
                    this.entitymanager.getTransaction().commit();
                    //this.entitymanager.getTransaction( ).commit( );
                    boolean bandera = this.actualizarPuntosCliente(resultado_int, cliente.getId_usuario());
            }catch(RollbackException e){
                    String error= ManejadorErrores.parsearRollback(e);
                    throw new ExceptionViajesCompartidos("ERROR: "+error);
            }               
        
        
        return true;
    }
}
