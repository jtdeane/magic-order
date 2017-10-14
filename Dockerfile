FROM openjdk:8u141-jdk-slim
MAINTAINER jeremydeane.net
EXPOSE 8080
RUN mkdir /app/
COPY target/magic-order-1.0.0.jar /app/
ENTRYPOINT exec java $JAVA_OPTS -jar /app/magic-order-1.0.0.jar