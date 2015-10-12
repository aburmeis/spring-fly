Task 11
-------

Create an additional flight controller offering destinations and connections.

| Mapping | Service |
| :------ | :-------- |
| [/rest/flight/destination/{iataCode}](http://localhost:8080/rest/flight/destination/FRA?maxStops=1) | all destination airports with a maximum number of stops (default to direct connections) |
| [/rest/flight/connection/{iataCode}/{iataCode}](http://localhost:8080/rest/flight/connection/FRA/JFK?maxStops=1) | all connections from the departure (first) airport to the destination (second) airport with a maximum number of stops (default to direct connections) |

Tips:

* create a new controller using both `AirportRegistry` and `FlightCatalog`
* do not return connections but list of list of flights
