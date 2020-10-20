FROM openjdk:11-jdk

VOLUME /tmp

ENV PORT=8480
EXPOSE 8480

ADD build/libs/store-service.jar store-service.jar

RUN bash -c 'touch /store-service.jar'

ENTRYPOINT ["java","-Dserver.port=${PORT:-8480}","-Djava.security.egd=file:/dev/./urandom","-XX:+UseContainerSupport","-Xmx512m","-Xms128m","-jar","/store-service.jar"]