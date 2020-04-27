package es.codeurjc.daw.monolito.domain;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Producto {

    @Id
    @EmbeddedId  ProductoId id;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    public Producto() {
        this.id = new ProductoId();
    }

    public Producto(ProductoId id, String nombre, BigDecimal precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public ProductoId getId() {
        return id;
    }

    public void setId(ProductoId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public void sumarStock(int cantidad) {
        this.stock += cantidad;
    }

    public void restarStock(int cantidad) {
        this.stock -= cantidad;
    }
}