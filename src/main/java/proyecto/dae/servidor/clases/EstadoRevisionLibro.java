/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.clases;

import proyecto.dae.servidor.servicios.dto.LibroDTO;

/**
 *
 * @author Loren
 */
public class EstadoRevisionLibro {
    private LibroDTO Libro;
    private String EstadoRevision;

    public EstadoRevisionLibro(LibroDTO _libro,long diferenciaDias){
        Libro = _libro;
        if (diferenciaDias > -23) {
            EstadoRevision = "EN_PLAZO";
        } else {
            if (diferenciaDias > -23 && diferenciaDias <= -30) {
                EstadoRevision = "FUERA_PLAZO_EN_1_SEMANA";
            } else {
                if(diferenciaDias<-30)
                EstadoRevision = "FUERA_PLAZO";
            }
        }
    }
        /**
         * @return the Libro
         */
    public LibroDTO getLibro() {
        return Libro;
    }

    /**
     * @param Libro the Libro to set
     */
    public void setLibro(LibroDTO Libro) {
        this.Libro = Libro;
    }

    /**
     * @return the EstadoRevision
     */
    public String getEstadoRevision() {
        return EstadoRevision;
    }

    /**
     * @param EstadoRevision the EstadoRevision to set
     */
    public void setEstadoRevision(String EstadoRevision) {
        this.EstadoRevision = EstadoRevision;
    }

}
