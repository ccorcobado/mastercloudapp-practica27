package es.codeurjc.daw.monolito.application.dto;

import java.math.BigDecimal;

public class ClienteBase {
    
    private String clienteId;
    private BigDecimal credito;

    public ClienteBase() {
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    @Override
    public String toString() {
        return "ClienteBase [clienteId=" + clienteId + ", credito=" + credito + "]";
    }
}