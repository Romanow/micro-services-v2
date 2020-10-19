FROM openjdk:11-jdk

VOLUME /tmp

ENV PORT=8180

ADD build/libs/warranty-service.jar warranty-service.jar

RUN bash -c 'touch /warranty-service.jar'

EXPOSE 8180

ENTRYPOINT ["java","-Dserver.port=$PORT","-Djava.security.egd=file:/dev/./urandom","-XX:+UseContainerSupport","-Xmx512m","-Xms128m","-jar","/warranty-service.jar"]