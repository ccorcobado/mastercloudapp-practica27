package es.codeurjc.daw.monolito.application.dto;

import java.math.BigDecimal;

public class ProductoInput {

    private String nombre;
    private BigDecimal precio;
    private int stock;

    public ProductoInput() {
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