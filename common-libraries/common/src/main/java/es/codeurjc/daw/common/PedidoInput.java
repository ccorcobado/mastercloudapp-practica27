package es.codeurjc.daw.common;

public class PedidoInput {

    private String clienteId;
    private String productoId;
    private int cantidad;

    public PedidoInput() {
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "PedidoInput [cantidad=" + cantidad + ", clienteId=" + clienteId + ", productoId=" + productoId + "]";
    }
    
}