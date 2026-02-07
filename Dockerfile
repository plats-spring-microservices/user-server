FROM alpine/java:22-jre
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
ARG JAR=target/*.jar
COPY ${JAR} app.jar
EXPOSE 8070
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]

