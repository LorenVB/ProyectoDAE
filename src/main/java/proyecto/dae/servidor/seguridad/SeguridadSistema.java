package proyecto.dae.servidor.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author Loren
 */

@Configuration
public class SeguridadSistema extends WebSecurityConfigurerAdapter{
    
    @Autowired
    ServicioDatosUsuarioBiblioteca servicioDatosUsuarioBiblioteca;
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        auth.userDetailsService(servicioDatosUsuarioBiblioteca);
        auth.inMemoryAuthentication()
                .withUser("admin").roles("Editor").password("admin");
    }
   
     @Override
     protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable();
        
        httpSecurity.authorizeRequests().antMatchers("/libros/**").anonymous();
        httpSecurity.authorizeRequests().antMatchers("/usuarios/registro").anonymous();
        httpSecurity.httpBasic();
        httpSecurity.authorizeRequests().antMatchers("/usuarios/editor/{email}/**").access("hasRole('Editor') and #email == principal.username");
        httpSecurity.authorizeRequests().antMatchers("/usuarios/autor/{email}/**").access("hasRole('Autor') and #email == principal.username");
        httpSecurity.authorizeRequests().antMatchers("/usuarios/revisor/{email}/**").access("hasRole('Revisor') and #email == principal.username");
        
     }
    
}
