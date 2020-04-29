# Solución Práctica 27 - Asignatura "Aplicaciones nativas de la nube" - De monolito a microservicios

## Consideraciones previas
### Prerequisitos
A continuación se indican los requisitos necesarios para instalar, desplegar y ejecutar cada una de las fases

* Docker
* JDK 8
* Maven

### Estructuras de cada fase
Todas las fases se han diseñado con una estructura común para mayor facilidad en el desarrollo y testeo de cada pieza.

Existe una librería común para los objetos de E/S que van a viajar en las llamadas HTTP via API (DTOS)

Cada fase tiene en su raíz los siguientes ficheros:

* install - Fichero en formato windows (ps1) y linux (sh) para ejecutar la compilación en local de los proyectos mediante maven y desplegar cada una de las aplicaciones usando docker-compose
* uninstall - para y borra los contenedores creados con docker-compose
* docker-compose - fichero en formato yml para lanzar las aplicaciones. Será ejecutado por el fichero install.

### Casos de uso
Los casos de uso implementados no cambian en cada fase y son comunes. A continuación se describen:

* Cliente
    * Nuevo cliente
    * Transacción de ingreso de saldo al cliente
    * Transacción de retirada de saldo al cliente
    * Leer un cliente
    * Leer todos los clientes
* Producto
    * Crear producto
    * Transacción de ingreso de unidades al stock del producto
    * Transacción de retirada de unidades al stock del producto
    * Leer un producto
    * Leer todos los productos
* Pedido
    * Leer un pedido
    * Leer todos los pedidos
    * Creación de pedido: la implementación de este caso de uso se ha realizado igual en todas las fases. El enfoque es un pedido asincrono, de tal manera que se orquesta cada uno de los pasos a realizar para su conclusión y se realizan tareas compensatorias en caso de error. 
    
```java
public class MyCommandService extends Thread
...
    private boolean step1(Pedido pedido)
    private boolean step2(Pedido pedido)
    private boolean step3(Pedido pedido)
    private boolean step4(Pedido pedido)
 
```
### Proyecto de Test END-TO-END
Se ha creado un proyecto de test end to end con todas las posibles casuísticas que abarca el aplicativo. 

Para realizar la ejecución es necesario configurar el fichero application.properties indicando la ip y el puerto donde se encuentra escuchando el nodo principal.

Operaciones testeadas:
* Crear cliente satisfactoriamente
* Transacción de ingreso de saldo al cliente
* Transacción de retirada de saldo al cliente
* Crear producto satisfactoriamente
* Transacción de ingreso de unidades al stock del producto
* Transacción de retirada de unidades al stock del producto
* Creación de pedido - Cliente no encontrado
* Creación de pedido - Producto no encontrado
* Creación de pedido - Credito insuficiente
* Creación de pedido - Unidades de stock insuficiente
* Creación de pedido - Correcta

## FASE 1 - Monolito
En el monolito se definen 3 end points, cada uno de ellos con un caso de uso:
* ClienteController
* ProductoController
* PedidoController

Los dtos son usados de la librería común.

El despliegue se realiza sobre el puerto 8080, el cuál será el endpoint principal para sucesivas fases

## FASE 2 - Extracción de pedidos mediante el patrón strangler fig
Se realiza la extracción de la funcionalidad implementada en la clase PedidoController a un microservicio

Para ello se realizas varios pasos:
* Se modifica el monolito para quitar toda funcionalidad asociada a un pedido.
* Se crea el microservicio que va a cubrir los casos de uso del pedido. 
* Se sustituye las llamadas que se realizaba a la base de datos de clientes y productos, que se mantienen en el monolito, por un cliente web dentro del nuevo microservicio. 

```java
// Ejecución fase 1 
private boolean step1(Pedido pedido) {

    // PASO 1 - Comprobar existencia de cliente
    logger.debug("PASO 1 - Comprobar existencia de cliente");
    Optional<Cliente> cliente = this.clienteRepository.findById(pedido.getClienteId());
    ...
}
```
```java
// Ejecucion fase 2
private boolean step1(Pedido pedido) {

    // PASO 1 - Comprobar existencia de cliente
    logger.debug("PASO 1 - Comprobar existencia de cliente");

    ClienteBase cliente = this.webClient.leerCliente(pedido.getClienteId().getId());
    ...
 }
```
* El monolito se cambia de puerto al `8081`
* El microservicio se configura en el puerto `8082`
* Se implementa un apigateway sobre el puerto `8080`, que es el enrutado original del monolito de la fase 1, y se enrutan las llamadas al monolito o el microservicio según corresponda.

## FASE 3 - Extracción del notificador mediante el patrón branch by abstraction

El notificador en fases posteriores era un servicio más dentro del monolito y se implementaba en la clase `NotificadorService`. 

Para aplicar el patrón branch by abstraction se ha usado una toggle Interface. 

Para la implementación de la toggle Interface se ha creado una interface llamada `INotificadorService`. Esta interface define los mismos métodos que tiene implementada la clase `NotificadorService`

```java
// Fase 2
@Service
public class NotificadorService {

    public void notificar(ClienteTransaccion transaccion) {
        ...
    }
```


```java
// Fase 3
public interface INotificadorService {

    public void notificar(ClienteTransaccion transaccion);
    
}
```

La clase `NotificadorService` implementará la nueva interface, de tal manera que abrastraemos toda su lógica.

```java
// Fase 3
@Service("notificadorInterno")
public class NotificadorService implements INotificadorService {

    public void notificar(ClienteTransaccion transaccion) {
        ...
    }
```

En cada uno de los sitios del código donde se usaba `NotificadorService` se ha sustituido por la interface y se le ha indicado la implementación a usar, que en una primera iteración apunta a la vieja implementación.

```java
// Fase 2
@Autowired
@Resource
private NotificadorService notificadorService;
```

```java
// Fase 3
@Autowired
@Resource(name="notificadorInterno")
private INotificadorService notificadorService;
```

Se crea el microservicio para el notificador, se lleva toda su funcionalidad a él, y se configura en el puerto `8083`. No exponemos su puerto y lo enrutamos desde el inicial `8080` al nuevo. Para ello modificamos el api gateway con los nuevos endpoint creados.

Se genera una nueva clase llamada `NotificadorExterno` que también implementa la interface, pero además hacemos que se comunique con el nuevo microservicio. Para este caso se ha usado un cliente web.

```java
// Fase 3
@Service("notificadorExterno")
public class NotificadorExternoService implements INotificadorService {
    ...
}
```

Ahora estamos preparados para realizar el cambio al nuevo servicio sin alterar el comportamiento de la aplicación inicial. Para ello simplemente indicamos en todos los sitios donde se usa la implementación a usar.

```java
// Fase 3
@Autowired
// @Resource(name="notificadorInterno")
@Resource(name="notificadorExterno")
private INotificadorService notificadorService;
    ...
}
```