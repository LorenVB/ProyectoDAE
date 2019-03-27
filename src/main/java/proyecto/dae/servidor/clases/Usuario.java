/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.clases;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


/**
 *
 * @author Miguel y Loren
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Usuario implements Serializable {
   
    
   
    private String nombre;
    private Integer edad;
    @Id
    private String email;
    private String password; 
    protected String rol;

 
    /**
     * Constructor vacío de Usuario
     */
    public Usuario() {
        nombre ="";
        edad = 0;
        email="";
        password="";
        rol="";
    }
    /**
     * 
     * @param _edad
     * @param _email
     * @param _nombre
     * @param _password
     * @param _rol
     * @param _token 
     */
    public Usuario(int _edad, String _email, String _nombre, String _password, String _rol, String _token){
        edad = _edad;
        email = _email;
        nombre = _nombre;
        password = _password;
        rol = _rol;
    }
   
    /**
     * Constructor por defecto de usuario
     * @param _nombre Nombre del usuario
     * @param _edad Edad del usuario
     * @param _email Email del usuario
     * @param _password Password del usuario
     * @param _rol Rol del usuario
     */
    public Usuario(String _nombre, Integer _edad, String _email, String _password,String _rol){
        nombre = _nombre;
        edad = _edad;
        email = _email;
        password = _password;
        rol = _rol;
    }


    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * @return the edad
     */
    public Integer getEdad() {
        return edad;
    }
    
        /**
     * @return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    /**
     * @param edad the edad to set
     */
    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}