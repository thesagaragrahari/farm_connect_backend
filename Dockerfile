# -------- build stage --------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# -------- runtime stage --------
FROM eclipse-temurin:17-jre

# 
THIS LINE FIXES YOUR PROBLEM
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

WORKDIR /app

# copy jar from build stage (CORRECT WAY)
COPY --from=build /app/target/krishisetu-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
