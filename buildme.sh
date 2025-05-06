#!/bin/bash

CACHE_BUST=$(date +"%Y%H%M%S")
echo "Docker containers restarting with rebuild"

docker compose build

docker compose up -d
