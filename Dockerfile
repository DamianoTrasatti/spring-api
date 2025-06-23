# Usa un'immagine base con JDK 17 (puoi cambiare versione se serve)
FROM eclipse-temurin:17-jdk-alpine

# Crea una directory per l'app
WORKDIR /app

# Copia tutto il progetto nel container
COPY . .

# Rendi eseguibile il Maven Wrapper
RUN chmod +x ./mvnw

# Costruisci il progetto (salta i test se vuoi velocizzare il deploy)
RUN ./mvnw clean package -DskipTests

# Espone la porta usata da Spring Boot (8080 di default)
EXPOSE 8080

# Esegui il file JAR risultante
CMD ["sh", "-c", "java -jar target/*.jar"]
