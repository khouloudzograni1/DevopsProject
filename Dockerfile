FROM openjdk:17-alpine
EXPOSE 8089
COPY target/eventsProject-1.0.0-SNAPSHOT.jar eventsProject.jar
ENTRYPOINT ["java", "-jar", "eventsProject.jar"]