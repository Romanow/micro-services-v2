FROM jboss/keycloak:15.0.1

ADD ./services-realm.json /tmp/realm.json

ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=admin
ENV KEYCLOAK_IMPORT=/tmp/realm.json
ENV DB_VENDOR=postgres