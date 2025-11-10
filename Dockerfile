## Étape 1: Utiliser une image de base qui contient Java (JDK 17) et Maven pour construire le projet.
## On la nomme 'build' pour pouvoir y faire référence plus tard.
#FROM maven:3.8.5-amazoncorretto-17 AS build
#
#
#COPY . .
#
#RUN mvn clean package -DskipTests
#
#
#FROM amazoncorretto:17-alpine
#
#
#COPY --from=build /target/job_tracker-0.0.1-SNAPSHOT.jar app.jar
#
#
#EXPOSE 8082
#
#
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=jdbc:postgresql://postgres-db:5432/job_tracker"]

# On part directement de l'image Java de production
FROM amazoncorretto:17-alpine

# On copie le .jar qui a été compilé par Jenkins
COPY target/job_tracker-0.0.1-SNAPSHOT.jar app.jar

# On expose le port
EXPOSE 8082

# On définit la commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]