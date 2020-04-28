package es.codeurjc.daw.common;

import java.math.BigDecimal;

public class ClienteInput {

    private String nombre;
    private String apellido;
    private BigDecimal credito;

    public ClienteInput() {
    }

    public ClienteInput(String nombre, String apellido, BigDecimal credito) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.credito = credito;
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

    @Override
    public String toString() {
        return "ClienteInput [apellido=" + apellido + ", credito=" + credito + ", nombre=" + nombre + "]";
    }
}