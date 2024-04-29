
## Table of Contents
- [Introduction](#introduction)
- [Functionality](#functionality)
- [Technologies](#technologies)
## Introduction
Test-assignment project that provides REST API for users handling.
## Technologies
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://openjdk.org/)
[![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)

## Functionality

Using Users API it is possible to perform following operations
- **/POST** receives DTO -> validates -> return DTO with Location header and CREATED status
- **/PUT/id** receives DTO -> validates -> modify entire entity ->  return DTO with OK status
- **/PATCH/id** receives DTO -> copy not null values on entity from DTO -> validates entity -> return DTO with applied patch and OK status
- **/DELETE/id** receives id ->  validates -> remove entire entity ->  return NOT FOUND or NO CONTENT
- **/GET** receives dates range -> validates -> return List of DTO with OK status

Project main points:
- Mostly all validations are carried out on with @Valid and @Groups and controller level
- Api errors handled on controller level
- Exceptions for validation and input param handled in Global Exception and return useful response
- User has also validation, just to follow a best practice even without ORM
- Database layer is implemented as Map<Long,User>
- Controller tested for basic workflow with mocking userService
- UserService partially tested with mocking dao and modelMapper

