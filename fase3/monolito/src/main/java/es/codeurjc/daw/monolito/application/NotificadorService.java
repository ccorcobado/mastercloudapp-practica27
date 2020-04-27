package es.codeurjc.daw.monolito.application;

import org.springframework.stereotype.Service;

import es.codeurjc.daw.common.ClienteTransaccion;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Service
public class NotificadorService implements INotificadorService {

    private static final Logger logger = LogManager.getLogger(NotificadorService.class);

    public void notificar(ClienteTransaccion transaccion) {

        String mensaje = "\n********* Envio de notificacion al cliente *********" + 
                         "\n" + transaccion.toString() +
                         "\n*****************************************";
                         
        logger.trace(mensaje);
    }
}