package es.codeurjc.daw.monolito.application;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.common.ClienteInput;
import es.codeurjc.daw.common.ClienteTransaccion;
import es.codeurjc.daw.common.ClienteBase;
import es.codeurjc.daw.monolito.domain.Cliente;
import es.codeurjc.daw.monolito.domain.ClienteId;
import es.codeurjc.daw.monolito.infrastructure.ClienteRepository;

@Service
public class ClienteCommandService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private NotificadorService notificadorService;
	
    @Autowired
    private ModelMapper modelMapperCommand;

	public ClienteBase commandAltaCliente(ClienteInput input) {

		if (input == null)
            throw new IllegalArgumentException("La entrada esta vacia");

		Cliente cliente = new Cliente();
		cliente.setNombre(input.getNombre());
		cliente.setApellido(input.getApellido());
		cliente.setCredito(input.getCredito());

		cliente = this.clienteRepository.save(cliente);

		return convertEntityToDto(cliente);
	}

	public ClienteBase commandRealizarTransaccion(ClienteTransaccion input) {

		if (input == null)
			throw new IllegalArgumentException("La entrada esta vacia");
			
		Optional<Cliente> optionalCliente = this.clienteRepository.findById(new ClienteId(input.getClienteId()));

		if (!optionalCliente.isPresent())
			throw new IllegalArgumentException("Identificador de cliente no valido");
			
		switch (input.getTransaccion()) {

			case INGRESO:
				optionalCliente.get().sumarCredito(input.getCredito());
				break;

			case RETIRADA:
				optionalCliente.get().restarCredito(input.getCredito());	
				break;
				
			default:
				break;
		}

		Cliente cliente = this.clienteRepository.save(optionalCliente.get());
		
		this.notificadorService.notificar(input);

		return convertEntityToDto(cliente);
	}

	private ClienteBase convertEntityToDto(Cliente cliente) {
		return modelMapperCommand.map(cliente, ClienteBase.class);
	}

}