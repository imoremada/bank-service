# Read Me First

####This test has been implemented with Spring Boot, Spring Batch Combined by following with Hexagonal architecture

* Application has been designed with hexagonal architecture where we have three layers called driving, core and driven
  * driving layer is having controller, dtos and handler, mapper 
  * core layer is having the business logic and it has been decoupled with service interface and core models. In this case core layer doesn't know anything about the driving and driven layer
  * driven layer is having the adapters to get data from data sources or other sub systems, in this layer we have JPA entities JPARepositories, and Command and Query Adapters
  

* How To Run
  * Application has been designed using H2 in memory database. later it can be changed to any database
  * once application has been started you can access the swagger link using http://localhost:8080/swagger-ui/index.html


* Implementation Details
  * Idempotency has been handled for all three APIs with database constraints. 
  * Unit tests have been added for implementation to validate the business logic correctness 
  * Thread pool (pool size is configurable via properties) has been introduced for parallel processing the daily interest calculation as the request contain more than one accounts to calculate the daily accrued interest
  * Interest rate has also been added as configurable with default value as 1% per month
  

* Future enhancements
  * Both batch processor and APIs can be divided into two microservices here we need to have a messaging system like Kafka to synchronize the data because we can't connect to same database from two microservices as it is an anti-pattern