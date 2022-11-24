#!/usr/bin/env bash

set -e

#script directory
BUILD_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_VERSION=$(grep -oP 'version = "\K[^"\047]+(?=["\047])' $BUILD_DIR/../../../build.gradle.kts)

echo "Building image: network.users:$PROJECT_VERSION"


cp "$BUILD_DIR/../../../build/libs/users-$PROJECT_VERSION.jar" "$BUILD_DIR/users.jar"

docker build -t "social-users:$PROJECT_VERSION" "$BUILD_DIR"

echo "Built image: network.users:$PROJECT_VERSION"
