package com.example.travelfly.services;

import com.example.travelfly.models.Flight;
import com.example.travelfly.models.dtos.RouteResponse;
import com.example.travelfly.repositories.FlightRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {

    private final FlightRepository flightRepository;
    private Map<String, List<Flight>> flightMap = new HashMap<>();

    public RouteServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @PostConstruct
    public void loadFlights() throws IOException {
        this.flightMap = flightRepository.loadFlights();
    }

    public List<RouteResponse> getAllRoutes(String origin, String destination, Integer maxFlights) {
        List<RouteResponse> result = new ArrayList<>();
        dfs(origin, destination, new ArrayList<>(), 0, result, new HashSet<>(), maxFlights);
        result.sort(Comparator.comparingInt(RouteResponse::getPrice));
        return result;
    }

    private void dfs(String current, String destination, List<String> path, int cost,
                     List<RouteResponse> result, Set<String> visited, Integer maxFlights) {

        path.add(current);
        visited.add(current);

        if (current.equals(destination)) {
            result.add(new RouteResponse(new ArrayList<>(path), cost));
        } else if (flightMap.containsKey(current)) {
            if (maxFlights == null || path.size() - 1 < maxFlights) {
                for (Flight flight : flightMap.get(current)) {
                    if (!visited.contains(flight.getOriginDestination())) {
                        dfs(flight.getOriginDestination(), destination, path, cost + flight.getPrice(),
                                result, visited, maxFlights);
                    }
                }
            }
        }

        path.remove(path.size() - 1);
        visited.remove(current);
    }

}
