Task 4
------

Unique conversion

Conversions are spread all over the code base. Spring has a `ConversionService` to do this centralized but extensible.
Use the service in the application to convert connections to strings.

Tips:

* write and test the converter implementing the `Converter` interface
* register them in the configuration using a `ConversionServiceFactoryBean`
* `ConversionService` is provided by Spring, just use it autowired!