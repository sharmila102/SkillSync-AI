FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

RUN ls -la target

CMD ["java", "-jar", "target/skillsync-0.0.1-SNAPSHOT.jar"]