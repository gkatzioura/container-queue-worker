FROM openjdk:8-jre-alpine
MAINTAINER Gkatziouras Emmanouil (gkatzioura@gmail.com)

RUN apk add --update curl && rm -rf /var/cache/apk/*

ADD https://s3-eu-west-1.amazonaws.com/softwaremill-public/elasticmq-server-0.9.3.jar /elasticmq/server.jar

ENTRYPOINT ["java", "-jar", "/elasticmq/server.jar"]

