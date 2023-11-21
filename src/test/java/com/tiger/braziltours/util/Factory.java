package com.tiger.braziltours.util;

import com.tiger.braziltours.dtos.CityDto;
import com.tiger.braziltours.dtos.TouristSpotDto;
import com.tiger.braziltours.entities.City;
import com.tiger.braziltours.entities.TouristSpot;

public class Factory {

    public static City createCity() {
        return new City(
                1L,
                "Recife",
                "Pernambuco",
                "Nordeste",
                "www.recife.braziltours.br");
    }

    public static CityDto createCityDto() {
        return new CityDto(
                2L,
                "Jõao Pessoa",
                "Paraíba",
                "Nordeste",
                "www.joaopessoa.braziltours.br");
    }

    public static TouristSpot createTouristSpot() {
        return new TouristSpot(
                1L,
                "Marco zero",
                "Descrição",
                "www.marcozerto.braziltours.br",
                Factory.createCity());
    }

    public static TouristSpotDto createTouristSpotDto() {
        return new TouristSpotDto(
                2L,
                "Skybar",
                "Descrição",
                "www.skybar.braziltours.br",
                2L);
    }
}
