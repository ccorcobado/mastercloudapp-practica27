package es.codeurjc.daw.monolito.domain;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cliente {

    @Id
    @EmbeddedId  ClienteId id;
        private String nombre;
        private String apellido;
        private BigDecimal credito;

    public Cliente() {
        this.id = new ClienteId();
    }

    public Cliente(ClienteId id, String nombre, String apellido, BigDecimal credito) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.credito = credito;
    }

    public ClienteId getId() {
        return id;
    }

    public void setId(ClienteId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public void sumarCredito(BigDecimal credito) {
        this.setCredito(this.credito.add(credito));
    }

    public void restarCredito(BigDecimal credito) {
        this.setCredito(this.credito.subtract(credito));
    }
}