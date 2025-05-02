package com.example.travelfly.services;

import com.example.travelfly.models.dtos.RouteResponse;

import java.util.List;

public interface RouteService {

    List<RouteResponse> getAllRoutes(String origin, String destination, Integer maxFlights);

}
