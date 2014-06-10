spring-fly
==========

A simple flight database as base for a Spring workshop.

The application is a commandline utility. It will offer the following commands:

airports
--------

List all known airports.

destinations
------------

List all destination airports of one airport. By default only direct flights are used, you may also allow one or more stops.

connections
-----------

List all connections betwen two airports. By default only one stop is allowed, you may specify the maximum stops (0 is for direct flights only).
