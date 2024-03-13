#!/usr/bin/env bash

REPOSITORY="/home/ec2-user/app"
JAR_FILE="$REPOSITORY/spring-webapp.jar"
TIME_NOW=$(date +%c)

APP_LOG="$REPOSITORY/application.log"
ERROR_LOG="$REPOSITORY/error.log"
DEPLOY_LOG="$REPOSITORY/deploy.log"

IDLE_PORT=$(find_idle_port)

echo "$TIME_NOW > Build 파일 복사" >> $DEPLOY_LOG
echo "$TIME_NOW > cp $REPOSITORY/*.jar $REPOSITORY/"

cp $REPOSITORY/build/libs/*.jar $JAR_FILE     # 새로운 jar file 계속 덮어쓰기

echo "$TIME_NOW > $JAR_FILE 에 실행권한 추가" >> $DEPLOY_LOG
chmod +x $JAR_NAME

echo "$TIME_NOW > JAR Name: $JAR_FILE" >> $DEPLOY_LOG
echo "$TIME_NOW > $JAR_FILE 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG