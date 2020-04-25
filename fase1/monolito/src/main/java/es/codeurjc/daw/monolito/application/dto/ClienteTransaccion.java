package es.codeurjc.daw.monolito.application.dto;

public class ClienteTransaccion extends ClienteBase{

    private TipoTransaccion transaccion;

    public ClienteTransaccion() {
    }

    public TipoTransaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(TipoTransaccion transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public String toString() {
        return "ClienteTransaccion [id = " + getClienteId() + ", transaccion=" + transaccion + ", credito = " + this.getCredito() + "]";
    }
}