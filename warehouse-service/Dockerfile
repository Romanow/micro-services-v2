FROM openjdk:11-jdk

VOLUME /tmp

ENV PORT=8280

ADD build/libs/warehouse-service.jar warehouse-service.jar

RUN bash -c 'touch /warehouse-service.jar'

EXPOSE 8280

ENTRYPOINT ["java","-Dserver.port=$PORT","-Djava.security.egd=file:/dev/./urandom","-XX:+UseContainerSupport","-Xmx512m","-Xms128m","-jar","/warehouse-service.jar"]