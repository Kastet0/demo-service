# ---------- Этап 1: сборка ----------
# Образ с Maven и JDK 21. "AS build" — имя этапа, по нему ссылаемся ниже
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Сначала только pom.xml — и скачиваем зависимости
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Потом код — и собираем jar
COPY src ./src
RUN mvn -B package -DskipTests

# ---------- Этап 2: запуск ----------
# Лёгкий образ только с JRE (без компилятора и Maven)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Забираем из этапа build только готовый jar
COPY --from=build /build/target/app.jar app.jar

# Документируем порт, который слушает приложение
EXPOSE 8080

# Команда запуска контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]
