package es.codeurjc.daw.microserviciopedido.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.microserviciopedido.domain.Pedido;
import es.codeurjc.daw.microserviciopedido.domain.PedidoId;

public interface PedidoRepository extends JpaRepository<Pedido, PedidoId> {

}