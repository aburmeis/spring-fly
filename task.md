Task 1
------

Make the services independent and more easy to test.

Setup `AirportRegistry` and `FlightCatalog` with very few airports and flights in the unit tests. Do not use the
`airports.csv` or `flights.csv` file in test but in production setup. Extract data import from constructor to an own
method. Handle errors on import by log and skip. Make the tests independent, use a mock for the `FlightCatalog` test.

Tips:

* inject the data resource, a simple `InputStream` will work
* use inline data for the test to make it more easy to verify
  (`LH,FRA,LHR` and `AB,LHR,MIA` should work for most test cases)
* the [Mockito](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Mockito.html) framework is available