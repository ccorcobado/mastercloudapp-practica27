FROM openjdk:8-jre

# copiamos el codigo compilado
COPY ./target/. /code/target/api-gateway/
WORKDIR /code/target/api-gateway/

CMD ["java", "-jar", "api-gateway-0.0.1-SNAPSHOT.jar"]