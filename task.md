Task 3
------

Use annotations

Add the appropriate annotations to your bean classes. Ese the XML beans file only if option `-xml`is passed to the
application. Introduce a configuration class to be used by default.

Tips:

* use `AnnotationConfigApplicationContext` instead of `GenericXmlApplicationContext`
* use `@Component`, `@Service`, `@Autowired`, `@Configuration`, `@Bean` and `@Qualifier` if needed
* you might need to modify the XML context to load with `@Autowired` added
