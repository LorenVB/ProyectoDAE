/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor.clases;

import java.io.Serializable;

/**
 *
 * @author Miguel y Loren
 */

public class Editor extends Usuario implements Serializable{
    
    /**
     * Constructor Editor
     * @param _nombre Nombre del editor
     * @param _edad Edad del editor
     * @param _email Email del editor
     * @param _password Password del editor
     * @param _rol Rol del editor
     */
    public Editor(int _edad, String _email, String _nombre, String _password, String _rol, String _token){
        super(_nombre,_edad,_email,_password,_rol);
    }
}
