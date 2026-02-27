FROM maven:3.9.6-eclipse-temurin-8 AS builder
WORKDIR /build

COPY fontes-estruturado/principal /build/principal

RUN mvn -f /build/principal/pom.xml -pl obtemoportunidadelinkedin -am -DskipTests clean install \
 && mvn -f /build/principal/obtemoportunidadelinkedin/pom.xml -DskipTests dependency:copy-dependencies -DincludeScope=runtime

FROM eclipse-temurin:8-jre
WORKDIR /app

RUN apt-get update \
 && apt-get install -y --no-install-recommends \
    chromium \
    chromium-driver \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libatspi2.0-0 \
    libcups2 \
    libdrm2 \
    libgbm1 \
    libglib2.0-0 \
    libnss3 \
    libpango-1.0-0 \
    libpangocairo-1.0-0 \
    libu2f-udev \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxkbcommon0 \
    libxrandr2 \
    xdg-utils \
 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /build/principal/obtemoportunidadelinkedin/target/classes /app/classes
COPY --from=builder /build/principal/obtemoportunidadelinkedin/target/dependency /app/libs

ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver
ENV JAVA_OPTS=""

CMD ["/bin/sh", "-c", "java $JAVA_OPTS -cp '/app/classes:/app/libs/*' gerador.obtemoportunidadelinkedin.app.ObtemOportunidadeLinkedin"]
