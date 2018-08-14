FROM openjdk:8-alpine3.8

ARG GIT_SHA1="CUSTOM BUILD"

LABEL maintainers="Erik Ogan <erik@stealthymonkeys.com>"
LABEL git_sha1="${GIT_SHA1}"

COPY build/libs/booklet-builder-*-all.jar /booklet-builder-all.jar

ENTRYPOINT ["java", "-jar", "/booklet-builder-all.jar"]
