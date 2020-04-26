package es.codeurjc.daw.mastercloudapppractica27test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.client.WebClient;

import es.codeurjc.daw.common.ClienteBase;
import es.codeurjc.daw.common.ClienteInput;
import es.codeurjc.daw.common.ClienteTransaccion;
import es.codeurjc.daw.common.PedidoBase;
import es.codeurjc.daw.common.PedidoInput;
import es.codeurjc.daw.common.ProductoBase;
import es.codeurjc.daw.common.ProductoInput;
import es.codeurjc.daw.common.ProductoTransaccion;
import es.codeurjc.daw.common.TipoTransaccion;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationTest {

	private WebClient webClient;

	@BeforeEach
	public void setUp() {
		this.webClient = WebClient.create(FactoryTest.getUrl());
	}

	private ClienteBase crearCliente(ClienteInput entrada) {

		// Creacion de cliente
		Mono<ClienteBase> monoClienteCreado = this.webClient
			.post()
			.uri(FactoryTest.getUrlCliente())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(entrada), ClienteInput.class)
			.retrieve()
			.bodyToMono(ClienteBase.class);

		ClienteBase clienteCreado = monoClienteCreado.block();

		return clienteCreado;
	}

	private ClienteBase leerCliente(String clienteId) {

		// Lectura de cliente
		Mono<ClienteBase> monoClienteEncontrado = this.webClient.get()
			.uri(FactoryTest.getUrlCliente(clienteId))
			.retrieve()
			.bodyToMono(ClienteBase.class);

		// Assert de cliente
		ClienteBase clienteEncontrado = monoClienteEncontrado.block();

		return clienteEncontrado;
	}

	private ClienteBase crearClienteTransaccion(ClienteTransaccion transaccion) {

		Mono<ClienteBase> monoClienteIngreso = this.webClient.put()
			.uri(FactoryTest.getUrlCliente())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(transaccion), ClienteTransaccion.class)
			.retrieve()
			.bodyToMono(ClienteBase.class);

		// Assert de cliente
		ClienteBase clienteIngreso = monoClienteIngreso.block();
		return clienteIngreso;
	}

	private ProductoBase crearProducto(ProductoInput entrada) {

		// Creacion de producto
		Mono<ProductoBase> monoProductoCreado = this.webClient.post()
			.uri(FactoryTest.getUrlProducto())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(entrada), ProductoInput.class)
			.retrieve()
			.bodyToMono(ProductoBase.class);

		ProductoBase productoCreado = monoProductoCreado.block();

		return productoCreado;
	}
	
	private ProductoBase leerProducto(String productoId) {

		// Lectura de producto
		Mono<ProductoBase> monoProductoEncontrado = this.webClient.get()
			.uri(FactoryTest.getUrlProducto(productoId))
			.retrieve()
			.bodyToMono(ProductoBase.class);

		// Assert de cliente
		ProductoBase productoEncontrado = monoProductoEncontrado.block();

		return productoEncontrado;
	}

	private ProductoBase crearProductoTransaccion(ProductoTransaccion transaccion) {

		Mono<ProductoBase> monoProductoIngreso = this.webClient
			.put()
			.uri(FactoryTest.getUrlProducto())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(transaccion), ProductoTransaccion.class)
			.retrieve()
			.bodyToMono(ProductoBase.class);

		// Assert de cliente
		ProductoBase productoIngreso = monoProductoIngreso.block();

		return productoIngreso;
	}

	private PedidoBase crearPedido(PedidoInput pedido) {

		// Creacion de pedido
		Mono<PedidoBase> monoPedidoCreado = this.webClient
			.post()
			.uri(FactoryTest.getUrlPedido())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(Mono.just(pedido), PedidoInput.class)
			.retrieve()
			.bodyToMono(PedidoBase.class);

		PedidoBase pedidoCreado = monoPedidoCreado.block();

		return pedidoCreado;
	}

	private PedidoBase leerPedido(String idPedido) {

		// Lectura de pedido
		Mono<PedidoBase> monoPedidoBuscado = this.webClient
			.get()
			.uri(FactoryTest.getUrlPedido(idPedido))
			.retrieve()
			.bodyToMono(PedidoBase.class);

		PedidoBase pedidoBuscado = monoPedidoBuscado.block();

		return pedidoBuscado;
	}

	@Test
	public void createCliente_Test() {

		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "createCliente_Test", new BigDecimal(1500));
		ClienteBase clienteCreado = this.crearCliente(entrada);
		ClienteBase clienteEncontrado = this.leerCliente(clienteCreado.getClienteId());

		// Assert de cliente		
		assertNotNull(clienteEncontrado);
	}

	@Test
	public void clienteTransaccionIngreso_Test() {

		BigDecimal importeInicial = new BigDecimal(1500);
		BigDecimal importeIngreso = new BigDecimal(150);

		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "clienteTransaccionIngreso_Test", importeInicial);
		ClienteBase clienteCreado = this.crearCliente(entrada);

		// Transaccion de ingreso
		ClienteTransaccion transaccion = new ClienteTransaccion();
		transaccion.setClienteId(clienteCreado.getClienteId());
		transaccion.setTransaccion(TipoTransaccion.INGRESO);
		transaccion.setCredito(importeIngreso);

		// Assert de importe cliente distinto
		ClienteBase clienteIngreso = this.crearClienteTransaccion(transaccion);
		assertNotEquals(importeInicial, clienteIngreso.getCredito());

		// Lectura de cliente
		ClienteBase clienteEncontrado = this.leerCliente(clienteCreado.getClienteId());

		// Assert de importe igual al leido
		assertEquals(clienteIngreso.getCredito(), clienteEncontrado.getCredito());
	}

	@Test
	public void clienteTransaccionRetirada_Test() {

		BigDecimal importeInicial = new BigDecimal(1500);
		BigDecimal importeIngreso = new BigDecimal(150);

		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "clienteTransaccionRetirada_Test", importeInicial);
		ClienteBase clienteCreado = this.crearCliente(entrada);

		// Transaccion de ingreso
		ClienteTransaccion transaccion = new ClienteTransaccion();
		transaccion.setClienteId(clienteCreado.getClienteId());
		transaccion.setTransaccion(TipoTransaccion.RETIRADA);
		transaccion.setCredito(importeIngreso);

		// Assert de importe cliente distinto
		ClienteBase clienteIngreso = this.crearClienteTransaccion(transaccion);
		assertTrue(importeInicial.compareTo(clienteIngreso.getCredito()) != 0);

		// Lectura de cliente
		ClienteBase clienteEncontrado = this.leerCliente(clienteCreado.getClienteId());

		// Assert de importe igual al leido
		assertTrue(clienteIngreso.getCredito().compareTo(clienteEncontrado.getCredito()) == 0);
	}

	@Test
	public void createProducto_Test() {

		ProductoInput producto = FactoryTest.CreateProducto(
			"createProducto_Test", new BigDecimal(1500), 150);

		ProductoBase productoCreado = this.crearProducto(producto);
		ProductoBase productoEncontrado = this.leerProducto(productoCreado.getProductoId());

		// Assert de producto
		assertNotNull(productoEncontrado);
	}

	@Test
	public void productoTransaccionIngreso_Test() {

		int stockInicial = 150;
		int stockTransaccion = 5;

		ProductoInput producto = FactoryTest.CreateProducto(
			"productoTransaccionIngreso_Test", new BigDecimal(1500), stockInicial);

		ProductoBase productoCreado = this.crearProducto(producto);

		// Transaccion de ingreso
		ProductoTransaccion transaccion = new ProductoTransaccion();
		transaccion.setProductoId(productoCreado.getProductoId());
		transaccion.setTransaccion(TipoTransaccion.INGRESO);
		transaccion.setStock(stockTransaccion);
		ProductoBase productoIngreso = this.crearProductoTransaccion(transaccion);

		assertNotEquals(stockInicial, productoIngreso.getStock());

		// Lectura de producto
		ProductoBase productoEncontrado = this.leerProducto(productoCreado.getProductoId());
		assertEquals(productoIngreso.getStock(), productoEncontrado.getStock());
	}

	@Test
	public void productoTransaccionRetirada_Test() {

		int stockInicial = 150;
		int stockTransaccion = 5;

		ProductoInput producto = FactoryTest.CreateProducto(
			"productoTransaccionRetirada_Test", new BigDecimal(1500), stockInicial);

		ProductoBase productoCreado = this.crearProducto(producto);

		// Transaccion de ingreso
		ProductoTransaccion transaccion = new ProductoTransaccion();
		transaccion.setProductoId(productoCreado.getProductoId());
		transaccion.setTransaccion(TipoTransaccion.RETIRADA);
		transaccion.setStock(stockTransaccion);
		ProductoBase productoIngreso = this.crearProductoTransaccion(transaccion);

		assertNotEquals(stockInicial, productoIngreso.getStock());

		// Lectura de producto
		ProductoBase productoEncontrado = this.leerProducto(productoCreado.getProductoId());
		assertEquals(productoIngreso.getStock(), productoEncontrado.getStock());
	}

	@Test
	public void pedidoTransaccionClienteNoEncontrado_Test() {

		PedidoInput pedido = FactoryTest.CreatePedido("FAKE_CLIENTEID", "FAKE_PRODUCTOID", 5);
		PedidoBase pedidoCreado = this.crearPedido(pedido);
		PedidoBase pedidoBuscado = null;

		do {

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pedidoBuscado = this.leerPedido(pedidoCreado.getPedidoId());
			
		} while (pedidoBuscado == null || pedidoBuscado.getEstado().equals("EN_PROCESO"));
		
		assertEquals(pedidoBuscado.getEstado(), "RECHAZADO");
		assertEquals(pedidoBuscado.getMotivoRechazo(), "CLIENTE_DESCONOCIDO");
	}

	@Test
	public void pedidoTransaccionProductoNoEncontrado_Test() {
		
		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "pedidoTransaccionProductoNoEncontrado_Test", new BigDecimal(1500));
		ClienteBase clienteCreado = this.crearCliente(entrada);

		PedidoInput pedido = FactoryTest.CreatePedido(clienteCreado.getClienteId(), "FAKE_PRODUCTOID", 5);
		PedidoBase pedidoCreado = this.crearPedido(pedido);
		PedidoBase pedidoBuscado = null;

		do {

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pedidoBuscado = this.leerPedido(pedidoCreado.getPedidoId());
			
		} while (pedidoBuscado == null || pedidoBuscado.getEstado().equals("EN_PROCESO"));
		
		assertEquals(pedidoBuscado.getEstado(), "RECHAZADO");
		assertEquals(pedidoBuscado.getMotivoRechazo(), "PRODUCTO_DESCONOCIDO");
	}

	@Test
	public void pedidoTransaccionCreditoInsuficiente_Test() {
		
		BigDecimal creditoCliente = new BigDecimal(80);
		BigDecimal precioProducto = new BigDecimal(70);
		int stockInicial = 5;
		int unidades = 2;

		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "pedidoTransaccionCreditoInsuficiente_Test", creditoCliente);
		ClienteBase clienteCreado = this.crearCliente(entrada);

		ProductoInput producto = FactoryTest.CreateProducto(
			"pedidoTransaccionCreditoInsuficiente_Test", precioProducto, stockInicial);
		ProductoBase productoCreado = this.crearProducto(producto);

		PedidoInput pedido = FactoryTest.CreatePedido(
			clienteCreado.getClienteId(), productoCreado.getProductoId(), unidades);
		PedidoBase pedidoCreado = this.crearPedido(pedido);
		PedidoBase pedidoBuscado = null;

		do {

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pedidoBuscado = this.leerPedido(pedidoCreado.getPedidoId());
			
		} while (pedidoBuscado == null || pedidoBuscado.getEstado().equals("EN_PROCESO"));
		
		assertEquals(pedidoBuscado.getEstado(), "RECHAZADO");
		assertEquals(pedidoBuscado.getMotivoRechazo(), "INSUFICIENTE_CREDITO");

		ClienteBase clienteEncontrado = this.leerCliente(clienteCreado.getClienteId());
		ProductoBase productoEncontrado = this.leerProducto(productoCreado.getProductoId());

		assertTrue(clienteEncontrado.getCredito().compareTo(creditoCliente) == 0);
		assertEquals(productoEncontrado.getStock(), stockInicial);
	}

	@Test
	public void pedidoTransaccionStockInsuficiente_Test() {
		
		BigDecimal creditoCliente = new BigDecimal(1080);
		BigDecimal precioProducto = new BigDecimal(70);
		int stockInicial = 1;
		int unidades = 5;

		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "pedidoTransaccionStockInsuficiente_Test", creditoCliente);
		ClienteBase clienteCreado = this.crearCliente(entrada);

		ProductoInput producto = FactoryTest.CreateProducto(
			"pedidoTransaccionStockInsuficiente_Test", precioProducto, stockInicial);
		ProductoBase productoCreado = this.crearProducto(producto);

		PedidoInput pedido = FactoryTest.CreatePedido(
			clienteCreado.getClienteId(), productoCreado.getProductoId(), unidades);
		PedidoBase pedidoCreado = this.crearPedido(pedido);
		PedidoBase pedidoBuscado = null;

		do {

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pedidoBuscado = this.leerPedido(pedidoCreado.getPedidoId());
			
		} while (pedidoBuscado == null || pedidoBuscado.getEstado().equals("EN_PROCESO"));
		
		assertEquals(pedidoBuscado.getEstado(), "RECHAZADO");
		assertEquals(pedidoBuscado.getMotivoRechazo(), "INSUFICIENTE_STOCK");

		ClienteBase clienteEncontrado = this.leerCliente(clienteCreado.getClienteId());
		ProductoBase productoEncontrado = this.leerProducto(productoCreado.getProductoId());

		assertTrue(clienteEncontrado.getCredito().compareTo(creditoCliente) == 0);
		assertEquals(productoEncontrado.getStock(), stockInicial);
	}

	@Test
	public void pedidoTransaccionPedidoCorrecto_Test() {
		
		BigDecimal creditoCliente = new BigDecimal(1080);
		BigDecimal precioProducto = new BigDecimal(70);
		int stockInicial = 50;
		int unidades = 5;

		ClienteInput entrada = FactoryTest.CreateCliente(
			"cliente", "pedidoTransaccionPedidoCorrecto_Test", creditoCliente);
		ClienteBase clienteCreado = this.crearCliente(entrada);

		ProductoInput producto = FactoryTest.CreateProducto(
			"pedidoTransaccionPedidoCorrecto_Test", precioProducto, stockInicial);
		ProductoBase productoCreado = this.crearProducto(producto);

		PedidoInput pedido = FactoryTest.CreatePedido(
			clienteCreado.getClienteId(), productoCreado.getProductoId(), unidades);
		PedidoBase pedidoCreado = this.crearPedido(pedido);
		PedidoBase pedidoBuscado = null;

		do {

			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pedidoBuscado = this.leerPedido(pedidoCreado.getPedidoId());
			
		} while (pedidoBuscado == null || pedidoBuscado.getEstado().equals("EN_PROCESO"));
		
		assertEquals(pedidoBuscado.getEstado(), "APROBADO");

		ClienteBase clienteEncontrado = this.leerCliente(clienteCreado.getClienteId());
		ProductoBase productoEncontrado = this.leerProducto(productoCreado.getProductoId());

		assertTrue(clienteEncontrado.getCredito().compareTo(creditoCliente) != 0);
		assertNotEquals(productoEncontrado.getStock(), stockInicial);
	}

}
