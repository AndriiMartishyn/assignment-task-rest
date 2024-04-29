FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw
RUN /bin/sh mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]