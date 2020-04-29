cd .\monolito
mvn package -DskipTests=true -B

cd ..
docker-compose -f docker-compose.yml up --build