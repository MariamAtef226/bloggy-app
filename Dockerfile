FROM eclipse-temurin:17

LABEL maintainer="Mariam Atef mariamatef226@gmai.com"
WORKDIR /app

COPY target/bloggy-0.0.1-SNAPSHOT.jar /app/bloggy-docker.jar

ENTRYPOINT ["java","-jar","bloggy-docker.jar"]