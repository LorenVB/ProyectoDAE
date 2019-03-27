/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.dae.servidor;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import proyecto.dae.servidor.beans.Sistema;


/**
 *
 * @author Miguel y Loren
 */
@SpringBootApplication
public class ServidorImpl {
   
    public static void main(String[] args) throws Exception {
        SpringApplication servidor = new SpringApplication(ServidorImpl.class);
        servidor.run(args);

    }
}
