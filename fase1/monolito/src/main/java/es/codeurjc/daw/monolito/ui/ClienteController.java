package es.codeurjc.daw.monolito.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.monolito.application.ClienteCommandService;
import es.codeurjc.daw.monolito.application.ClienteQueryService;
import es.codeurjc.daw.monolito.application.dto.ClienteCompletoOutput;
import es.codeurjc.daw.monolito.application.dto.ClienteInput;
import es.codeurjc.daw.monolito.application.dto.ClienteTransaccion;
import es.codeurjc.daw.monolito.application.dto.ClienteBase;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private static final Logger logger = LogManager.getLogger(ClienteController.class);

    @Autowired
    private ClienteCommandService clienteCommandService;

    @Autowired
    private ClienteQueryService clienteQueryService;

    @PostMapping("")
	public ResponseEntity<ClienteBase> alta(@RequestBody ClienteInput entrada) {

        logger.trace("Inicio alta cliente ");

        try {

            ClienteBase salida = this.clienteCommandService.commandAltaCliente(entrada);
            logger.trace(salida.toString());

            return new ResponseEntity<ClienteBase>(salida, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
	public ResponseEntity<ClienteBase> transaccion(@RequestBody ClienteTransaccion entrada) {

        logger.trace("Inicio transaccion cliente ");

        try {

            ClienteBase salida = this.clienteCommandService.commandRealizarTransaccion(entrada);
            logger.trace(salida.toString());

            return new ResponseEntity<ClienteBase>(salida, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
	public ResponseEntity<List<ClienteCompletoOutput>> listar() {

        logger.trace("Inicio listar todos los clientes ");

        return new ResponseEntity<List<ClienteCompletoOutput>>(
            this.clienteQueryService.getAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{clienteId}")
	public ResponseEntity<ClienteCompletoOutput> leer(@PathVariable String clienteId) {

        logger.trace("Inicio leer cliente con identificador " + clienteId);

        ClienteCompletoOutput salida = this.clienteQueryService.get(clienteId);

        if (salida == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

        return new ResponseEntity<ClienteCompletoOutput>(
            salida, HttpStatus.OK);
	}
}