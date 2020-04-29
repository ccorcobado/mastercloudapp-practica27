FROM openjdk:8-jre

# copiamos el codigo compilado
COPY ./target/. /code/target/microservicio-pedido/
WORKDIR /code/target/microservicio-pedido/

CMD ["java", "-jar", "microservicio-pedido-0.0.1-SNAPSHOT.jar"]