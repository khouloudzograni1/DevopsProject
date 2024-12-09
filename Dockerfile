FROM openjdk:17-alpine
EXPOSE 8089
COPY target/eventsProject-1.0.0-SNAPSHOT.jar eventsproject.jar
ENTRYPOINT ["java", "-jar", "eventsproject.jar"]