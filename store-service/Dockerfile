FROM openjdk:11-jdk

VOLUME /tmp

ARG PROFILE

ADD build/libs/store-service.jar store-service.jar

RUN bash -c 'touch /store-service.jar'

EXPOSE 8480

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=$PROFILE","-jar","/store-service.jar"]