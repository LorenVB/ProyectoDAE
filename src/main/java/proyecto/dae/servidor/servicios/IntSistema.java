/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.servicios;

import java.util.List;
import proyecto.dae.servidor.clases.EstadoRevisionLibro;
import proyecto.dae.servidor.clases.Usuario;
import proyecto.dae.servidor.servicios.dto.LibroDTO;
import proyecto.dae.servidor.servicios.dto.UsuarioDTO;

/**
 *
 * @author Miguel y Loren
 */
public interface IntSistema {
    public void subirLibro(LibroDTO _libro, Usuario u); // HECHO
    public List<LibroDTO> obtenerLibrosPendientesRevision(Usuario u); 
    public void aceptarLibro(String ISBN); 
    public void rechazarLibro(String ISBN);
    public List<LibroDTO> verListaPendientesRevisar(String email);  
    public void asignarNota(int nota, String email); 
    public void registrarUsuario(String nombre, int edad, String email, String password, String rol);
    public LibroDTO descargarLibro(String _titulo);  
    public List<LibroDTO> consultarPorTematica(String tematica); 
    public UsuarioDTO devuelveUsuario(String email); 
    
}
