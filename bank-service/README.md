# Read Me First

####This test has been implemented with Spring Boot, Spring Batch Combined by following with Hexagonal architecture
* System Requirement
  * Java11
  * maven
  
* Application has been designed with hexagonal architecture where we have three layers called driving, core and driven with domain driven naming for packages
  * driving layer is having controller, dtos and handler, mapper 
  * core layer is having the business logic, It has been decoupled with service interface and core models. In this case core layer doesn't know anything about the driving and driven layer
  * driven layer is having the adapters to get data from data sources or other sub systems, in this layer we have JPA entities JPARepositories, and Command and Query Adapters
  

* How To Run
  * Application has been designed using H2 in memory database. later it can be changed to any database
  * once application has been started you can access the swagger link using http://localhost:8080/swagger-ui/index.html


* Configurable properties
  * Application has been developed by defining default values for the configurable values database related configs can be found in application.properties file
  * Default values can be overwritten by defining in application.properties. those are listed bellow,
    * time zone can be defined with "app.zoneId" default has been set as Asia/Singapore in application.properties
    * for asynchronous processing thread pool has been used with default pool size 10. which can be configured with "maxThreadPoolSize" 
    * daily batch chunk size has been set as 20. which can be changed with "daily.batch.execution-chunk-size"
    * monthly batch chunk size has been set as 20 which can be changed with "monthly.batch.execution-chunk-size"
    * daily scheduler has been set to run at every day at 7pm which can be changed with "dailyInterestCronExpression"
    * monthly scheduler has been set to run at 11pm in last day of month which can be changed with "monthlyInterestCronExpression"



* Implementation Details
  * Idempotency has been handled for all three APIs with database constraints. 
  * Unit tests have been added for implementation to validate the business logic correctness 
  * Thread pool (pool size is configurable via properties) has been introduced for parallel processing the daily interest calculation as the request contain more than one accounts to calculate the daily accrued interest
  * Interest rate has also been added as configurable with default value as 1% per month
  

* Future enhancements
  * Scheduler and batch processor can be defined as a separate microservice, for that we need to introduce a messaging system to synchronize the accounts to the other microservice. 
  * For data synchronization we can use Kafka 