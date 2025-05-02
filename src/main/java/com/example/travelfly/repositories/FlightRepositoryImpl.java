package com.example.travelfly.repositories;

import com.example.travelfly.models.Flight;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FlightRepositoryImpl implements FlightRepository {

    @Override
    public Map<String, List<Flight>> loadFlights() throws IOException {
        Map<String, List<Flight>> flightMap = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/flights.txt"));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            String origin = parts[0].trim().toUpperCase();
            String destination = parts[1].trim().toUpperCase();
            int price = Integer.parseInt(parts[2].trim());

            flightMap.computeIfAbsent(origin, k -> new ArrayList<>())
                    .add(new Flight(destination, price));
        }

        return flightMap;
    }
}
