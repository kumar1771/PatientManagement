# ---- build stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- run stage ----
FROM openjdk:21-jdk AS runner
WORKDIR /app
# Use the correct jar name
COPY --from=builder /app/target/billing-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 4001
EXPOSE 9001
ENTRYPOINT ["java","-jar","app.jar"]
