FROM openjdk:21

EXPOSE 8080

# 어플리케이션 JAR 복사 및 설정
ARG JAR_FILE=build/libs/auto-trader-app.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul

# 어플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "./app.jar"]