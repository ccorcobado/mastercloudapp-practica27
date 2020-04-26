package es.codeurjc.daw.monolito.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.common.ClienteCompletoOutput;
import es.codeurjc.daw.monolito.domain.Cliente;
import es.codeurjc.daw.monolito.domain.ClienteId;
import es.codeurjc.daw.monolito.infrastructure.ClienteRepository;

@Service
public class ClienteQueryService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired(required=true)
    private ModelMapper modelMapperQuery;

	public List<ClienteCompletoOutput> getAll() {

        List<ClienteCompletoOutput> allClientesDto = new ArrayList<>();
        
        List<Cliente> allClientes = this.clienteRepository.findAll();

        for (Cliente cliente : allClientes) {
            allClientesDto.add(this.convertEntityToDto(cliente));
        }

        return allClientesDto;
	}

	public ClienteCompletoOutput get(String clienteId) {

        Optional<Cliente> cliente = this.clienteRepository.findById(new ClienteId(clienteId));

        if (!cliente.isPresent())
            return null;

		return this.convertEntityToDto(cliente.get());
    }
    
    private ClienteCompletoOutput convertEntityToDto(Cliente cliente) {
		return modelMapperQuery.map(cliente, ClienteCompletoOutput.class);
	}

}