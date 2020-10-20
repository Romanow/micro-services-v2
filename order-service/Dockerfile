FROM openjdk:11-jdk

VOLUME /tmp

ENV PORT=8380
EXPOSE 8380

ADD build/libs/order-service.jar order-service.jar

RUN bash -c 'touch /order-service.jar'

ENTRYPOINT java -Dserver.port=$PORT -Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -Xmx512m -Xms128m -jar /order-service.jar