FROM openjdk:11-jdk

VOLUME /tmp

ARG PROFILE
ENV SPRING_PROFILES_ACTIVE=$PROFILE

ADD build/libs/store-service.jar store-service.jar

RUN bash -c 'touch /store-service.jar'

EXPOSE 8480

ENTRYPOINT ["java","-Dserver.port=$PORT","-Djava.security.egd=file:/dev/./urandom","-XX:+UseContainerSupport","-Xmx512m","-Xms128m","-jar","/store-service.jar"]