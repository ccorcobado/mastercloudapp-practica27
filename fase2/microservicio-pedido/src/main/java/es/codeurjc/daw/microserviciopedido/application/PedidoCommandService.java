package es.codeurjc.daw.microserviciopedido.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.common.PedidoBase;
import es.codeurjc.daw.common.PedidoInput;
import es.codeurjc.daw.microserviciopedido.domain.ClienteId;
import es.codeurjc.daw.microserviciopedido.domain.Pedido;
import es.codeurjc.daw.microserviciopedido.domain.PedidoEstado;
import es.codeurjc.daw.microserviciopedido.domain.ProductoId;
import es.codeurjc.daw.microserviciopedido.infrastructure.PedidoRepository;
import es.codeurjc.daw.microserviciopedido.infrastructure.WebClientMonolito;

@Service
public class PedidoCommandService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private WebClientMonolito webClient;

    @Autowired
    private ModelMapper modelMapperCommand;

	public PedidoBase commandAltaPedido(PedidoInput entrada) {

        if (entrada == null)
            throw new IllegalArgumentException("La entrada esta vacia");

        // creamos pedido con estado en proceso
		Pedido pedido = new Pedido();
		pedido.setProductoId(new ProductoId(entrada.getProductoId()));
		pedido.setClienteId(new ClienteId(entrada.getClienteId()));
        pedido.setCantidad(entrada.getCantidad());
        pedido.setEstado(PedidoEstado.EN_PROCESO);

        pedido = this.pedidoRepository.save(pedido);

        // Lanzamos la saga del proceso del pedido en modo asincrono
        SagaPedidoCommandService transaccionAsync = 
            new SagaPedidoCommandService(pedido.getId(), this.pedidoRepository, webClient);
        transaccionAsync.start();

		return convertEntityToDto(pedido);
    }
    
    private PedidoBase convertEntityToDto(Pedido pedido) {
		return modelMapperCommand.map(pedido, PedidoBase.class);
	}

}