Task 7
------

Use a database

Create repositories for airports and flights using a database with a table for each CSV file. Create a separate
database configuration for the new profile _relational_ with a data source and a transaction manager.

Tips:

* rename the current services to `InMemory…`
* introduce interfaces for both repositories and use them for dependencies
* add the following compile dependencies:  
  org.springframework:spring-jdbc:4.0.3.RELEASE  
  org.springframework:spring-tx:4.0.3.RELEASE  
  org.hsqldb:hsqldb:2.3.2  
  org.liquibase:liquibase-core:3.2.0
* enable transaction management for the database config
* use `JdbcTemplate` and `RowMapper` to implements the queries
* add a [liquibase](http://www.liquibase.org/documentation/spring.html) bean and write a
  [changelog](http://www.liquibase.org/documentation/databasechangelog.html) to create the tables and fill in the data
  using `loadData`