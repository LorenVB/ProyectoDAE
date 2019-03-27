/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import proyecto.dae.servidor.clases.Usuario;
import proyecto.dae.servidor.excepciones.ErrorCreacionUsuario;

/**
 *
 * @author Miguel
 */
@Repository("UsuariosDAO")
@Transactional
public class UsuarioDAO {

    @PersistenceContext
    EntityManager em;

    /**
     * Metodo bucar. Realiza una busqueda de un usuario por id
     *
     * @param email Clave primaria
     * @return Un usuario según su email
     */
    @Cacheable(value="usuarios")
    @Transactional(readOnly = true)
    public Usuario buscarEmail(String email) {
        return em.find(Usuario.class, email);
    }

    /**
     * Metodo buscarToken. Realiza una busqueda de un usuario por token
     *
     * @param token Token del usuario
     * @return Un libro según token
     */
    @Cacheable(value="usuarios")
    @Transactional(readOnly = true)
    public Usuario buscarToken(String token) {
        String query = "select h from Usuario h where h.token = :token";
        Usuario buscado = em.createQuery(query, Usuario.class).setParameter("token", token).getSingleResult();
        return buscado;
    }

    /**
     * Metodo buscarTematica. Realiza una busqueda de un libro por tematica
     *
     * @param email Email del usuario
     * @return Una lista de libros
     */
    /**
     * Metodo insertar. Realiza una inserción de un usuario
     *
     * @param usuario El usuario a insertar
     */
    @Transactional(rollbackFor = ErrorCreacionUsuario.class)
    public void insertar(Usuario usuario) {
        try {
            em.persist(usuario);
            em.flush();
        } catch (Exception e) {
            throw new ErrorCreacionUsuario();
        }
    }

    /**
     * Metodo actualizar. Realiza una actualizacion de un usuario
     *
     * @param usuario El usuario a actualizar
     */
    @CacheEvict(value="usuarios", key="#usuario.getEmail()")
    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

    /**
     * Metodo eliminar. Elimina un usuario
     *
     * @param usuario El usuario a eliminar
     */
    @CacheEvict(value="usuarios", key="#usuario.getEmail()")
    public void eliminar(Usuario usuario) {
        em.remove(em.merge(usuario));
    }
}
