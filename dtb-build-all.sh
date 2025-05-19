#!/bin/bash

set -e

echo "Building customer-service..."
cd customer-service
mvn clean package -DskipTests
docker build -t customer-service:local .
cd ..

echo "Building account-service..."
cd account-service
mvn clean package -DskipTests
docker build -t account-service:local .
cd ..

echo "Building card-service..."
cd card-service
mvn clean package -DskipTests
docker build -t card-service:local .
cd ..

echo "----All services built and Docker images created successfully!-----"
