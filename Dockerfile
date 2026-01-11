# build stage (optional if using multi-stage)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy built jar from builder
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
