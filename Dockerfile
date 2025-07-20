# Этап сборки
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Финальный образ с Tomcat
FROM tomcat:10.1-jdk21-temurin-jammy
WORKDIR /usr/local/tomcat/webapps
COPY --from=builder /app/target/*.war ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]