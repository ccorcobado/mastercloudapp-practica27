package es.codeurjc.daw.monolito.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.monolito.domain.Producto;
import es.codeurjc.daw.monolito.domain.ProductoId;

public interface ProductoRepository extends JpaRepository<Producto, ProductoId> {

}