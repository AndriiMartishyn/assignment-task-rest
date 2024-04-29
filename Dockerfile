FROM openjdk:17-oracle

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw
RUN /bin/sh mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]