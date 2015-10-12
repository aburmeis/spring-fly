Task 10
-------

Create an error handler to make an unknown airport or invalid country return a valid JSON with error message and appropriate HTTP response status code:



| Mapping | Reponse |
| :------ | :-------- |
| [/rest/airport/XYZ](http://localhost:8080/rest/airport/XYZ) | 404 |
| [/rest/airport/search?country=foo](http://localhost:8080/rest/airport/search?country=foo) | 400 |

Tips:

* create a `@ControllerAdvice` with`@ExceptionHandler` methods for each return status
* extend the integration test by an error case
* You may use `ResponseEntity` and `ErrorAttributes` if you like
