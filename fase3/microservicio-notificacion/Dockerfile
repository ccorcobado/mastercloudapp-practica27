FROM openjdk:8-jre

# copiamos el codigo compilado
COPY ./target/. /code/target/microservicio-notificacion/
WORKDIR /code/target/microservicio-notificacion/

CMD ["java", "-jar", "microservicio-notificacion-0.0.1-SNAPSHOT.jar"]