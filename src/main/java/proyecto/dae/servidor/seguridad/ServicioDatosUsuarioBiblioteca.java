/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import proyecto.dae.servidor.clases.Usuario;
import proyecto.dae.servidor.daos.UsuarioDAO;
import proyecto.dae.servidor.excepciones.ParametrosIncorrectos;

/**
 *
 * @author Miguel
 */

@Component
public class ServicioDatosUsuarioBiblioteca implements UserDetailsService {
    
    @Autowired
    UsuarioDAO usuarios;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(email.equals("") || email==null){
            throw new ParametrosIncorrectos();
        }
        
        Usuario usuario = usuarios.buscarEmail(email);
        String rol = usuario.getRol();
        
        return User.withUsername(email).password(usuario.getPassword()).roles(rol).build();        
    }
}
