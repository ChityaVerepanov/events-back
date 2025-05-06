#!/bin/bash

git fetch origin
git reset --hard origin/main
#git pull
echo "Repository loaded..."

chmod +x -- "$0"
echo "Script permission restored. If failed? start manually: 'chmod +x buildme.sh'"

export CACHE_BUST=$(date +"%Y%H%M%S")
#echo "Docker containers restarting with rebuild ${CACHE_BUST}"
docker compose build --build-arg CACHE_BUST=${CACHE_BUST}

docker compose up -d

docker compose logs -f events-backend