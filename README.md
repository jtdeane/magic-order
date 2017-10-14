magic-order
=======================

Built with Java 8+, Spring Boot (1.5.6) and EHCache (2.10.3)

Tested with JUnit (4.12)

Executes with Spring Boot from target directory

`java -jar magic-order-1.0.0.jar`

OR

`mvn spring-boot:run -Drun.arguments="-Xmx256m,-Xms128m"`

OR 

Build & execute in Docker

`docker build -t magic-order:latest .`

`docker run -d -p 8080:8080 -e JAVA_OPTS='-Xmx256m -Xms128m’ magic-order:latest`

Includes JMeter (2.13) Functional Test