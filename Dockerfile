FROM openjdk:17-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 9010

CMD ["java", "-jar", "app.jar"]
