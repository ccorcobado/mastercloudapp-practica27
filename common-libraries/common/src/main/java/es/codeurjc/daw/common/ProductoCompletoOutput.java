package es.codeurjc.daw.common;

import java.math.BigDecimal;

public class ProductoCompletoOutput {

    private String productoId;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    public ProductoCompletoOutput() {
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductoCompletoOutput [nombre=" + nombre + ", precio=" + precio + ", productoId=" + productoId
                + ", stock=" + stock + "]";
    }

}