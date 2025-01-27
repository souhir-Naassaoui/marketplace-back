# Utiliser l'image officielle de Java
FROM openjdk:17-jdk-slim AS build

# Spécifier le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier tout le code source du projet
COPY src /app/src

# Compiler le projet et créer le JAR
RUN mvn clean package -DskipTests

# Utiliser une image plus légère pour l'exécution
FROM openjdk:17-alpine

# Copier le JAR compilé depuis l'image de build
COPY --from=build /app/target/*.jar /app/my-app.jar

ENV DB_HOST=localhost
ENV DB_PORT=1433
ENV DB_NAME=marketplace
ENV DB_USER=admin
ENV DB_PASSWORD=secretpassword

# Exposer le port sur lequel ton application va écouter
EXPOSE 8080

# Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]
