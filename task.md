Task 9
------

Spring Boot has an easy support to enable Web MVC. This has been done in `Config` by adding `@EnableWebMvc`.
Add an airport controller which offers the following access to the airport registry:

| Mapping | Operation |
| :------ | :-------- |
| [/rest/airport/{iataCode}](http://localhost:8080/rest/airport/FRA) | return the attributes for the airport with the passed IATA code. |
| [/rest/airport/search](http://localhost:8080/rest/airport/search?country=DE) | return all airports with their attributes, optionally for one country only if a parameter `country` is passed with a 2-letter ISO code. |

Tips:

* create a new `com.tui.fly.web.AirportController` using `@RestController` and `@RequestMapping`
* write a unit test `MockMvcBuilders.standaloneSetup(controller).build()`
* use `gradle bootRun` to do a manual integration test using the links above
