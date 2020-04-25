package es.codeurjc.daw.monolito.application;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.monolito.application.dto.ProductoBase;
import es.codeurjc.daw.monolito.application.dto.ProductoInput;
import es.codeurjc.daw.monolito.application.dto.ProductoTransaccion;
import es.codeurjc.daw.monolito.domain.Producto;
import es.codeurjc.daw.monolito.domain.ProductoId;
import es.codeurjc.daw.monolito.infrastructure.ProductoRepository;

@Service
public class ProductoCommandService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ModelMapper modelMapperCommand;

	public ProductoBase commandAltaProducto(ProductoInput entrada) {
        
        if (entrada == null)
            throw new IllegalArgumentException("La entrada esta vacia");

		Producto producto = new Producto();
		producto.setNombre(entrada.getNombre());
		producto.setPrecio(entrada.getPrecio());
        producto.setStock(entrada.getStock());

		producto = this.productoRepository.save(producto);

		return convertEntityToDto(producto);
	}

	public ProductoBase commandRealizarTransaccion(ProductoTransaccion entrada) {
        
        if (entrada == null)
			throw new IllegalArgumentException("La entrada esta vacia");
			
		Optional<Producto> optionalProducto = this.productoRepository.findById(new ProductoId(entrada.getProductoId()));

		if (!optionalProducto.isPresent())
			throw new IllegalArgumentException("Identificador de producto no valido");
			
		switch (entrada.getTransaccion()) {

			case INGRESO:
				optionalProducto.get().sumarStock(entrada.getStock());
				break;

			case RETIRADA:
				optionalProducto.get().restarStock(entrada.getStock());	
				break;
				
			default:
				break;
		}

		Producto producto = this.productoRepository.save(optionalProducto.get());

		return convertEntityToDto(producto);
    }
    
    private ProductoBase convertEntityToDto(Producto producto) {
		return modelMapperCommand.map(producto, ProductoBase.class);
	}

}