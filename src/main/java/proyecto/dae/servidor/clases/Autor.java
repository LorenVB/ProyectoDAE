/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.clases;

import proyecto.dae.servidor.excepciones.libroInexistente;
import java.util.*;
import javax.persistence.Entity;

/**
 *
 * @author Miguel y Loren
 */

@Entity
public class Autor extends Usuario{
  
    
    ArrayList<Libro> historialLibrosEscritos;

    public Autor() {
    }

    public Autor(int _edad, String _email, String _nombre, String _password, String _rol, String _token, ArrayList<Libro> _historialLibrosEscritos) {
        super(_edad, _email, _nombre, _password, _rol, _token);
        historialLibrosEscritos =  new ArrayList();
    }
    
    /**
     * Funcion AnadirLibro. Añade un libro al historial de libros escritos
     * @param _libro El libro a añadir
     */
    public void AnadirLibro(Libro _libro){
        if (_libro == null) throw new libroInexistente();
        historialLibrosEscritos.add(_libro);
    }
}
