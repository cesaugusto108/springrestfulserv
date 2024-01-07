FROM eclipse-temurin:17.0.9_9-jre
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV PORT=port
ENV HOST=db
ENV DBPORT=port
ENV USERNAME=root
ENV PASSWORD=pass
ENV USERPASSWORD=pass
ENTRYPOINT ["java", "-DSERVER-PORT=${PORT}", "-DDB-HOST=${HOST}", "-DDB-PORT=${DBPORT}", \
            "-DDB-USERNAME=${USERNAME}", "-DDB-PASSWORD=${PASSWORD}", \
            "-DUSER-PASSWORD=${USERPASSWORD}", "-jar", "/app.jar"]
