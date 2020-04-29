cd .\monolito
mvn package -DskipTests=true -B

cd ..
cd .\api-gateway
mvn package -DskipTests=true -B

cd ..
cd .\microservicio-pedido
mvn package -DskipTests=true -B

cd ..
cd .\microservicio-notificacion
mvn package -DskipTests=true -B

cd ..
docker-compose -f docker-compose.yml up --build