package es.codeurjc.daw.microserviciopedido.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.common.PedidoBase;
import es.codeurjc.daw.microserviciopedido.domain.Pedido;
import es.codeurjc.daw.microserviciopedido.domain.PedidoId;
import es.codeurjc.daw.microserviciopedido.infrastructure.PedidoRepository;

@Service
public class PedidoQueryService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ModelMapper modelMapperQuery;

	public List<PedidoBase> getAll() {
        
        List<PedidoBase> allPedidoDto = new ArrayList<>();
        
        List<Pedido> allPedidos = this.pedidoRepository.findAll();

        for (Pedido pedido : allPedidos) {
            allPedidoDto.add(this.convertEntityToDto(pedido));
        }

        return allPedidoDto;
	}

	public PedidoBase get(String pedidoId) {
        
        Optional<Pedido> pedido = this.pedidoRepository.findById(new PedidoId(pedidoId));

        if (!pedido.isPresent())
            return null;

		return this.convertEntityToDto(pedido.get());
    }
    
    private PedidoBase convertEntityToDto(Pedido pedido) {
		return modelMapperQuery.map(pedido, PedidoBase.class);
	}
}