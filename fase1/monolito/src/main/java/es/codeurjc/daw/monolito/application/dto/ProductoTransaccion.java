package es.codeurjc.daw.monolito.application.dto;

public class ProductoTransaccion {

    private String productoId;
    private int stock;
    private TipoTransaccion transaccion;

    public ProductoTransaccion() {
    }

    public TipoTransaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(TipoTransaccion transaccion) {
        this.transaccion = transaccion;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductoTransaccion [stock=" + stock + ", productoId=" + productoId + ", transaccion=" + transaccion
                + "]";
    }
}