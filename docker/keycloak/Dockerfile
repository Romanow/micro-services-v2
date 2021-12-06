FROM jboss/keycloak:15.0.1

COPY ./services-realm.json /tmp/realm.json

ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=admin
ENV KEYCLOAK_IMPORT=/tmp/realm.json
ENV DB_VENDOR=postgres
ENV JAVA_OPTS="-server -Xms512m -Xmx512m"

COPY ./docker-entrypoint.sh /opt/jboss/tools

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]
CMD ["-b", "0.0.0.0"]