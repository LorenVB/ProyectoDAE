/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import proyecto.dae.servidor.excepciones.noExistenLibros;
import proyecto.dae.servidor.excepciones.rolErroneo;
import proyecto.dae.servidor.excepciones.revisoresInsuficientes;
import proyecto.dae.servidor.excepciones.libroDatosErroneos;
import proyecto.dae.servidor.excepciones.permisoNoAutorizado;
import java.util.ArrayList;
import proyecto.dae.servidor.servicios.IntSistema;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import proyecto.dae.servidor.clases.Autor;
import proyecto.dae.servidor.clases.EstadoRevisionLibro;
import proyecto.dae.servidor.clases.Libro;
import proyecto.dae.servidor.clases.Revisor;
import proyecto.dae.servidor.clases.Usuario;
import proyecto.dae.servidor.daos.AutorDAO;
import proyecto.dae.servidor.daos.LibroDAO;
import proyecto.dae.servidor.daos.RevisorDAO;
import proyecto.dae.servidor.daos.UsuarioDAO;
import proyecto.dae.servidor.excepciones.ISBNIncorrecto;
import proyecto.dae.servidor.excepciones.UsuarioIncorrecto;
import proyecto.dae.servidor.servicios.dto.LibroDTO;
import proyecto.dae.servidor.servicios.dto.UsuarioDTO;

/**
 *
 * @author Miguel y Loren
 */

@Component
public class Sistema implements IntSistema {
    
    @Autowired
    @Qualifier("UsuariosDAO")
    private UsuarioDAO usuariosDAO;

    @Autowired
    @Qualifier("AutoresDAO")
    private AutorDAO autoresDAO;

    @Autowired
    @Qualifier("RevisoresDAO")
    private RevisorDAO revisoresDAO;

    @Autowired
    @Qualifier("LibroDAO")
    private LibroDAO librosDAO;

    public Sistema() {
        
    }

    
    /**
     * Metodo subirLibro
     * @param _libro El libro que queremos subir(_libro.getISBN(), _
     * @param u 
     */
    @Override
    public void subirLibro(LibroDTO _libro,Usuario u) {
           if(_libro==null){
              throw new libroDatosErroneos();
           }
           Libro libro = new Libro(_libro.getISBN(),_libro.getTematica(), _libro.getTitulo(),_libro.getTematica());
           if (libro.getISBN().equals("") || libro.getTitulo().equals("") || libro.getTematica().equals("")) {
                throw new libroDatosErroneos();
            } else {
                Autor autor = (Autor) u;
                librosDAO.insertar(libro);
                libro.setAutor(autor.getNombre());
                libro.setPendiente(Boolean.TRUE);
                librosDAO.actualizar(libro);
                autor.AnadirLibro(libro);
           }
    }

    /**
     * Metodo obtenerLibrosPendientesRevision. El editor obtiene una lista
     * de libros pendientes de aceptacion 
     * @param u Token unico para comprobar identidad de usuario
     * @return Devuelve una lista de libros pendientes de revision
     */
    @Override
    public List<LibroDTO> obtenerLibrosPendientesRevision(Usuario u) {
            List<Libro> librosPendientes = librosDAO.obtenerListaPendientes();
            if (librosPendientes.isEmpty()) {
                throw new noExistenLibros();
            } else {
                List<LibroDTO> pendientes = new ArrayList<>();
                for (int i = 0; i < librosPendientes.size(); i++) {
                    pendientes.add(librosPendientes.get(i).getLibroDTO());
                }
                return pendientes;
        }
    }

    /**
     * Metodo aceptarLibro. El editor decide si el libro se acepta o no
     * @param ISBN Token unico para comprobar identidad de usuario
     */
    @Override
    public void aceptarLibro(String ISBN){
            Libro libro = librosDAO.buscarISBN(ISBN);
            libro.setPendiente(Boolean.FALSE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateInString = "2017-12-27";
            Date fecha=null;
            try {
                fecha=sdf.parse(dateInString);
            } catch (ParseException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
            }
            libro.setFechaRevisionAsignada(fecha);
            librosDAO.actualizar(libro);
            asignarLibroRevisores(libro);
        
    }

    /**
     * Metodo rechazarLibro. El editor decide si el libro de rechaza o no
     * @param ISBN Token unico para comprobar identidad de usuario
     */
    @Override
    public void rechazarLibro(String ISBN) {
        Libro libro = librosDAO.buscarISBN(ISBN);
        libro.setPendiente(Boolean.FALSE);
        librosDAO.actualizar(libro);
        }
    
    

    /**
     * Metodo privado del sistema. asignarLibroRevisores
     * @param libro El libro a asignar
     */
    private void asignarLibroRevisores(Libro libro) {
        List<Revisor> revisores = revisoresDAO.listarRevisores();
        if (revisores.isEmpty() || revisores.size() < 3) {
            throw new revisoresInsuficientes();
        } else {
            List<Revisor> revisoresOrdenados = revisoresDAO.ordenarPorCargaTrabajo();
            revisoresDAO.asignarLibroRevisor(revisoresOrdenados, libro);
            Libro libroEnRevision = librosDAO.buscarISBN(libro.getISBN());
            libroEnRevision.setEnRevision(Boolean.TRUE);
            librosDAO.actualizar(libroEnRevision);
        }
    }

    /**
     * Metodo verListaPendientesRevisar. Lista de libros que tiene asignados
     * un revisor
     * @param email Token unico para comprobar identidad de usuario
     * @return Devuelve una lista de libros asignados a un revisor
     */
    @Override
    public List<LibroDTO> verListaPendientesRevisar(String email) {
        Usuario nuevo = usuariosDAO.buscarEmail(email);
        List<LibroDTO> libros = revisoresDAO.listarLibrosRevisor(email);
        return libros;
        }

    /**
     * Metodo asignarNota
     * @param nota Nota a asignar a un libro
     * @param email Email de usuario
     */
    @Override
    public void asignarNota(int nota, String email) {
        Usuario nuevo = usuariosDAO.buscarEmail(email);
        Libro libro_revisar = revisoresDAO.escribirNota(nota, email);
            
        //Comprobamos si a ese libro lo han revisado ya ( Se le han asignado sus tres notas)
            if (libro_revisar.getRevisado()) {
                publicarLibro(libro_revisar);
            }
            librosDAO.actualizar(libro_revisar);
        }
    

    /**
     * Metodo privado del sistema. Publicar libro
     * @param libro Libro que queremos publicar
     */
    private void publicarLibro(Libro libro) {
        Boolean publicable = libro.CalcularNota();
            if (publicable) {
                libro.setPublicado(Boolean.TRUE);
                Date fecha = new Date();
                libro.setFechaPublicacion(fecha);
            }
    }

    /**
     * Metodo registrarUsuario
     * @param nombre Nombre del usuario
     * @param edad Edad del usuario
     * @param email Email del usuario
     * @param password Constraseña del usuario
     * @param rol Rol del usuario (Autor o Revisor)
     */
    @Override
    public void registrarUsuario(String nombre, int edad, String email, String password, String rol) {
        switch (rol) {
            case "Autor":
                Autor autor = null;
                autor = new Autor(edad, email, nombre, password, rol, null, null);
                autoresDAO.insertar(autor);
                break;
            case "Revisor":
                Revisor revisor = null;
                revisor = new Revisor(edad, email, nombre, password, rol, null, null, null, 0);
                revisoresDAO.insertar(revisor);
                break;
            default:
                throw new rolErroneo();
        }
    }

    /**
     * Metodo descargarLibro. 
     * @param _titulo El titulo del libro a descargar
     * @return El libro que queremos descargar
     */
    @Override
    public LibroDTO descargarLibro(String _titulo) {
        Libro buscado = librosDAO.buscarTitulo(_titulo);
        
        if(buscado==null){
            throw new noExistenLibros();
        }
        if (buscado.getPublicado() == false) {
            throw new noExistenLibros();
        }else{
            LibroDTO libroDTO = null;
            libroDTO = buscado.getLibroDTO();
            return libroDTO;
        }
    }

    /**
     * Metodo consultarPorTematica.
     * @param tematica La tematica que queremos consultar
     * @return Una lista de libros con la tematica descrita
     */
    @Override
    public List<LibroDTO> consultarPorTematica(String tematica) {
        List<Libro> buscados = librosDAO.buscarTematica(tematica);
        List<LibroDTO> buscadosDTO= new LinkedList<>();
        
        if (buscados.isEmpty()) {
            throw new noExistenLibros();
        } else {
            for(int i = 0; i < buscados.size() ;i++){
                if(buscados.get(i).getPublicado() == true){
                    buscadosDTO.add(buscados.get(i).getLibroDTO());
                }
            }
            
        }
        return buscadosDTO;
    }

    /**
     * Metodo devuelveUsuario
     * @param email El email del usuario 
     * @return El usuario con el email que queremos buscar
     */
    @Override
    public UsuarioDTO devuelveUsuario(String email) {
        Usuario aux = usuariosDAO.buscarEmail(email);
        if (aux == null) {
            throw new UsuarioIncorrecto();
        } else {
            UsuarioDTO usuario = new UsuarioDTO(aux.getNombre(), aux.getEdad(), aux.getEmail(), aux.getPassword(), aux.getRol());
            return usuario;
        }
    }
    
        public void cargaBBDD() {
        //EDITOR
        Usuario usuario1 = new Usuario(25, "jefe@gmail", "Antonio", "1234", "Editor", null);
        usuariosDAO.insertar(usuario1);
        
        //AUTORES
        Autor autor1 = new Autor(25, "miguel@gmail", "miguel", "1234", "Autor", null, null);
        Autor autor2 = new Autor(21, "lorenzo@gmail", "lorenzo", "1234", "Autor", null, null);
        autoresDAO.insertar(autor1);
        autoresDAO.insertar(autor2);

        //REVISORES
        Revisor revisor1 = new Revisor(25, "revisor1@gmail", "Antonio", "1234", "Revisor", null, null, null, 0);
        Revisor revisor2 = new Revisor(21, "revisor2@gmail", "Fernando", "1234", "Revisor", null, null, null, 0);
        Revisor revisor3 = new Revisor(33, "revisor3@gmail", "Julio Jesus", "1234", "Revisor", null, null, null, 0);
        Revisor revisor4 = new Revisor(29, "revisor4@gmail", "Alberto", "1234", "Revisor", null, null, null, 0);
        Revisor revisor5 = new Revisor(23, "revisor5@gmail", "Rodolfo", "1234", "Revisor", null, null, null, 0);
        Revisor revisor6 = new Revisor(23, "revisor6@gmail", "Fernando", "1234", "Revisor", null, null, null, 0);
        
        revisoresDAO.insertar(revisor1);
        revisoresDAO.insertar(revisor2);
        revisoresDAO.insertar(revisor3);
        revisoresDAO.insertar(revisor4);
        revisoresDAO.insertar(revisor5);
        revisoresDAO.insertar(revisor6);
        
        //LIBROS
        Libro libro1 = new Libro("1000", "autor1", false, null ,null, null, false, true, true, "terror", "harry1");        
        Libro libro2 = new Libro("1001","yo",false,null,null,null,false, true,true,"terror","harry2");        
        Libro libro3 = new Libro("1002", "autor1", false, null ,null, null, true, false, false, "accion", "deadpool");        
        Libro libro4 = new Libro("1003", "autor3", false, null,null, null, true, false, false, "suspense", "alien 3");        
        Libro libro5 = new Libro("1004", "autor4", false, null ,null, null, true, false, false, "terror", "harry3");
       
        librosDAO.insertar(libro1);
        librosDAO.insertar(libro2);
        librosDAO.insertar(libro3);
        librosDAO.insertar(libro4);
        librosDAO.insertar(libro5);
    }
}
