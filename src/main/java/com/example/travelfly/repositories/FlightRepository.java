package com.example.travelfly.repositories;

import com.example.travelfly.models.Flight;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FlightRepository {

    Map<String, List<Flight>> loadFlights() throws IOException;
}
