# Project has been cloned from there: https://github.com/hardikSinghBehl/firebase-integration-spring-boot



Before building you must clone repository, then create file **src\main\resources\firebase.yml** which contains parameters that Firebase web-console provide as downloaded json-file. Please view **src\main\resources\firebase-example.yml**

When file **src\main\resources\firebase.yml** is created and placed, you can set permissions and execute this script:

```
chmod +x buildme.sh
buildme.sh
```

# Permission matrix

|                                          | Public | ADMIN |   USER    |  CREATOR  |
| ---------------------------------------- | :----: | :---: | :-------: | :-------: |
| POST /api/v1/events                      |        |   +   |     -     |     +     |
| GET /api/v1/events/{eventId}             |   +    |       |           |           |
| DELETE /api/v1/events                    |        |   +   |     -     | + (owned) |
| PUT /api/v1/events                       |        |   +   |     -     | + (owned) |
| GET /api/v1/events/                      |   +    |       |           |           |
| GET /api/v1/events/byCreator             |        |   +   |     -     |     +     |
| POST /api/v1/events/favorite/{eventId}   |        |   +   |     +     |     +     |
| DELETE /api/v1/events/favorite/{eventId} |        |   +   |     +     |     +     |
| POST /api/v1/events/planned/{eventId}    |        |   +   |     +     |     +     |
| DELETE /api/v1/events/planned/{eventId}  |        |   +   |     +     |     +     |
| POST /api/v1/images/upload               |        |   +   |     -     |     +     |
| GET /api/v1/images/{fileName}            |   +    |       |           |           |
| POST /api/v1/users                       |   +    |       |           |           |
| POST /api/v1/users/login                 |   +    |       |           |           |
| GET /api/v1/users                        |        |   +   |     -     |     -     |
| GET /api/v1/users/user_details           |        |   +   |     +     |     +     |
| PUT /api/v1/users/{userId}               |        |   +   | + (owned) | + (owned) |













