FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
# Tests already run in CI.
RUN ./mvnw -B -q package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /app/target/*.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
