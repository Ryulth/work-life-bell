FROM java:8
LABEL maintainer="dev.ryulth@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/worklifebell-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} worklifebell-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/worklifebell-springboot.jar"]
