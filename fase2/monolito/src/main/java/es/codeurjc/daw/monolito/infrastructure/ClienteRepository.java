package es.codeurjc.daw.monolito.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.monolito.domain.Cliente;
import es.codeurjc.daw.monolito.domain.ClienteId;

public interface ClienteRepository extends JpaRepository<Cliente, ClienteId> {

}