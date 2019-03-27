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
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import proyecto.dae.servidor.beans.Sistema;
import proyecto.dae.servidor.excepciones.noExistenLibros;
import proyecto.dae.servidor.servicios.dto.LibroDTO;

/**
 *
 * @author Loren
 */
@RestController
@RequestMapping("/libros")
public class LibreriaRestController {

    @Autowired
    Sistema sistema;

    @RequestMapping(value = "/descargaPorTitulo/{_titulo}", method = GET, produces = "application/json")
    public ResponseEntity<LibroDTO> descargarLibro(@PathVariable String _titulo) {
        LibroDTO libroDTO = null;
        try {
            libroDTO = sistema.descargarLibro(_titulo);
        } catch (noExistenLibros e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(libroDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/consultaPorTematica/{_tematica}", method = GET, produces = "application/json")
    public ResponseEntity<List<LibroDTO>> consultaPorTematica(@PathVariable String _tematica) {
        List<LibroDTO> libroDTO = null;
        try {
            libroDTO = sistema.consultarPorTematica(_tematica);
        } catch (noExistenLibros e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(libroDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/cargaBBDD", method = GET, produces = "application/json")
    public void cargaBBDD() {
        sistema.cargaBBDD();
    }
}