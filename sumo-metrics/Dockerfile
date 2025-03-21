FROM openjdk:17-jdk-slim
WORKDIR /opt/sumo-metrics
COPY target/sumo-metrics-1.0-SNAPSHOT.jar /opt/sumo-metrics/
ENTRYPOINT ["java", "-jar", "sumo-metrics-1.0-SNAPSHOT.jar"]
