version: '2'
services:
    parcoauto-app:
        image: docker.si.cnr.it/parco-auto
        links:
            - parcoauto-postgresql:parcoauto-postgresql
        environment:
            # - _JAVA_OPTIONS=-Xmx512m -Xms256m
            - SPRING_PROFILES_ACTIVE=prod,swagger
            - SPRING_DATASOURCE_URL=jdbc:postgresql://parcoauto-postgresql:5432/parcoauto
            - JHIPSTER_SLEEP=10 # gives time for the database to boot before the application
        ports:
            - 8080:8080
        labels:
            SERVICE_NAME: "##{SERVICE_NAME}##"
    parcoauto-postgresql:
        extends:
            file: postgresql.yml
            service: parcoauto-postgresql
