package es.codeurjc.daw.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/api/pedido/**")
                        .uri("http://localhost:8082/")
                        .id("pedidoModule"))

                .route(r -> r.path("/api/cliente/**")
                        .uri("http://localhost:8081/")
                        .id("clienteModule"))

                .route(r -> r.path("/api/producto/**")
                        .uri("http://localhost:8081/")
                        .id("productoModule"))
                .build();
                
    }
}