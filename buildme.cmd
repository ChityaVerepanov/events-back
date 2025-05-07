cd /d %~dp0

set CACHE_BUST=%date:~-4%%time:~0,2%%time:~3,2%%time:~6,2%
docker compose build --build-arg CACHE_BUST=${CACHE_BUST}
docker compose up -d
docker compose logs -f events-backend

@pause