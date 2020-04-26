package es.codeurjc.daw.microserviciopedido.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import es.codeurjc.daw.common.ClienteBase;
import es.codeurjc.daw.common.ClienteTransaccion;
import es.codeurjc.daw.common.ProductoBase;
import es.codeurjc.daw.common.ProductoTransaccion;
import reactor.core.publisher.Mono;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Component
public class WebClientMonolito {

    private static final Logger logger = LogManager.getLogger(WebClientMonolito.class);

    @Value( "${monolito.uri}" )
    private String configuracion;
    
    private WebClient createWebClient() {
        logger.debug("Cliente creado con la siguiente configuracion, " + configuracion);
        return WebClient.create(configuracion);
    }

    private String getUrlCliente() {
        return "/api/cliente";
    }

    private String getUrlCliente(String idCliente) {
        return String.format("/api/cliente/%s", idCliente);
    }

    public ClienteBase leerCliente(String clienteId) {

        WebClient webClient = this.createWebClient();

        logger.debug("Llamada a, " + this.getUrlCliente(clienteId));
		// Lectura de cliente
		Mono<ClienteBase> monoClienteEncontrado = webClient.get()
			.uri(this.getUrlCliente(clienteId))
			.retrieve()
			.bodyToMono(ClienteBase.class);

        try {
            // Leemos cliente
            ClienteBase clienteEncontrado = monoClienteEncontrado.block();

            return clienteEncontrado;
        } catch (Exception e) {
            return null;
        }
        
    }
    
    private String getUrlProducto() {
        return "/api/producto";
    }

    private String getUrlProducto(String idProducto) {
        return String.format("/api/producto/%s", idProducto);
    }

    public ProductoBase leerProducto(String productoId) {

        WebClient webClient = this.createWebClient();
        
        logger.debug("Llamada a, " + this.getUrlProducto(productoId));
		// Lectura de producto
		Mono<ProductoBase> monoProductoEncontrado = webClient.get()
			.uri(this.getUrlProducto(productoId))
			.retrieve()
			.bodyToMono(ProductoBase.class);

        try {
            // Leemos producto
            ProductoBase productoEncontrado = monoProductoEncontrado.block();

            return productoEncontrado;
        } catch (Exception e) {
            return null;
        }
	}

    public ClienteBase realizarTransaccion(ClienteTransaccion clienteTransaccion) {
        
        WebClient webClient = this.createWebClient();

        logger.debug("Llamada a, " + this.getUrlCliente());
        // Transaccion de cliente
		Mono<ClienteBase> monoClienteCreado = webClient
            .put()
            .uri(this.getUrlCliente())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(clienteTransaccion), ClienteTransaccion.class)
            .retrieve()
            .bodyToMono(ClienteBase.class);

        try {
            ClienteBase clienteCreado = monoClienteCreado.block();

            return clienteCreado;
        } catch (Exception e) {
            return null;
        }
        
    }

    public ProductoBase realizarTransaccion(ProductoTransaccion productoTransaccion) {
        
        WebClient webClient = this.createWebClient();
        
        logger.debug("Llamada a, " + this.getUrlProducto());
        // Transaccion de producto
		Mono<ProductoBase> monoProductoCreado = webClient
            .put()
            .uri(this.getUrlProducto())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(productoTransaccion), ProductoTransaccion.class)
            .retrieve()
            .bodyToMono(ProductoBase.class);

        try {
            ProductoBase productoCreado = monoProductoCreado.block();

            return productoCreado;
        } catch (Exception e) {
            return null;
        }
        
    }
}