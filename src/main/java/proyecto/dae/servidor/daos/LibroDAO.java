/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.daos;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import proyecto.dae.servidor.clases.Libro;
import proyecto.dae.servidor.excepciones.ErrorCreacionLibro;

/**
 *
 * @author Miguel
 */
@Repository("LibroDAO")
@Transactional
public class LibroDAO {
    @PersistenceContext
    EntityManager em;
    
    /**
     * Metodo bucar. Realiza una busqueda de un libro por ISBN
     * @param ISBN Clave primaria
     * @return Devuelve  un libro según ISBN
     */
    @Cacheable(value="libros")
    @Transactional(readOnly=true)
    public Libro buscarISBN(String ISBN){
       String query= "select h from Libro h where h.ISBN = :ISBN" ;
       Libro buscado = em.createQuery(query,Libro.class).setParameter("ISBN", ISBN).getSingleResult();
       return buscado;
    }
    
    /**
     * Metodo buscarTitulo. Realiza una busqueda de un libro por titulo
     * @param titulo Titulo del libro
     * @return Devuelve el libro con dicho titulo
     */
    @Cacheable(value="libros")
    @Transactional(readOnly=true)
    public Libro buscarTitulo(String titulo){
        String query ="select h from Libro h where h.titulo= :titulo";
        Libro buscado = em.createQuery(query,Libro.class).setParameter("titulo", titulo).getSingleResult();
        return buscado;
    }
    
    /**
     * Metodo buscarTematica. Realiza una busqueda de un libro por tematica
     * @param tematica Tematica del libro
     * @return U¡Devuelve una lista de libros según temática
     */
    @Cacheable(value="libros")
    @Transactional(readOnly=true)
    public List<Libro> buscarTematica(String tematica){
        String query ="select h from Libro h where h.tematica= :tematica";
        List<Libro> libros = em.createQuery(query, Libro.class).setParameter("tematica", tematica).getResultList();
        return libros;
    }
    
    @Cacheable(value="libros")
    @Transactional(readOnly=true)
    public List<Libro> obtenerListaPendientes(){
        String query="select h from Libro h where h.pendiente = 'true'" ;
        List<Libro> pendientes= em.createQuery(query, Libro.class).getResultList();
        return pendientes;
    }


    /**
     * Metodo insertar. Realiza una inserción de un libro
     * @param libro El libro a insertar
     */
  @Transactional(rollbackFor = ErrorCreacionLibro.class)
    public void insertar(Libro libro) {
        try {
            em.persist(libro);
            em.flush();
        } catch (Exception e) {
            throw new ErrorCreacionLibro();
        }
    }

    
    /**
     * Metodo actualizar. Realiza una actualizacion de un libro 
     * @param libro El libro a actualizar
     */
    @CacheEvict(value="libros", key="#libro.getISBN()")
    public void actualizar(Libro libro){
        em.merge(libro);
    }
    
    /**
     * Metodo eliminar. Elimina un libro
     * @param libro El libro a eliminar
     */
    @CacheEvict(value="libros" , key="#libro.getISBN()")
    public void eliminar(Libro libro){
        em.remove(em.merge(libro));
    }
    
    
       
}