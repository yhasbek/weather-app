# ---------- 1. Aşama: Derleme ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Önce sadece Maven dosyalarını kopyala ve bağımlılıkları indir
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Sonra kaynak kodu kopyala, testleri çalıştır ve paketle
COPY src ./src
RUN ./mvnw package -B

# ---------- 2. Aşama: Çalıştırma ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]