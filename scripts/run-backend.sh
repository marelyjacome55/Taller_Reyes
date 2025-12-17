#!/usr/bin/env bash
set -euo pipefail

# Simple helper to run the backend JAR using environment variables.
# It will check that required env vars are present and then run the JAR.

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
JAR_PATH="$ROOT_DIR/backend/target/cotizaciones-0.0.1-SNAPSHOT.jar"

if [ ! -f "$JAR_PATH" ]; then
  echo "Error: JAR not found at $JAR_PATH"
  echo "Build first with: mvn -f $ROOT_DIR/backend/pom.xml -DskipTests package"
  exit 2
fi

if [ -z "${SPRING_DATASOURCE_URL-}" ] || [ -z "${SPRING_DATASOURCE_USERNAME-}" ] || [ -z "${SPRING_DATASOURCE_PASSWORD-}" ]; then
  echo "One or more required env vars are missing. Please export:"
  echo "  SPRING_DATASOURCE_URL"
  echo "  SPRING_DATASOURCE_USERNAME"
  echo "  SPRING_DATASOURCE_PASSWORD"
  echo "Example:"
  echo "  export SPRING_DATASOURCE_URL='jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?sslmode=require&connectTimeout=10&socketTimeout=30&prepareThreshold=0'"
  echo "  export SPRING_DATASOURCE_USERNAME='postgres'"
  echo "  export SPRING_DATASOURCE_PASSWORD='<YOUR_PASSWORD>'"
  exit 3
fi

echo "Starting backend JAR: $JAR_PATH"
echo "Using SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}"

# Run the JAR
exec java -jar "$JAR_PATH"
