package es.codeurjc.daw.monolito.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.monolito.domain.Pedido;
import es.codeurjc.daw.monolito.domain.PedidoId;

public interface PedidoRepository extends JpaRepository<Pedido, PedidoId> {

}