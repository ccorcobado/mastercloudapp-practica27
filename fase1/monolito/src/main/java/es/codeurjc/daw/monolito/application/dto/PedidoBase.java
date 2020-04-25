package es.codeurjc.daw.monolito.application.dto;

public class PedidoBase {

    private String pedidoId;
    private String clienteId;
    private String productoId;
    private int cantidad;

    public PedidoBase() {
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    @Override
    public String toString() {
        return "PedidoBase [cantidad=" + cantidad + ", clienteId=" + clienteId + ", pedidoId=" + pedidoId
                + ", productoId=" + productoId + "]";
    }
}