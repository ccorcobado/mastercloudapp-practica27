package es.codeurjc.daw.monolito.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.monolito.application.dto.ProductoCompletoOutput;
import es.codeurjc.daw.monolito.domain.Producto;
import es.codeurjc.daw.monolito.domain.ProductoId;
import es.codeurjc.daw.monolito.infrastructure.ProductoRepository;

@Service
public class ProductoQueryService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ModelMapper modelMapperQuery;

	public List<ProductoCompletoOutput> getAll() {
        
        List<ProductoCompletoOutput> allProductoDto = new ArrayList<>();
        
        List<Producto> allProductos = this.productoRepository.findAll();

        for (Producto producto : allProductos) {
            allProductoDto.add(this.convertEntityToDto(producto));
        }

        return allProductoDto;
	}

	public ProductoCompletoOutput get(String productoId) {
        
        Optional<Producto> producto = this.productoRepository.findById(new ProductoId(productoId));

        if (!producto.isPresent())
            return null;

		return this.convertEntityToDto(producto.get());
    }
    
    private ProductoCompletoOutput convertEntityToDto(Producto producto) {
		return modelMapperQuery.map(producto, ProductoCompletoOutput.class);
	}

}