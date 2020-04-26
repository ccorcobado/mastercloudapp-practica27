package es.codeurjc.daw.monolito.application;

import java.math.BigDecimal;
import java.util.Optional;

import es.codeurjc.daw.common.ClienteTransaccion;
import es.codeurjc.daw.common.ProductoTransaccion;
import es.codeurjc.daw.common.TipoTransaccion;
import es.codeurjc.daw.monolito.domain.*;
import es.codeurjc.daw.monolito.infrastructure.ClienteRepository;
import es.codeurjc.daw.monolito.infrastructure.PedidoRepository;
import es.codeurjc.daw.monolito.infrastructure.ProductoRepository;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class TransaccionPedidoCommandService extends Thread {

    private static final Logger logger = LogManager.getLogger(TransaccionPedidoCommandService.class);

    public PedidoId pedidoId;
    public ClienteRepository clienteRepository;
    public ClienteCommandService clienteCommandService;
    public ProductoRepository productoRepository;
    public ProductoCommandService productoCommandService;
    public PedidoRepository pedidoRepository;    

    private boolean step1(Pedido pedido) {

        // PASO 1 - Comprobar existencia de cliente
        logger.debug("PASO 1 - Comprobar existencia de cliente");
        Optional<Cliente> cliente = this.clienteRepository.findById(pedido.getClienteId());

        if (!cliente.isPresent()) {

            pedido.setEstado(PedidoEstado.RECHAZADO);
            pedido.setMotivoRechazo(PedidoRechazo.CLIENTE_DESCONOCIDO);
            this.pedidoRepository.save(pedido);

            logger.debug("Cliente no encontrado, rechazamos el pedido");

            return false;
        }

        return true;

    }

    private boolean step2(Pedido pedido) {

        // PASO 2 - Comprobar existencia de producto
        logger.debug("PASO 2 - Comprobar existencia de producto");
        Optional<Producto> producto = this.productoRepository.findById(pedido.getProductoId());

        if (!producto.isPresent()) {
            pedido.setEstado(PedidoEstado.RECHAZADO);
            pedido.setMotivoRechazo(PedidoRechazo.PRODUCTO_DESCONOCIDO);
            this.pedidoRepository.save(pedido);

            logger.debug("Producto no encontrado, rechazamos el pedido");

            return false;
        }

        return true;

    }

    private boolean step3(Pedido pedido) {

        Optional<Cliente> cliente = this.clienteRepository.findById(pedido.getClienteId());
        Optional<Producto> producto = this.productoRepository.findById(pedido.getProductoId());
        BigDecimal totalPedido = producto.get().getPrecio().multiply(new BigDecimal(pedido.getCantidad()));

        // PASO 3 - Transaccion realizar cobro al cliente
        logger.debug("PASO 3 - Transaccion realizar cobro al cliente");
        // 3.1 - Transaccion de retirada de dinero del cliente
        logger.debug("3.1 - Transaccion de retirada de dinero del cliente");
        
        ClienteTransaccion clienteTransaccion = new ClienteTransaccion();
        clienteTransaccion.setClienteId(cliente.get().getId().getId());
        clienteTransaccion.setTransaccion(TipoTransaccion.RETIRADA);
        clienteTransaccion.setCredito(totalPedido);
        this.clienteCommandService.commandRealizarTransaccion(clienteTransaccion);

        // 3.2 - Comprobacion del saldo
        logger.debug("3.2 - Comprobacion del saldo");
        cliente = this.clienteRepository.findById(pedido.getClienteId());
        if (cliente.get().getCredito().compareTo(new BigDecimal(0)) < 0) {

            // abonamos al cliente
            clienteTransaccion = new ClienteTransaccion();
            clienteTransaccion.setClienteId(cliente.get().getId().getId());
            clienteTransaccion.setTransaccion(TipoTransaccion.INGRESO);
            clienteTransaccion.setCredito(totalPedido);
            this.clienteCommandService.commandRealizarTransaccion(clienteTransaccion);

            // rechazamos la operacion
            pedido.setEstado(PedidoEstado.RECHAZADO);
            pedido.setMotivoRechazo(PedidoRechazo.INSUFICIENTE_CREDITO);
            this.pedidoRepository.save(pedido);

            logger.debug("Cliente sin saldo, rechazamos el pedido");

            return false;
        }

        return true;

    }

    private boolean step4(Pedido pedido) {
        
        Optional<Cliente> cliente = this.clienteRepository.findById(pedido.getClienteId());
        Optional<Producto> producto = this.productoRepository.findById(pedido.getProductoId());
        BigDecimal totalPedido = producto.get().getPrecio().multiply(new BigDecimal(pedido.getCantidad()));

        // PASO 4 - Transaccion realizar retiraza de stock
        logger.debug("PASO 4 - Transaccion realizar retiraza de stock");

        // 4.1 - Transaccion de retirada de unidades del stock
        logger.debug("4.1 - Transaccion de retirada de unidades del stock");
        ProductoTransaccion productoTransaccion = new ProductoTransaccion();
        productoTransaccion.setProductoId(producto.get().getId().getId());
        productoTransaccion.setTransaccion(TipoTransaccion.RETIRADA);
        productoTransaccion.setStock(pedido.getCantidad());
        this.productoCommandService.commandRealizarTransaccion(productoTransaccion);

        // 4.2 - Comprobacion del stock
        logger.debug("4.2 - Comprobacion del stock");
        producto = this.productoRepository.findById(pedido.getProductoId());
        if (producto.get().getStock() < 0) {

            // retrocedemos el stock
            productoTransaccion = new ProductoTransaccion();
            productoTransaccion.setProductoId(producto.get().getId().getId());
            productoTransaccion.setTransaccion(TipoTransaccion.INGRESO);
            productoTransaccion.setStock(pedido.getCantidad());
            this.productoCommandService.commandRealizarTransaccion(productoTransaccion);

            // abonamos al cliente
            ClienteTransaccion clienteTransaccion = new ClienteTransaccion();
            clienteTransaccion.setClienteId(cliente.get().getId().getId());
            clienteTransaccion.setTransaccion(TipoTransaccion.INGRESO);
            clienteTransaccion.setCredito(totalPedido);
            this.clienteCommandService.commandRealizarTransaccion(clienteTransaccion);

            // rechazamos la operacion
            pedido.setEstado(PedidoEstado.RECHAZADO);
            pedido.setMotivoRechazo(PedidoRechazo.INSUFICIENTE_STOCK);
            this.pedidoRepository.save(pedido);

            logger.debug("Producto sin stock, rechazamos el pedido");

            return false;
        }

        return true;

    }

    private void execute(Optional<Pedido> pedido) {

        if (pedido.isPresent()) {

            if (step1(pedido.get())) {

                if (step2(pedido.get())) {
                    
                    if (step3(pedido.get())) {
                        
                        if (step4(pedido.get())) {
                        
                            // si todo fue bien, aprobamos el pedido
                            pedido.get().setEstado(PedidoEstado.APROBADO);    
                            this.pedidoRepository.save(pedido.get());

                        }
                    }
                }
            }
        }

    }

    public void run()
    {
        logger.debug("Iniciamos Transaccion de pedido");

        try {
            // Le damos suspense a la transaccion :P
            Thread.sleep(2000);
        }
        catch (InterruptedException iex) { }

        if (this.pedidoId != null) {

            Optional<Pedido> pedido = this.pedidoRepository.findById(pedidoId);
            this.execute(pedido);
            
        }
        
        logger.debug("Finalizamos saga de pedido");
    }
}