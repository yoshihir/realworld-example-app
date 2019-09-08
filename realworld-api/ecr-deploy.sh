#!/usr/bin/env bash

set -ex

REPOSITORY_NAME="realworld/app"
GIT_HASH=$(git rev-parse --short HEAD)
NOW_DATE=$(date +'%Y%m%d')

# build
sbt dist

# deploy
$(aws ecr get-login --no-include-email --region ap-northeast-1)
REPOSITORY_URI=$(aws ecr describe-repositories --repository-names $REPOSITORY_NAME --region ap-northeast-1|jq -r .repositories[].repositoryUri)

docker build -t ${REPOSITORY_URI}:${NOW_DATE}-${GIT_HASH} .
docker tag ${REPOSITORY_URI}:${NOW_DATE}-${GIT_HASH} ${REPOSITORY_URI}:latest

docker push ${REPOSITORY_URI}:${NOW_DATE}-${GIT_HASH}
docker push ${REPOSITORY_URI}:latest
