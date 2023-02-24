#!/usr/bin/env bash

set -e

#script directory
BUILD_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_VERSION=$(grep -oP 'version = "\K[^"\047]+(?=["\047])' $BUILD_DIR/../../build.gradle.kts)

echo "Building image: social-users:$PROJECT_VERSION"


cp "$BUILD_DIR/../../build/libs/users-$PROJECT_VERSION.jar" "$BUILD_DIR/users.jar"

docker build . --tag "ghcr.io/aigetcode/social-users:$PROJECT_VERSION"
docker run "ghcr.io/aigetcode/social-users:$PROJECT_VERSION"
docker push "ghcr.io/aigetcode/social-users:$PROJECT_VERSION"

echo "Built image: social-users:$PROJECT_VERSION"
