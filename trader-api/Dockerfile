FROM openjdk:21-slim

# 필수 패키지 설치
RUN apt-get update && apt-get install -y wget curl unzip

# 크롬 설치
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get install -y ./google-chrome-stable_current_amd64.deb
RUN rm ./google-chrome-stable_current_amd64.deb

# 작업 디렉토리를 설정
WORKDIR /app

# 로그 디렉토리 생성
RUN mkdir -p /app/logs /app/logs/archived /app/iamges

# 포트 오픈
EXPOSE 8080

# 어플리케이션 JAR 복사 및 설정
ARG JAR_FILE=trader-api/build/libs/auto-trader-app.jar
COPY ${JAR_FILE} /app/app.jar
ENV TZ=Asia/Seoul

# 어플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]