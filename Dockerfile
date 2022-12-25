FROM openjdk:11

WORKDIR /

COPY target/sac-0.1.0-SNAPSHOT-standalone.jar sac.jar
EXPOSE 3000

CMD java -jar sac.jar
