Task 2
------

Introduce Spring

The setup refactoring from task 1 influenced the code of Application. Spring will manage the setup and wiring itself.
Create a Spring application context from a XML bean file with three main beans: `AirportRegistry`, `FlightCatalog` and
`Application`. Write a new main method initializing the context, grabbing the application bean and running it.

Tips:

* add the following dependencies:
  _compile_ `org.springframework:spring-context-support:4.0.3.RELEASE`
  _runtime_ `org.springframework:spring-aop:4.0.3.RELEASE`
  _testCompile_ `org.springframework:spring-test:4.0.3.RELEASE`
* implement `InitializingBean` to load the data
* also offer setters instead of resource loading in the services to simplify tests, ensure proper initialization anyway
* use the `Resource` interface, in fact the `ClassPathResource` implementation for data injection
* use the `GenericXmlApplicationContext` implementation for loading the beans
* create a Spring context loading test using `AbstractJUnit4SpringContextTests`