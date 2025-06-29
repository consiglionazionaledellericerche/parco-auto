    FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

COPY cert/  /cert

RUN $JAVA_HOME/bin/keytool -import -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt -file "/cert/GEANT TLS RSA 1.crt" -alias geant

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh jhipster
WORKDIR /home/jhipster

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown jhipster:jhipster entrypoint.sh
USER jhipster

ADD target/*.war app.war

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

