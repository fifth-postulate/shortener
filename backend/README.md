# URL Shortener Backend
This is the backend for the URL Shortener project.

It is build in [Kotlin][kotlin] with [Spring Boot][boot]. It is build with [Gradle][gradle].

## Development
To run the application execute the command

```bash
SERVER_PORT=7478 ./gradlew bootRun
```

For more tasks see `./gradlew tasks`.

### Build
A docker image can be build with `make`

## Deployment
In order for the container to run succesfully the following environment variables should be available

| Variable         | Value |
|------------------+-------|
| COUCHDB_URL      | The url of the database that will be used as store |
| COUCHDB_USERNAME | Username of the user that will connect to the store |
| COUCHDB_PASSWORD | The password of the connecting user |


[kotlin]: https://kotlinlang.org/
[boot]: https://spring.io/projects/spring-boot
[gradle]: https://docs.gradle.org
