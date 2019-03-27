/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.daos;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import proyecto.dae.servidor.clases.Libro;
import proyecto.dae.servidor.clases.Revisor;
import proyecto.dae.servidor.clases.Usuario;
import proyecto.dae.servidor.excepciones.ErrorCreacionRevisor;
import proyecto.dae.servidor.excepciones.noExistenLibros;
import proyecto.dae.servidor.servicios.dto.LibroDTO;

/**
 *
 * @author Miguel
 */
@Repository("RevisoresDAO")
@Transactional
public class RevisorDAO {

    @PersistenceContext
    EntityManager em;

    /**
     * 
     * @param token
     * @return 
     */
    @Cacheable(value="revisores")
    public Usuario buscarToken(String token) {
        String query = "select h from Usuario h where h.token = :token";
        Usuario buscado = em.createQuery(query, Usuario.class).setParameter("token", token).getSingleResult();
        return buscado;
    }

    /**
     *
     * @param email
     * @return
     */
    @Cacheable(value="revisores")
    public Revisor buscarEmail(String email) {
        return em.find(Revisor.class, email);
    }

    /**
     * Metodo insertar. Realiza una inserción de un usuario
     *
     * @param revisor El revisor a insertar
     */
    @Transactional(rollbackFor = ErrorCreacionRevisor.class, readOnly = false)
    public void insertar(Revisor revisor) {
          try {
            em.persist(revisor);
            em.flush();
        } catch (Exception e) {
            throw new ErrorCreacionRevisor();
        }
    }

    /**
     * Metodo actualizar. Realiza una actualizacion de un usuario
     *
     * @param revisor El revisor a actualizar
     */
    @CacheEvict(value="revisores", key="#revisor.getEmail()")
    public void actualizar(Revisor revisor) {
        em.merge(revisor);
    }

    /**
     * Metodo eliminar. Elimina un usuario
     *
     * @param revisor El revisor a eliminar
     */
    @CacheEvict(value="revisores", key="#revisor.getEmail()")
    public void eliminar(Revisor revisor) {
        em.remove(em.merge(revisor));
    }

    /**
     *
     * @return
     */
    @Cacheable(value="revisores")
    public List<Revisor> listarRevisores() {
        String query = "select h from Revisor h";
        List<Revisor> revisores = em.createQuery(query, Revisor.class).getResultList();
        return revisores;
    }

    /**
     *
     * @return
     */
    public List<Revisor> ordenarPorCargaTrabajo() {
        String query = "select h from Revisor h order by h.cargaDeTrabajo asc";
        List<Revisor> results = em.createQuery(query).getResultList();
        return results;
    }

    /**
     *
     * @param ordenados
     * @param libro
     */
    public void asignarLibroRevisor(List<Revisor> ordenados, Libro libro) {
        Revisor revisor;
        Date currentDate = new Date();
        for (int i = 0; i < 3; i++) {
            revisor = em.merge(ordenados.get(i));
            revisor.setUltFechaAsignacion(currentDate);
            revisor.setCargaDeTrabajo(revisor.getCargadeTrabajo() + 1);
            revisor.anadirLibroRevisor(libro);
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public List<LibroDTO> listarLibrosRevisor(String email) {
        Revisor r1 = (Revisor) buscarEmail(email);

        if (r1.getCargadeTrabajo() == 0) {
            throw new noExistenLibros();
        } else {

            List<Libro> librosRevisar = r1.getPendientesDeRevision();
            LinkedList<LibroDTO> librosDTORevisar = new LinkedList<LibroDTO>();

            for (int i = 0; i < r1.getCargadeTrabajo(); i++) {
                librosDTORevisar.add(librosRevisar.get(i).getLibroDTO());
            }
            return librosDTORevisar;
        }
    }

    /**
     * 
     * @param nota
     * @param email
     * @return 
     */
    public Libro escribirNota(int nota, String email) {
        Revisor r1 = (Revisor) buscarEmail(email);

            List<Libro> lista = r1.getPendientesDeRevision();
            Libro libro_revisar = lista.get(0);
            libro_revisar.AgregarNota(nota);
            lista.remove(libro_revisar);
            r1.setCargaDeTrabajo(r1.getCargadeTrabajo() - 1);

            return libro_revisar;
        }
    }

