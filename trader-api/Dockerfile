FROM openjdk:21

EXPOSE 8080

# 어플리케이션 JAR 복사 및 설정
ARG JAR_FILE=trader-api/build/libs/auto-trader-app.jar
VOLUME /var/log
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul

# 어플리케이션 실행
ENTRYPOINT ["java", "-jar", "./app.jar"]