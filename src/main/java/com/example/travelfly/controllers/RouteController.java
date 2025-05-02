package com.example.travelfly.controllers;

import com.example.travelfly.models.dtos.RouteRequest;
import com.example.travelfly.models.dtos.RouteResponse;
import com.example.travelfly.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<?> getRoutes(@RequestBody RouteRequest request) {
        List<RouteResponse> response = routeService.getAllRoutes(request.getOrigin(), request.getDestination(), request.getMaxFlights());
        if(response.isEmpty()){
            return ResponseEntity.ok("No routes");
        }
        return ResponseEntity.ok(response);
    }

}

