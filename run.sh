#!/bin/bash

TAG="latest"

while getopts "t:" opt; do
  case ${opt} in
    t ) TAG=$OPTARG ;;
    \? ) echo "Usage: $0 -t <tag>" ; exit 1 ;;
  esac
done

echo "Starting services with tag: ${TAG}..."
IMAGE_TAG=${TAG} docker compose up -d

echo "Services started!"
echo "App: http://localhost:8080"
echo "Grafana: http://localhost:3000 (admin/admin)"