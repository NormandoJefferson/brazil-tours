package com.tiger.braziltours.dtos;

import com.tiger.braziltours.entities.TouristSpot;

import java.io.Serializable;

public class TouristSpotDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private String imgUrl;
    private Long cityId;

    public TouristSpotDto() {
    }

    public TouristSpotDto(Long id, String name, String description, String imgUrl, Long cityId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.cityId = cityId;
    }

    public TouristSpotDto(TouristSpot entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.imgUrl = entity.getImgUrl();
        this.cityId = entity.getCity().getId();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
