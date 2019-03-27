/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.clases;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import proyecto.dae.servidor.excepciones.libroInexistente;

/**
 *
 * @author Miguel y Loren
 */
@Entity
public class Revisor extends Usuario{
    
   @ManyToMany
   private List<Libro> pendientesDeRevision;
   @Temporal(TemporalType.TIMESTAMP)
   private Date ultFechaAsignacion;
   private Integer cargaDeTrabajo;

    public Revisor() {
    }
    
    
    public Revisor(int _edad, String _email, String _nombre, String _password, String _rol, String _token, LinkedList _pendientesDeRevision, Date _ultFechaAsignacion, Integer _cargaDeTrabajo ) {
        super(_edad, _email, _nombre, _password, _rol, _token);
        pendientesDeRevision =  new LinkedList<>();
        ultFechaAsignacion = _ultFechaAsignacion;
        cargaDeTrabajo= _cargaDeTrabajo;
    }

    /**
     * @return the ultFechaAsignacion
     */
    public Date getUltFechaAsignacion() {
        return ultFechaAsignacion;
    }

    /**
     * @param ultFechaAsignacion the ultFechaAsignacion to set
     */
    public void setUltFechaAsignacion(Date ultFechaAsignacion) {
        this.ultFechaAsignacion = ultFechaAsignacion;
    }
    /**
     * @return the pendientesDeRevision
     */
    public List getPendientesDeRevision() {
        return pendientesDeRevision;
    }
    
    /**
     * Metodo getCargadeTrabajo. Obtienes la carga de trabajo de un Revisor
     * @return La carga de trabajo
     */
    public Integer getCargadeTrabajo(){
        return cargaDeTrabajo;
    }
    
    
    /**
     * @param cargaDeTrabajo the cargaDeTrabajo to set
     */
    public void setCargaDeTrabajo(Integer cargaDeTrabajo) {
        this.cargaDeTrabajo = cargaDeTrabajo;
    }

    
    /**
     * Metodo anadirLibroRevisor. Añade un libro a la lista de pendiente
     * de revision de un revisor
     * @param _libro El libro que se añade 
     */
    public void anadirLibroRevisor(Libro _libro){
      if (_libro == null) throw new libroInexistente();
        getPendientesDeRevision().add(_libro);
    }
}