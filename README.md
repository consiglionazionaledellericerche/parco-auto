# Parcoauto

## Sistema per l’inserimento del Parco Auto del Consiglio Nazionale delle Ricerche

## Avvio locale
### Prerequisiti
- Installazione di Apache Maven versione 3
- Git
- Java 8
- Docker

### Clonare il repository

```bash
git clone https://github.com/consiglionazionaledellericerche/parco-auto.git
cd parco-auto
```

Per avviare il sistema di autenticazione 

```bash
docker build -f Dockerfile.keycloak . -t keycloak-parcoauto:latest && docker run -p 8180:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin keycloak-parcoauto:latest
```

### Comandi
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=showcase,swagger
```

L'applicazionre sarà attiva alla seguente URL: <http://localhost:8080>, per accedere sarà ncessario autenticarsi con le seguenti credenziali:

**username**: parco.auto

**password**: cambiami

