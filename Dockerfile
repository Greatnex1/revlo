FROM eclipse-temurin:17-alpine
MAINTAINER revlo
EXPOSE 8060
ADD target/revlo-0.0.1-SNAPSHOT.jar revlo.jar
ENTRYPOINT ["java", "-jar", "/revlo.jar"]