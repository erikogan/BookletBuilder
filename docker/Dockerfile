#FROM openjdk:8-alpine3.8
FROM openjdk:jre-slim

ARG GIT_SHA1="CUSTOM BUILD"

LABEL maintainers="Erik Ogan <erik@stealthymonkeys.com>"
LABEL git_sha1="${GIT_SHA1}"

COPY build/libs/BookletBuilder-all.jar /

ENTRYPOINT ["java", "-jar", "/BookletBuilder-all.jar"]
