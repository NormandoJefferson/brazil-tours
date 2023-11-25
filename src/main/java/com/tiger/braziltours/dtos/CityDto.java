package com.tiger.braziltours.dtos;

import com.tiger.braziltours.entities.City;
import com.tiger.braziltours.entities.TouristSpot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CityDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size(min = 2, max = 20, message = "Deve ter entre 2 e 20 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String name;

    @Size(min = 2, max = 20, message = "Deve ter entre 2 e 20 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String state;

    @Size(min = 2, max = 20, message = "Deve ter entre 2 e 20 caracteres")
    @NotBlank(message = "Campo não pode ser vazio")
    private String region;

    @Size(max = 100)
    private String imgUrl;

    private List<TouristSpotDto> touristSpotDtoList = new ArrayList<>();

    public CityDto() {
    }

    public CityDto(Long id, String name, String state, String region, String imgUrl) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.region = region;
        this.imgUrl = imgUrl;
    }

    public CityDto(City entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.state = entity.getState();
        this.region = entity.getRegion();
        this.imgUrl = entity.getImgUrl();
    }

    public CityDto(City entity, Set<TouristSpot> touristSpotSet) {
       this(entity);
        touristSpotSet.forEach(touristSpot -> touristSpotDtoList.add(new TouristSpotDto(touristSpot)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<TouristSpotDto> getTouristSpotDtoList() {
        return touristSpotDtoList;
    }
}
