FROM openjdk:8
COPY target/sitemap-mapper-0.0.1-SNAPSHOT.war app.war
#ENV AWS_DB=$AWS_DB
ENTRYPOINT ["java","-jar","/app.war"]