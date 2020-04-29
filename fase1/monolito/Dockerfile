FROM openjdk:8-jre

# copiamos el codigo compilado
COPY ./target/. /code/target/
WORKDIR /code/target/

CMD ["java", "-jar", "monolito-0.0.1-SNAPSHOT.jar"]