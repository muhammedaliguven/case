FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} springbootdockerapplication.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/springbootdockerapplication.jar"]