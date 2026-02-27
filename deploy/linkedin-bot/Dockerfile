FROM maven:3.9.6-eclipse-temurin-8 AS builder
WORKDIR /build

COPY fontes-estruturado/principal /build/principal

RUN mvn -f /build/principal/pom.xml -pl obtemoportunidadelinkedin -am -DskipTests clean install \
 && mvn -f /build/principal/obtemoportunidadelinkedin/pom.xml -DskipTests dependency:copy-dependencies -DincludeScope=runtime

FROM mcr.microsoft.com/playwright/java:v1.55.0-noble
WORKDIR /app

COPY --from=builder /build/principal/obtemoportunidadelinkedin/target/classes /app/classes
COPY --from=builder /build/principal/obtemoportunidadelinkedin/target/dependency /app/libs

ENV JAVA_OPTS=""

CMD ["/bin/sh", "-c", "if [ -z \"$CHROME_BINARY\" ]; then CHROME_BINARY=$(find /ms-playwright -type f -path '*/chrome-linux/chrome' | head -n 1); export CHROME_BINARY; fi; java $JAVA_OPTS -cp '/app/classes:/app/libs/*' gerador.obtemoportunidadelinkedin.app.ObtemOportunidadeLinkedin"]
