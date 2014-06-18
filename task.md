Task 5
------

Dynamic binding

Refactor the application and introduce three command beans. Invoke the command by its bean name.

Tips:

* make the commands `@Component`s implementing an own `Command` interface
* inject the dependencies into the commands not the application 
* inject the `ApplicationContext` into the application bean and get all commands out of the context
