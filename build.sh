#!/bin/bash

TAG="latest"

while getopts "t:" opt; do
  case ${opt} in
    t ) TAG=$OPTARG ;;
    \? ) echo "Usage: $0 -t <tag>" ; exit 1 ;;
  esac
done

echo "🔨 Building culinary-exchange:${TAG}..."
docker build -t culinary-exchange:${TAG} ./CulinaryService

if [ $? -eq 0 ]; then
    echo "Build successful: culinary-exchange:${TAG}"
else
    echo "Build failed!"
    exit 1
fi