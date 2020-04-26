package es.codeurjc.daw.monolito.application;

import org.springframework.stereotype.Service;

import es.codeurjc.daw.monolito.application.dto.ClienteTransaccion;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Service
public class NotificadorService {

    private static final Logger logger = LogManager.getLogger(NotificadorService.class);

    public void notificar(ClienteTransaccion transaccion) {

        logger.trace("********* Envio de notificacion *********");
        logger.trace(transaccion.toString());
        logger.trace("*****************************************");
    }
}