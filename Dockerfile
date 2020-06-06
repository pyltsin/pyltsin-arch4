FROM bellsoft/liberica-openjdk-alpine:11
MAINTAINER MPYLTSIN
WORKDIR /tmp
COPY build/libs/arch4-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/tmp/app.jar"]

