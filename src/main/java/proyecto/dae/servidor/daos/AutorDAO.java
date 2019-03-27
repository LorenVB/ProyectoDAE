/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import proyecto.dae.servidor.clases.Autor;
import proyecto.dae.servidor.excepciones.ErrorCreacionAutor;

/**
 *
 * @author Miguel
 */
@Repository("AutoresDAO")
@Transactional
public class AutorDAO {

    @PersistenceContext
    EntityManager em;

    /**
     * Metodo insertar. Realiza una inserción de un autor
     *
     * @param autor El autor a insertar
     */
    @Transactional(rollbackFor = ErrorCreacionAutor.class, readOnly = false)
    public void insertar(Autor autor) {
        try {
            em.persist(autor);
            em.flush();
        } catch (Exception e) {
            throw new ErrorCreacionAutor();
        }
    }

    /**
     * Metodo actualizar. Realiza una actualizacion de un autor
     *
     * @param autor El autor a actualizar
     */
    @CacheEvict(value="usuarios", key="#usuario.getEmail()")
    public void actualizar(Autor autor) {
        em.merge(autor);
    }

    /**
     * Metodo eliminar. Elimina un autor
     *
     * @param autor El autor a eliminar
     */
    public void eliminar(Autor autor) {
        em.remove(em.merge(autor));
    }

}
