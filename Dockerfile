FROM amazoncorretto:19

ADD avalon-v1-jar-with-dependencies.jar avalon-v1-jar-with-dependencies.jar

ENTRYPOINT ["java", "-jar", "avalon-v1-jar-with-dependencies.jar"]