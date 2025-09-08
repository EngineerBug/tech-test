# Java Technical Test

## Turning it on

Compile and test the project: ./gradlew build

Compile without testing: ./gradlew -x test

Run the project: ./gradlew bootrun

## Using the Project

Project runs on localhost:8090

Endpoints:
- GET /dataserver/isok ; returns true if the server is running correctly
- POST /dataserver/pushdata ; sends a DataEnvelope to be persisted in the database and the organisation's datalake
- GET /dataserver/data/{blockType} ; gets all datas in the database with the specified type (encoded as json DataEnvelopes)
- PATCH /dataserver/update/{name}/{newBlockType} ; updates the type of a single data to the specified type
- POST /hadoopserver/pushbigdata ; persists a DataEnvelope in the organisations datalake (WARNING; service is new, may be unstable)

Project also comes with a Client class which contains several methods for sending requests to the server.

# Specification

This repository contains a Java technical test project, focusing on a simplified microservice-based architecture. The project involves completing unfinished functionality related to saving arbitrary bank data blocks to a database and interacting with external services.

## Scenario Overview

The core of this test is a Spring Boot application that simulates a data platform. It includes a `client` component responsible for interacting with a `server` component. The `server` handles data persistence and interacts with other simulated services. The primary goal is to complete several tasls to implement and enhance the data flow, persistence, and API interactions.

## Guidelines

* **Technology Stack**: Java, Spring Boot, H2 Database (in-memory embedded).
* **Allowed Resources**: Candidates are permitted to use reference materials.
* **Dependencies**: No new build dependencies are allowed.
* **Code Quality**: Solutions must adhere to best practices, emphasizing code reusability, leveraging the Spring Framework, and minimizing code.
* **Production Readiness**: Treat this project as production code. High code coverage, thorough test scenarios, and consideration of potential microservice/API architecture issues are crucial.
* **Refactoring**: Existing code can be refactored if deemed not production-ready.
* **Database Transactions**: Transaction boundaries must be carefully considered.
* **Deliverables**:
    * The final solution must compile.
    * All tests must pass.
    * The Spring Boot application should start successfully.
    * The solution should be pushed to a personal GitHub repository, and its details emailed as instructed.

## Exercises

The following exercises outline the unfinished functionality to be completed:

### Exercise 1: Push Data from Client to Server

Update the `client` to push a block of data via an existing HTTP API on the test `server`.

* **Client Method**: `org.hmxlabs.techtest.client.component.impl.ClientImpl.pushData()`
* **Server Endpoint**: Exposed in `org.hmxlabs.techtest.server.api.controller.ServerController`

### Exercise 2: MD5 Checksum Calculation and Validation

Add functionality to the `server` to calculate and persist an MD5 checksum of the body of an incoming data block. The endpoint must return to the client whether the hash matches a client-provided checksum. The system policy dictates that invalid data (mismatched checksums) is considered wasted space.

### Exercise 3: Retrieve Blocks by Type

Expand `server` functionality to expose a new API endpoint to get all persisted blocks that have a given `blockType`. Update the `client` to call this new endpoint.

### Exercise 4: Update Block Type

Add a new endpoint to the `server` with an appropriate HTTP verb to update an existing block's `blockType`. The block to update can be identified uniquely in the persistence store by its `blockName`. Add at least API input validation for the `blockName`. Update the `client` to call the new update endpoint.

### Exercise 5: Push Data to Hadoop Data Lake

In the existing `server` push data endpoint, in addition to persistence, add functionality to push data to the bank’s Hadoop data lake.

* **Data Lake Service Endpoint**: `http://localhost:8090/hadoopserver/pushbigdata`
* **Request Method**: `POST`
* **Payload**: The `POST` request body should be a string containing the data block.
* **Considerations**: The data lake is a new service; instability and long-running calls can be expected. Asynchronous communication or robust error handling should be considered.