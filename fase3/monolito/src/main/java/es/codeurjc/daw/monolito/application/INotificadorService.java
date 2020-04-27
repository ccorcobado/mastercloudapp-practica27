package es.codeurjc.daw.monolito.application;

import es.codeurjc.daw.common.ClienteTransaccion;

public interface INotificadorService {

    public void notificar(ClienteTransaccion transaccion);
    
}