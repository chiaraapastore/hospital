FROM quay.io/keycloak/keycloak:22.0.3 as builder
#immagine docker

#installazione web server nginx all'interno dell'immagine usando dnf
RUN dnf install -y nginx \
  	&& dnf clean all \
  	&& rm -rf /var/cache/yum

# Configure a database vendor, imposta la variabile ambiente su postgres indicando che keycloak utilizzerà un database postgre
ENV KC_DB=postgres

#definisce la directory di lavoro
WORKDIR /opt/keycloak
# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
FROM quay.io/keycloak/keycloak:latest
#Inizia una nuova fase di build, utilizzando l'immagine Keycloak più recente
COPY --from=builder /opt/keycloak/ /opt/keycloak/
#Copia i file da /opt/keycloak/ creati nella prima fase "builder" nella stessa directory dell'immagine finale

COPY ./themes/my-theme /opt/keycloak/themes/my-theme
#permette di personalizzare l'aspetto di keycloak, copio un tema personalizzato dal percorso locale

ENTRYPOINT [ "opt/keycloak/bin/kc.sh"]
#Definisce il comando che sarà eseguito quando il container partirà, kc.sh è lo script che avvia il server Keycloak