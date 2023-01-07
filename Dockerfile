FROM amazoncorretto:19

WORKDIR /.\target\ \

ADD avalon-v1-jar-with-dependencies.jar avalon-v1-jar-with-dependencies.jar

ENTRYPOINT ["java", "-jar", "avalon-v1-jar-with-dependencies.jar"]