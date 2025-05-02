package com.example.travelfly.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {

    @NotNull
    public String origin;

    @NotNull
    public String destination;

    public Integer maxFlights;


}
