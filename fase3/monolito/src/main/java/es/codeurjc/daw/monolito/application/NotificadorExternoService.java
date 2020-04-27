package es.codeurjc.daw.monolito.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import es.codeurjc.daw.common.ClienteTransaccion;
import reactor.core.publisher.Mono;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Service("notificadorExterno")
public class NotificadorExternoService implements INotificadorService {

    private static final Logger logger = LogManager.getLogger(NotificadorExternoService.class);

    @Value( "${notificador.uri}" )
    private String configuracion;

    private WebClient createWebClient() {
        logger.debug("Cliente creado con la siguiente configuracion, " + configuracion);
        return WebClient.create(configuracion);
    }

    private String getUrlNotificador() {
        return "/api/notificador";
    }

    @Override
    public void notificar(ClienteTransaccion transaccion) {
        
        WebClient webClient = this.createWebClient();

        // Llamada al microservicio notificador via api
		Mono<ClienteTransaccion> monoClienteCreado = webClient
            .put()
            .uri(this.getUrlNotificador())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(transaccion), ClienteTransaccion.class)
            .retrieve()
            .bodyToMono(ClienteTransaccion.class);

        monoClienteCreado.block();

    }

}