package com.example.travelfly.services;

import com.example.travelfly.models.Flight;
import com.example.travelfly.models.dtos.RouteResponse;
import com.example.travelfly.repositories.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceImplTest {

    @InjectMocks
    private RouteServiceImpl routeService;

    @Mock
    private FlightRepository flightRepository;

    @Test
    public void getAllRoutes_ShouldReturn_CorrectRoutes_When_1maxFlights() throws IOException {

        Map<String, List<Flight>> testData = new HashMap<>();
        testData.put("SOF", List.of(new Flight("MLE", 70)));
        when(flightRepository.loadFlights()).thenReturn(testData);

        routeService.loadFlights();

        List<RouteResponse> routes = routeService.getAllRoutes("SOF", "MLE", 1);

        assertEquals(1, routes.size());
        RouteResponse route = routes.get(0);
        assertEquals(List.of("SOF", "MLE"), route.getRoute());
        assertEquals(70, route.getPrice());
    }

    @Test
    public void getAllRoutes_ShouldReturn_CorrectRoutes_When_2maxFlights() throws IOException {

        Map<String, List<Flight>> testData = new HashMap<>();
        testData.put("SOF", Arrays.asList(
                new Flight("LON", 10),
                new Flight("MLE", 70)
        ));
        testData.put("LON", List.of(
                new Flight("MLE", 20)
        ));
        when(flightRepository.loadFlights()).thenReturn(testData);

        routeService.loadFlights();

        List<RouteResponse> routes = routeService.getAllRoutes("SOF", "MLE", 2);

        assertEquals(2, routes.size());

        boolean foundIndirect = routes.stream().anyMatch(r ->
                r.getRoute().equals(List.of("SOF", "LON", "MLE")) &&
                        r.getPrice() == 30
        );
        assertTrue(foundIndirect);

        boolean foundDirect = routes.stream().anyMatch(r ->
                r.getRoute().equals(List.of("SOF", "MLE")) &&
                        r.getPrice() == 70
        );
        assertTrue(foundDirect);
    }

    @Test
    public void getAllRoutes_ShouldReturn_SortedRoutesByPrice_When_ValidData() throws IOException {

        Map<String, List<Flight>> testData = new HashMap<>();
        testData.put("SOF", Arrays.asList(
                new Flight("MLE", 70),
                new Flight("LON", 10)
        ));
        testData.put("LON", List.of(new Flight("MLE", 20)));
        when(flightRepository.loadFlights()).thenReturn(testData);

        routeService.loadFlights();

        List<RouteResponse> routes = routeService.getAllRoutes("SOF", "MLE", 2);

        assertEquals(30, routes.get(0).getPrice());
        assertEquals(List.of("SOF", "LON", "MLE"), routes.get(0).getRoute());
        assertEquals(70, routes.get(1).getPrice());
        assertEquals(List.of("SOF", "MLE"), routes.get(1).getRoute());
    }

    @Test
    public void getAllRoutes_ShouldReturn_NoRoutes_When_FlightsExceedMaxLimit() throws Exception {
        Map<String, List<Flight>> testData = new HashMap<>();
        testData.put("SOF", List.of(new Flight("MLE", 70)));
        when(flightRepository.loadFlights()).thenReturn(testData);

        routeService.loadFlights();

        List<RouteResponse> routes = routeService.getAllRoutes("SOF", "MLE", 0);

        assertTrue(routes.isEmpty());
    }

    @Test
    public void getAllRoutes_ShouldReturn_NoRoutes_WhenNoMatchingData() throws IOException {

        Map<String, List<Flight>> testData = new HashMap<>();
        testData.put("SOF", List.of(new Flight("LON", 10)));
        when(flightRepository.loadFlights()).thenReturn(testData);

        routeService.loadFlights();

        List<RouteResponse> routes = routeService.getAllRoutes("SOF", "NYC", 2);
        assertTrue(routes.isEmpty());
    }

    @Test
    public void getAllRoutes_ShouldReturn_AllRoutes_When_WithSamePrice() throws IOException {

        Map<String, List<Flight>> testData = new HashMap<>();
        testData.put("SOF", Arrays.asList(
                new Flight("MLE", 50),
                new Flight("LON", 20),
                new Flight("PAR", 30)
        ));
        testData.put("LON", List.of(new Flight("MLE", 30)));
        testData.put("PAR", List.of(new Flight("MLE", 20)));
        when(flightRepository.loadFlights()).thenReturn(testData);

        routeService.loadFlights();

        List<RouteResponse> result = routeService.getAllRoutes("SOF", "MLE", null);

        assertEquals(3, result.size());
        assertEquals(50, result.get(0).getPrice());
        assertEquals(50, result.get(1).getPrice());
        assertEquals(50, result.get(2).getPrice());
    }

}