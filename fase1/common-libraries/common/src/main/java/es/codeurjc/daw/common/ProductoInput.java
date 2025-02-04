package es.codeurjc.daw.common;

import java.math.BigDecimal;

public class ProductoInput {

    private String nombre;
    private BigDecimal precio;
    private int stock;

    public ProductoInput() {
    }

    public ProductoInput(String nombre, BigDecimal precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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
        return "ProductoInput [nombre=" + nombre + ", precio=" + precio + ", stock=" + stock + "]";
    }
}