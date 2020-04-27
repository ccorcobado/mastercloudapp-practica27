package es.codeurjc.daw.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Value( "${monolito.uri}" )
    private String monolitoUri;

    @Value( "${monolito.port}" )
    private String monolitoPort;

    @Value( "${mspedido.uri}" )
    private String mspedidoUri;

    @Value( "${mspedido.port}" )
    private String mspedidoPort;
    
    @Value( "${msnotificador.uri}" )
    private String msnotificadorUri;

    @Value( "${msnotificador.port}" )
    private String msnotificadorPort;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/api/pedido/**")
                        .uri(String.format("http://%s:%s/", mspedidoUri, mspedidoPort))
                        .id("pedidoModule"))

                .route(r -> r.path("/api/cliente/**")
                        .uri(String.format("http://%s:%s/", monolitoUri, monolitoPort))
                        .id("clienteModule"))

                .route(r -> r.path("/api/producto/**")
                        .uri(String.format("http://%s:%s/", monolitoUri, monolitoPort))
                        .id("productoModule"))

                .route(r -> r.path("/api/notificador/**")
                        .uri(String.format("http://%s:%s/", msnotificadorUri, msnotificadorPort))
                        .id("productoModule"))

                .build();
                
    }
}