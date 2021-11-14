# Rocket Container #

A coding challenge for Bottle Rocket's API Integration Software Engineer role.
Instructions and requirements can be found
[here](https://bottlerocketstudios.stoplight.io/docs/rocket-container).

## About ##

This project is written in Kotlin and uses Ktor for handling incoming REST
requests and making outgoing requests.

### Technologies Used ###

1. **Kotlin:** Programming language
2. **Gradle:** Project structure and dependency management
3. **Ktor:** HTTP client and server
4. **Logback:** Application logging
5. **JUnit:** Unit and integration tests
6. **Jacoco:** Test coverage reporting and verification

## Building ##

This project was built and test using JDK 16. The minimum required version of
Java is 13 or the kotlin-cache dependency will throw a class version error.

## Running ##

This project can be run using the `run` gradle target.

## Testing ##

This project has 2 test targets, `integrationTest` and `test`. The gradle
`check` task will run both.

## Roadmap ##

[JIRA](https://bitbucket.org/phrionhaus/bottle-rocket-api-test/jira?site=98e96332-17e7-4ccb-9737-78b75ec34de8&statuses=new&statuses=indeterminate&sort=-updated&page=1)
