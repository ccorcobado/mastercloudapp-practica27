package es.codeurjc.daw.monolito.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.monolito.application.dto.PedidoBase;
import es.codeurjc.daw.monolito.application.dto.PedidoInput;
import es.codeurjc.daw.monolito.domain.ClienteId;
import es.codeurjc.daw.monolito.domain.Pedido;
import es.codeurjc.daw.monolito.domain.PedidoEstado;
import es.codeurjc.daw.monolito.application.saga.PedidoSaga;
import es.codeurjc.daw.monolito.domain.ProductoId;
import es.codeurjc.daw.monolito.infrastructure.ClienteRepository;
import es.codeurjc.daw.monolito.infrastructure.PedidoRepository;
import es.codeurjc.daw.monolito.infrastructure.ProductoRepository;

@Service
public class PedidoCommandService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteCommandService clienteCommandService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoCommandService productoCommandService;

    @Autowired
    private ModelMapper modelMapperCommand;

	public PedidoBase commandAltaPedido(PedidoInput entrada) {

        if (entrada == null)
            throw new IllegalArgumentException("La entrada esta vacia");

		Pedido pedido = new Pedido();
		pedido.setProductoId(new ProductoId(entrada.getProductoId()));
		pedido.setClienteId(new ClienteId(entrada.getClienteId()));
        pedido.setCantidad(entrada.getCantidad());
        pedido.setEstado(PedidoEstado.EN_PROCESO);

        pedido = this.pedidoRepository.save(pedido);

        PedidoSaga saga = new PedidoSaga();
        saga.pedidoId = pedido.getId();
        saga.clienteRepository = this.clienteRepository;
        saga.pedidoRepository = this.pedidoRepository;
        saga.productoRepository = this.productoRepository;
        saga.clienteCommandService = clienteCommandService;
        saga.productoCommandService = productoCommandService;
        saga.start();

		return convertEntityToDto(pedido);
    }
    
    private PedidoBase convertEntityToDto(Pedido pedido) {
		return modelMapperCommand.map(pedido, PedidoBase.class);
	}

}