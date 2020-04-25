package es.codeurjc.daw.monolito.ui;

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

import es.codeurjc.daw.monolito.application.ProductoCommandService;
import es.codeurjc.daw.monolito.application.ProductoQueryService;
import es.codeurjc.daw.monolito.application.dto.ProductoBase;
import es.codeurjc.daw.monolito.application.dto.ProductoCompletoOutput;
import es.codeurjc.daw.monolito.application.dto.ProductoInput;
import es.codeurjc.daw.monolito.application.dto.ProductoTransaccion;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    private static final Logger logger = LogManager.getLogger(ProductoController.class);

    @Autowired
    private ProductoCommandService productoCommandService;

    @Autowired
    private ProductoQueryService productoQueryService;

    @PostMapping("")
	public ResponseEntity<ProductoBase> alta(@RequestBody ProductoInput entrada) {

        logger.trace("Inicio alta producto ");

        try {

            ProductoBase salida = this.productoCommandService.commandAltaProducto(entrada);
            logger.trace(salida.toString());

            return new ResponseEntity<ProductoBase>(salida, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
	public ResponseEntity<ProductoBase> transaccion(@RequestBody ProductoTransaccion entrada) {

        logger.trace("Inicio transaccion producto");

        try {

            ProductoBase salida = this.productoCommandService.commandRealizarTransaccion(entrada);
            logger.trace(salida.toString());

            return new ResponseEntity<ProductoBase>(salida, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
	public ResponseEntity<List<ProductoCompletoOutput>> listar() {

        logger.trace("Inicio listar todos los productos ");

        return new ResponseEntity<List<ProductoCompletoOutput>>(
            this.productoQueryService.getAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{productoId}")
	public ResponseEntity<ProductoCompletoOutput> leer(@PathVariable String productoId) {

        logger.trace("Inicio leer producto con identificador " + productoId);

        ProductoCompletoOutput salida = this.productoQueryService.get(productoId);

        if (salida == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

        return new ResponseEntity<ProductoCompletoOutput>(
            salida, HttpStatus.OK);
	}

}