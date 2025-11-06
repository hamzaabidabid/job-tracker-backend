# Étape 1: Utiliser une image de base qui contient Java (JDK 17) et Maven pour construire le projet.
# On la nomme 'build' pour pouvoir y faire référence plus tard.
FROM maven:3.8.5-openjdk-17 AS build


COPY . .

RUN mvn clean package -DskipTests


FROM openjdk:17-slim


COPY --from=build /target/job_tracker-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8082


ENTRYPOINT ["java", "-jar", "app.jar", "--spring.datasource.url=jdbc:postgresql://postgres-db:5432/job_tracker"]