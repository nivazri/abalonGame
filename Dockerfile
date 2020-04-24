FROM adoptopenjdk/maven-openjdk11

WORKDIR /code
COPY pom.xml .

RUN mvn dependency:resolve
RUN mvn verify

ADD src /code/src

RUN ls

RUN mvn package

RUN ls target
RUN which java

CMD ["java", "-jar", "target/abalon-game-server-jar-with-dependencies.jar"]