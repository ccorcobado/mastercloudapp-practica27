package es.codeurjc.daw.monolito.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.monolito.application.dto.PedidoBase;
import es.codeurjc.daw.monolito.application.dto.PedidoInput;
import es.codeurjc.daw.monolito.domain.ClienteId;
import es.codeurjc.daw.monolito.domain.Pedido;
import es.codeurjc.daw.monolito.domain.ProductoId;
import es.codeurjc.daw.monolito.infrastructure.PedidoRepository;

@Service
public class PedidoCommandService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ModelMapper modelMapperCommand;
    
	public PedidoBase commandAltaPedido(PedidoInput entrada) {

        if (entrada == null)
            throw new IllegalArgumentException("La entrada esta vacia");

// TODO: Falta crear la saga aqui

		Pedido pedido = new Pedido();
		pedido.setProductoId(new ProductoId(entrada.getProductoId()));
		pedido.setClienteId(new ClienteId(entrada.getClienteId()));
		pedido.setCantidad(entrada.getCantidad());

		pedido = this.pedidoRepository.save(pedido);

		return convertEntityToDto(pedido);
    }
    
    private PedidoBase convertEntityToDto(Pedido pedido) {
		return modelMapperCommand.map(pedido, PedidoBase.class);
	}

}