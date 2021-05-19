# Tuition Reimbursement Management System

## Project Description

The Inventory Management System allows for enterprises to track inventory sold for every location they have and automatically order products needed for eveystore based on sales and inventory data. 

## Technologies Used

* Java 8
* Javalin
* Cassandra NoSQL
* JUnit
* Mockito
* Maven

## Features

* Employees of all levels can login and out
* Employees can submit a request for reimbursement with funds auto-calculated from data provided
* Submissions must follow certain business rules or be automatically rejected
* Hierarchy for aprroval
* Certain approval steps can be skipped by submitting course documents
* Certain approval steps will be skipped after a long enough time without approval
* Submission will be marked urgent within 1 week of course start date
* Only the appropiate person at each stage can view the request and files submitted

## Usage

> Clone Repository, setup Amazon Keyspaces, setup Amazon truststore, setup enviornment vairables in run configurations, use postman for api requests
