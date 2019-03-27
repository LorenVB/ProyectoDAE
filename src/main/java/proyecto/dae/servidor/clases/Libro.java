/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.clases;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import proyecto.dae.servidor.excepciones.NotaNoValida;
import proyecto.dae.servidor.servicios.dto.LibroDTO;

/**
 *
 * @author Miguel y Loren
 */
@Entity
public class Libro {
    @Id
    private String ISBN;
    
    
    private String tematica;
    private String titulo;
    private String autor;
    
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;
    
    @Temporal(TemporalType.DATE)
    private Date fechaRevisionAsignada;
   
    private Boolean pendiente;
    private Boolean enRevision;
    private Boolean revisado;
    private Boolean publicado;
    
    private Integer[] notas;
    
    public Libro(){
        ISBN = "";
        autor = "";
        enRevision = false; 
        fechaRevisionAsignada=null;
        fechaPublicacion = null;
        notas = null;
        pendiente = false;
        publicado = false;  
        revisado = false;
        tematica = "";
        titulo = "";
    };
    
    public Libro(String _ISBN, String _tematica, String _titulo, String _autor){
        ISBN = _ISBN;
        tematica = _tematica;
        titulo = _titulo;
        autor = _autor;
         enRevision = false; 
        fechaRevisionAsignada=null;
        fechaPublicacion = null;
        notas = new Integer[3];
        pendiente = false;
        publicado = false;  
        revisado = false;
        
    }
    
     public Libro(String _ISBN, String _autor, boolean _enRevision, Date _fechaRevision,Date _fechaPubli, Integer[] _notas, boolean _pendiente, boolean _publicado, boolean _revisado, String _tematica, String _titulo) {
        ISBN = _ISBN;
        autor = _autor;
        enRevision = _enRevision; 
        fechaRevisionAsignada=_fechaRevision;
        fechaPublicacion = _fechaPubli;
        notas = new Integer[3];
        pendiente = _pendiente;
        publicado = _publicado;  
        revisado = _revisado;
        tematica = _tematica;
        titulo = _titulo;
    }
    
    /**
     * Metodo AgregarNota. Agrega una nota a un libro
     * @param Nota La nota debe ser entre 0 y 5
     */
    public void AgregarNota(Integer Nota){
        if (Nota < 0 || Nota > 5){
            throw new NotaNoValida();
        }else{
            Integer asignado=0;
            for (int i=0; i<notas.length; i++){
                if (notas[i]==null) {
                    notas[i]=Nota;
                    asignado = i;
                    break;
                }
            }
            if (asignado==2){
            setEnRevision(false);
            setRevisado(true);
            
           }
        }
    }
    
    /**
     * Metodo CalcularNota. Metodo para calcular si la nota media de un libro
     * es superior a 2
     * @return True si la media de las 3 notas es menor que 10 y false si alguna
     * nota es menor que 2
     */
    public Boolean CalcularNota(){
        Integer suma=0;
        for (int i=0; i<notas.length; i++){
            suma+=notas[i];
            if(notas[i]<2){
               return false;
            }
        }
        return suma>=10;
    }

    /**
     * @return the ISBN
     */
    
    public String getISBN() {
        return ISBN;
    }

    /**
     * @return the tematica
     */
    public String getTematica() {
        return tematica;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @return the fechaPublicacion
     */
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * @return the publicado
     */
    public Boolean getPublicado() {
        return publicado;
    }

    /**
     * @return the revisado
     */
    public Boolean getEnRevision() {
        return enRevision;
    }

    /**
     * @return the notas
     */
    public Integer[] getNotas() {
        return notas;
    }

    /**
     * @param publicado the publicado to set
     */
    public void setPublicado(Boolean publicado) {
        this.publicado = publicado;
    }
    
    /**
     * @param fecha the publicado to set
     */
    public void setFechaPublicacion(Date fecha) {
        this.fechaPublicacion = fecha;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * @param revisado the revisado to set
     */
    public void setEnRevision(Boolean revisado) {
        this.enRevision = revisado;
    }
    
    /**
     * Metodo getLibroDTO. Crea un libroDTO
     * @return Devuelve un libroDTO
     */
    public LibroDTO getLibroDTO(){
        return new LibroDTO(ISBN,tematica,titulo,autor);
    }

    /**
     * @return the pendiente
     */
    public Boolean getPendiente() {
        return pendiente;
    }

    /**
     * @param pendiente the pendiente to set
     */
    public void setPendiente(Boolean pendiente) {
        this.pendiente = pendiente;
    }

    /**
     * @return the revisado
     */
    public Boolean getRevisado() {
        return revisado;
    }

    /**
     * @param revisado the revisado to set
     */
    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    /**
     * @return the fechaRevisionAsignada
     */
    public Date getFechaRevisionAsignada() {
        return fechaRevisionAsignada;
    }

    /**
     * @param fechaRevisionAsignada the fechaRevisionAsignada to set
     */
    public void setFechaRevisionAsignada(Date fechaRevisionAsignada) {
        this.fechaRevisionAsignada = fechaRevisionAsignada;
    }
}
