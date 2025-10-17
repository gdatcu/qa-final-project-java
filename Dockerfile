# Etapa 1: Folosim o imagine de bază care conține Maven și JDK
# Alegem o versiune specifică pentru reproductibilitate
FROM maven:3.8.5-openjdk-17-slim AS builder

# Setăm directorul de lucru în container
WORKDIR /app

# Copiem fișierul de management al proiectului
COPY pom.xml .

# Optimizare: descărcăm dependențele înainte de a copia codul sursă.
# Acest pas se va refolosi din cache dacă pom.xml nu se schimbă.
RUN mvn dependency:go-offline

# Copiem restul codului sursă al proiectului
COPY src ./src

# Etapa 2: Rularea testelor
# Comanda care va fi executată la pornirea containerului
CMD ["mvn", "test"]