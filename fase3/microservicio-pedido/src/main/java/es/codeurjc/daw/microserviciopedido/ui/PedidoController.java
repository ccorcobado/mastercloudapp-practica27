package es.codeurjc.daw.microserviciopedido.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.microserviciopedido.application.PedidoCommandService;
import es.codeurjc.daw.microserviciopedido.application.PedidoQueryService;
import es.codeurjc.daw.common.PedidoBase;
import es.codeurjc.daw.common.PedidoInput;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    private static final Logger logger = LogManager.getLogger(PedidoController.class);

    @Autowired
    private PedidoCommandService pedidoCommandService;

    @Autowired
    private PedidoQueryService pedidoQueryService;

    @PostMapping("")
	public ResponseEntity<?> alta(@RequestBody PedidoInput entrada) {

        logger.trace("Inicio alta pedido ");

        try {

            PedidoBase salida = this.pedidoCommandService.commandAltaPedido(entrada);
            logger.trace(salida.toString());

            return new ResponseEntity<PedidoBase>(salida, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(e);

            return new ResponseEntity<>("Ups! Algo fue mal :S \n" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
	public ResponseEntity<List<PedidoBase>> listar() {

        logger.trace("Inicio listar todos los pedidos ");

        return new ResponseEntity<List<PedidoBase>>(
            this.pedidoQueryService.getAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{pedidoId}")
	public ResponseEntity<PedidoBase> leer(@PathVariable String pedidoId) {

        logger.trace("Inicio leer pedido con identificador " + pedidoId);

        PedidoBase salida = this.pedidoQueryService.get(pedidoId);

        if (salida == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

        return new ResponseEntity<PedidoBase>(
            salida, HttpStatus.OK);
	}

}