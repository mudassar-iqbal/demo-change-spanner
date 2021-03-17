FROM adoptopenjdk/openjdk11:alpine-jre

# maintainer info
LABEL maintainer="mudassar1.iqbal.babu@infogain.com"

# add volume pointing to /tmp
VOLUME /tmp

# Make port 9001 available to the world outside the container
EXPOSE 9000

# application jar file when packaged
ARG jar_file=target/demo-change-spanner.jar

# add application jar file to container
COPY ${jar_file} demo-change-spanner.jar

# run the jar file
ENTRYPOINT ["java", "-jar", "demo-change-spanner.jar"]