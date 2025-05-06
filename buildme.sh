#!/bin/bash

git fetch origin
git reset --hard origin/main
#git pull

chmod +x -- "$0"

export CACHE_BUST=$(date +"%Y%H%M%S")
#echo "Docker containers restarting with rebuild ${CACHE_BUST}"
docker compose build --build-arg CACHE_BUST=${CACHE_BUST}

docker compose up -d

docker compose logs -f events-backend