package es.codeurjc.daw.monolito.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pedido {

    @Id
    @EmbeddedId  PedidoId id;
    private ClienteId clienteId;
    private ProductoId productoId;
    private PedidoEstado estado;
    private PedidoRechazo motivoRechazo;
    private int cantidad;

    public Pedido() {
        this.id = new PedidoId();
    }

    public Pedido(PedidoId id, ClienteId clienteId, ProductoId productoId, PedidoEstado estado,
            PedidoRechazo motivoRechazo, int cantidad) {
        this.id = id;
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.estado = estado;
        this.motivoRechazo = motivoRechazo;
        this.cantidad = cantidad;
    }

    public PedidoId getId() {
        return id;
    }

    public void setId(PedidoId id) {
        this.id = id;
    }

    public ClienteId getClienteId() {
        return clienteId;
    }

    public void setClienteId(ClienteId clienteId) {
        this.clienteId = clienteId;
    }

    public ProductoId getProductoId() {
        return productoId;
    }

    public void setProductoId(ProductoId productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public PedidoEstado getEstado() {
        return estado;
    }

    public void setEstado(PedidoEstado estado) {
        this.estado = estado;
    }

    public PedidoRechazo getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(PedidoRechazo motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
}