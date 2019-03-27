/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.RestController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;
import proyecto.dae.servidor.beans.Sistema;
import proyecto.dae.servidor.clases.EstadoRevisionLibro;
import proyecto.dae.servidor.clases.Usuario;
import proyecto.dae.servidor.daos.UsuarioDAO;
import proyecto.dae.servidor.excepciones.ErrorCreacionAutor;
import proyecto.dae.servidor.excepciones.ErrorCreacionLibro;
import proyecto.dae.servidor.excepciones.ErrorCreacionRevisor;
import proyecto.dae.servidor.excepciones.ISBNIncorrecto;
import proyecto.dae.servidor.excepciones.NotaNoValida;
import proyecto.dae.servidor.excepciones.ParametrosIncorrectos;
import proyecto.dae.servidor.excepciones.UsuarioIncorrecto;
import proyecto.dae.servidor.excepciones.libroDatosErroneos;
import proyecto.dae.servidor.excepciones.noExistenLibros;
import proyecto.dae.servidor.excepciones.revisoresInsuficientes;
import proyecto.dae.servidor.seguridad.SeguridadSistema;
import proyecto.dae.servidor.servicios.dto.LibroDTO;
import proyecto.dae.servidor.servicios.dto.UsuarioDTO;

/**
 *
 * @author Loren
 */

@RestController
@RequestMapping("/usuarios")
public class UsuariosRestController {
    @Autowired
    Sistema sistema;
    
    @Autowired
    UsuarioDAO usuarios;
    
    @Autowired
    SeguridadSistema seguridad;
    
    /**
     * 
     * @param usuario
     * @return 
     */
    @RequestMapping(value="/registro",method=POST, produces="application/json")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody UsuarioDTO usuario) {
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            sistema.registrarUsuario(usuario.getNombre(), usuario.getEdad(), usuario.getEmail(), usuario.getPassword(), usuario.getRol());
        } catch (ErrorCreacionAutor a) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (ErrorCreacionRevisor r) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(usuario,HttpStatus.OK);
    }
    
    /**
     * 
     * @param email
     * @return 
     */
    @RequestMapping(value = "/login/{email}", method = GET, produces = "application/json")
    public ResponseEntity<UsuarioDTO> logIn(@PathVariable String email) {
        UsuarioDTO user = null;
        try {
            user = sistema.devuelveUsuario(email);
        } catch (UsuarioIncorrecto u) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 
     * @param libro
     * @param email
     * @param ISBN
     * @return
     * @throws libroDatosErroneos 
     */
    @RequestMapping(value = "/autor/{email}/libro/{ISBN}", method = POST, produces = "application/json")
    public ResponseEntity<LibroDTO> subirLibro(@RequestBody LibroDTO libro, @PathVariable String email,
                           @PathVariable String ISBN) throws libroDatosErroneos {

        Usuario u = usuarios.buscarEmail(email);

        try {
            sistema.subirLibro(libro, u);
        } catch (libroDatosErroneos e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ErrorCreacionLibro a){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(libro, HttpStatus.OK);

    }

    /**
     * 
     * @param email
     * @return 
     */
    @RequestMapping(value = "/editor/{email}/librosPendientes", method = GET, produces = "application/json")
    public ResponseEntity<List<LibroDTO> > obtenerLibrosPendientes(@PathVariable String email) {
    
        Usuario u = usuarios.buscarEmail(email);
        List<LibroDTO> librosPendientes = null;
        try {
            librosPendientes = sistema.obtenerLibrosPendientesRevision(u);
        } catch (noExistenLibros e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(librosPendientes, HttpStatus.OK);
    }

    /**
     * 
     * @param email
     * @param ISBN
     * @return 
     */
    @RequestMapping(value = "/editor/{email}/libro/{ISBN}/aceptado", method = GET, produces = "application/json")
    public ResponseEntity<Void> aceptarLibro(@PathVariable String email, @PathVariable String ISBN) {
        Usuario u = usuarios.buscarEmail(email);

        try {
            sistema.aceptarLibro(ISBN);
        } catch (revisoresInsuficientes r) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 
     * @param email
     * @param ISBN 
     */
    @RequestMapping(value = "/editor/{email}/libro/{ISBN}/rechazado", method = GET, produces = "application/json")
    public ResponseEntity<Void> rechazarLibro(@PathVariable String email, @PathVariable String ISBN) {
        Usuario u = usuarios.buscarEmail(email);
        sistema.rechazarLibro(ISBN);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/revisor/{email}/libros/listadoRevision", method = GET, produces = "application/json")
    public ResponseEntity< List<LibroDTO> > verListaPendientesRevisar(@PathVariable String email) {
       
        Usuario u = usuarios.buscarEmail(email);
        List<LibroDTO> libPendRevisar = null;
        try {
            libPendRevisar = sistema.verListaPendientesRevisar(email);
        } catch (noExistenLibros e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(libPendRevisar, HttpStatus.OK);
    }

 
    @RequestMapping(value = "/revisor/{email}/libros/{ISBN}/nota/{nota}", method = GET, produces = "application/json")
    public ResponseEntity<Void> asignarNota(@PathVariable String email, @PathVariable String ISBN,@PathVariable int nota) throws libroDatosErroneos {
        
        Usuario u = usuarios.buscarEmail(email);
        
        if(nota < 0 || nota > 5){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        sistema.asignarNota(nota, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    
    @RequestMapping(value = "/devolverUsuario/{email}/", method = GET, produces = "application/json")
    public ResponseEntity<Usuario> devuelveUsuario(@PathVariable String email) {
        if (email.equals("") || email == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Usuario u = usuarios.buscarEmail(email);

        if (u == null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(u,HttpStatus.OK);
    }
 
}