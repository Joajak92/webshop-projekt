FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src
RUN chmod +x mvnw

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    ./mvnw -B -DskipTests clean package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]