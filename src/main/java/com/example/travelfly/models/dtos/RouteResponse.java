package com.example.travelfly.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RouteResponse {

    public List<String> route;
    public int price;

}
