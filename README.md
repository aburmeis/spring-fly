spring-fly
==========

A simple flight database to be used for a [Spring](http://projects.spring.io/spring-framework/) workshop.

The application
---------------

The application is a simple commandline utility to determine flight connections. It will offer the following commands:

### airports

List all known airports.

### destinations

List all destination airports of one airport. By default only direct flights are used, you may also allow one or more
stops.

### connections

List all connections betwen two airports. By default only one stop is allowed, you may specify the maximum stops (0 is
for direct flights only).


The workshop
------------

The workshop is based on the application code provided. It is divided into [tasks](task.md).