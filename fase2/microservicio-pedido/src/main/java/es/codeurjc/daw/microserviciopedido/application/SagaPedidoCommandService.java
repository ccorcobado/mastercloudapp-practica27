package es.codeurjc.daw.microserviciopedido.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import es.codeurjc.daw.common.ClienteBase;
import es.codeurjc.daw.common.ClienteTransaccion;
import es.codeurjc.daw.common.ProductoBase;
import es.codeurjc.daw.common.ProductoTransaccion;
import es.codeurjc.daw.common.TipoTransaccion;
import es.codeurjc.daw.microserviciopedido.domain.Pedido;
import es.codeurjc.daw.microserviciopedido.domain.PedidoEstado;
import es.codeurjc.daw.microserviciopedido.domain.PedidoId;
import es.codeurjc.daw.microserviciopedido.domain.PedidoRechazo;
import es.codeurjc.daw.microserviciopedido.infrastructure.PedidoRepository;
import es.codeurjc.daw.microserviciopedido.infrastructure.WebClientMonolito;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SagaPedidoCommandService extends Thread {

    private static final Logger logger = LogManager.getLogger(SagaPedidoCommandService.class);

    private final WebClientMonolito webClient;
    private final PedidoId pedidoId;
    private final PedidoRepository pedidoRepository;

    public SagaPedidoCommandService(PedidoId pedidoId, PedidoRepository pedidoRepository, WebClientMonolito webClient) {

        this.pedidoId = pedidoId;
        this.pedidoRepository = pedidoRepository;
        this.webClient = webClient;
    }

    private boolean step1(Pedido pedido) {

        // PASO 1 - Comprobar existencia de cliente
        logger.debug("PASO 1 - Comprobar existencia de cliente");

        ClienteBase cliente = this.webClient.leerCliente(pedido.getClienteId().getId());

        if (cliente == null) {

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
        ProductoBase producto = this.webClient.leerProducto(pedido.getProductoId().getId());

        if (producto == null) {
            pedido.setEstado(PedidoEstado.RECHAZADO);
            pedido.setMotivoRechazo(PedidoRechazo.PRODUCTO_DESCONOCIDO);
            this.pedidoRepository.save(pedido);

            logger.debug("Producto no encontrado, rechazamos el pedido");

            return false;
        }

        return true;

    }

    private boolean step3(Pedido pedido) {

        ClienteBase cliente = this.webClient.leerCliente(pedido.getClienteId().getId());
        ProductoBase producto = this.webClient.leerProducto(pedido.getProductoId().getId());
        BigDecimal totalPedido = producto.getPrecio().multiply(new BigDecimal(pedido.getCantidad()));

        // PASO 3 - Transaccion realizar cobro al cliente
        logger.debug("PASO 3 - Transaccion realizar cobro al cliente");
        // 3.1 - Transaccion de retirada de dinero del cliente
        logger.debug("3.1 - Transaccion de retirada de dinero del cliente");
        
        ClienteTransaccion clienteTransaccion = new ClienteTransaccion();
        clienteTransaccion.setClienteId(cliente.getClienteId());
        clienteTransaccion.setTransaccion(TipoTransaccion.RETIRADA);
        clienteTransaccion.setCredito(totalPedido);
        this.webClient.realizarTransaccion(clienteTransaccion);

        // 3.2 - Comprobacion del saldo
        logger.debug("3.2 - Comprobacion del saldo");
        cliente = this.webClient.leerCliente(pedido.getClienteId().getId());
        if (cliente.getCredito().compareTo(new BigDecimal(0)) < 0) {

            // abonamos al cliente
            clienteTransaccion = new ClienteTransaccion();
            clienteTransaccion.setClienteId(cliente.getClienteId());
            clienteTransaccion.setTransaccion(TipoTransaccion.INGRESO);
            clienteTransaccion.setCredito(totalPedido);
            this.webClient.realizarTransaccion(clienteTransaccion);

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
        
        ClienteBase cliente = this.webClient.leerCliente(pedido.getClienteId().getId());
        ProductoBase producto = this.webClient.leerProducto(pedido.getProductoId().getId());
        BigDecimal totalPedido = producto.getPrecio().multiply(new BigDecimal(pedido.getCantidad()));

        // PASO 4 - Transaccion realizar retiraza de stock
        logger.debug("PASO 4 - Transaccion realizar retiraza de stock");

        // 4.1 - Transaccion de retirada de unidades del stock
        logger.debug("4.1 - Transaccion de retirada de unidades del stock");
        ProductoTransaccion productoTransaccion = new ProductoTransaccion();
        productoTransaccion.setProductoId(producto.getProductoId());
        productoTransaccion.setTransaccion(TipoTransaccion.RETIRADA);
        productoTransaccion.setStock(pedido.getCantidad());
        this.webClient.realizarTransaccion(productoTransaccion);

        // 4.2 - Comprobacion del stock
        logger.debug("4.2 - Comprobacion del stock");
        producto = this.webClient.leerProducto(pedido.getProductoId().getId());
        if (producto.getStock() < 0) {

            // retrocedemos el stock
            productoTransaccion = new ProductoTransaccion();
            productoTransaccion.setProductoId(producto.getProductoId());
            productoTransaccion.setTransaccion(TipoTransaccion.INGRESO);
            productoTransaccion.setStock(pedido.getCantidad());
            this.webClient.realizarTransaccion(productoTransaccion);

            // abonamos al cliente
            ClienteTransaccion clienteTransaccion = new ClienteTransaccion();
            clienteTransaccion.setClienteId(cliente.getClienteId());
            clienteTransaccion.setTransaccion(TipoTransaccion.INGRESO);
            clienteTransaccion.setCredito(totalPedido);
            this.webClient.realizarTransaccion(clienteTransaccion);

            // rechazamos la operacion
            pedido.setEstado(PedidoEstado.RECHAZADO);
            pedido.setMotivoRechazo(PedidoRechazo.INSUFICIENTE_STOCK);
            this.pedidoRepository.save(pedido);

            logger.debug("Producto sin stock, rechazamos el pedido");

            return false;
        }

        return true;

    }

    private List<Function<Pedido, Boolean>> cargarPasos() {

        Function<Pedido, Boolean> fStep1 = p -> step1(p);
        Function<Pedido, Boolean> fStep2 = p -> step2(p);
        Function<Pedido, Boolean> fStep3 = p -> step3(p);
        Function<Pedido, Boolean> fStep4 = p -> step4(p);
        
        List<Function<Pedido, Boolean>> listaPasos = Arrays.asList(fStep1, fStep2, fStep3, fStep4);

        return listaPasos;
    }
    
    private void execute(Optional<Pedido> pedido) {

        if (pedido.isPresent()) {
            
            Boolean procesoCorrecto = true;
            List<Function<Pedido, Boolean>> listaPasos = this.cargarPasos();

            for (Function<Pedido,Boolean> step : listaPasos) {

                if (!step.apply(pedido.get())) {
                    
                    procesoCorrecto = false;
                    break;

                }

            }

            if (procesoCorrecto) {

                // si todo fue bien, aprobamos el pedido
                pedido.get().setEstado(PedidoEstado.APROBADO);    
                this.pedidoRepository.save(pedido.get());

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

            try {
                Optional<Pedido> pedido = this.pedidoRepository.findById(pedidoId);
                this.execute(pedido);
            } catch (Exception e) {
                logger.error("Error al ejecutar la transaccion de pedido " + this.pedidoId.getId());
            }
            
        }
        
        logger.debug("Finalizamos saga de pedido");
    }
}