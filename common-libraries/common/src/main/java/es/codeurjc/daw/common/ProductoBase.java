package es.codeurjc.daw.common;

import java.math.BigDecimal;

public class ProductoBase {

    private String productoId;
    private BigDecimal precio;
    private int stock;

    public ProductoBase() {
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ProductoBase [precio=" + precio + ", productoId=" + productoId + ", stock=" + stock + "]";
    }
}