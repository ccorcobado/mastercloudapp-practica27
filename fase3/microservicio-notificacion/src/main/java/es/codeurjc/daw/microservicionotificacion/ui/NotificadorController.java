package es.codeurjc.daw.microservicionotificacion.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.common.ClienteTransaccion;

@RestController
@RequestMapping("/api/notificador")
public class NotificadorController {

    private static final Logger logger = LogManager.getLogger(NotificadorController.class);

    @PostMapping("")
	public ResponseEntity<?> launch(@RequestBody ClienteTransaccion transaccion) {

        logger.trace("Inicio notificacion ");

        try {
            
            String mensaje = "\n********* Envio de notificacion al cliente *********" + 
                             "\n" + transaccion.toString() +
                             "\n*****************************************";
                         
            logger.trace(mensaje);

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(e);

            return new ResponseEntity<>("Ups! Algo fue mal :S \n" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}