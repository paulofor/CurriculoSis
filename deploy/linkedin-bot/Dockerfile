FROM maven:3.9.6-eclipse-temurin-8 AS builder
WORKDIR /build

COPY fontes-estruturado/principal /build/principal

RUN mvn -f /build/principal/pom.xml -pl obtemoportunidadelinkedin -am -DskipTests clean package \
 && mvn -f /build/principal/obtemoportunidadelinkedin/pom.xml -DskipTests dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:8-jre
WORKDIR /app

RUN apt-get update \
 && apt-get install -y --no-install-recommends chromium chromium-driver fonts-liberation \
 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /build/principal/obtemoportunidadelinkedin/target/classes /app/classes
COPY --from=builder /build/principal/obtemoportunidadelinkedin/target/dependency /app/libs

ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver
ENV JAVA_OPTS=""

CMD ["/bin/sh", "-c", "java $JAVA_OPTS -cp '/app/classes:/app/libs/*' gerador.obtemoportunidadelinkedin.app.ObtemOportunidadeLinkedin"]
