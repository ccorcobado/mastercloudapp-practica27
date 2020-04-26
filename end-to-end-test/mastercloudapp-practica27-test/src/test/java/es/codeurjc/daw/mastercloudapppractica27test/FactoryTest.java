package es.codeurjc.daw.mastercloudapppractica27test;

import java.math.BigDecimal;

import es.codeurjc.daw.common.ClienteInput;
import es.codeurjc.daw.common.PedidoInput;
import es.codeurjc.daw.common.ProductoInput;

public class FactoryTest {

    public static final int Port = 8080;

    public static String getUrl() {
        return String.format("http://localhost:%s", Port);
    }

    public static String getUrlCliente() {
        return "/api/cliente";
    }

    public static String getUrlCliente(String idCliente) {
        return String.format("/api/cliente/%s", idCliente);
    }

    public static String getUrlProducto() {
        return "/api/producto";
    }

    public static String getUrlProducto(String idProducto) {
        return String.format("/api/producto/%s", idProducto);
    }

    public static String getUrlPedido() {
        return "/api/pedido";
    }

    public static String getUrlPedido(String idPedido) {
        return String.format("/api/pedido/%s", idPedido);
    }

    public static ClienteInput CreateCliente(String nombre, String apellido, BigDecimal credito) {

        return new ClienteInput(nombre, apellido, credito);
    }

    public static ProductoInput CreateProducto(String nombre, BigDecimal precio, int stock) {
        
        return new ProductoInput(nombre, precio, stock);
    }

    public static PedidoInput CreatePedido(String clienteId, String productoId, int cantidad) {

        return new PedidoInput(clienteId, productoId, cantidad);
    }
}