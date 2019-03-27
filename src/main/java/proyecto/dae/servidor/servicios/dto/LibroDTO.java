/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.servicios.dto;

/**
 *
 * @author Miguel y Loren
 */
public class LibroDTO {
    private String ISBN;
    private String tematica;
    private String titulo;
    private String autor;
    
    public LibroDTO(){
        ISBN = "";
        tematica = "";
        titulo = "";
        autor = "";
    }

    ;
    
     public LibroDTO(String _ISBN, String _tematica, String _titulo, String _autor){
        ISBN = _ISBN;
        tematica = _tematica;
        titulo = _titulo;
        autor = _autor;
    }
     


    /**
     * @return the ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN the ISBN to set
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return the tematica
     */
    public String getTematica() {
        return tematica;
    }

    /**
     * @param tematica the tematica to set
     */
    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
}
