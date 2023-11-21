package com.tiger.braziltours.services;

import com.tiger.braziltours.dtos.CityDto;
import com.tiger.braziltours.dtos.TouristSpotDto;
import com.tiger.braziltours.entities.City;
import com.tiger.braziltours.entities.TouristSpot;
import com.tiger.braziltours.repositories.CityRepository;
import com.tiger.braziltours.repositories.TouristSpotRepository;
import com.tiger.braziltours.services.exceptions.DatabaseException;
import com.tiger.braziltours.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TouristSpotRepository touristSpotRepository;

    @Transactional(readOnly = true)
    public Page<CityDto> findAllPaged(Pageable pageable) {
        Page<City> cityPage = cityRepository.findAll(pageable);
        return cityPage.map(city -> new CityDto(city));
    }

    @Transactional(readOnly = true)
    public CityDto findById(Long id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        City city = cityOptional.orElseThrow(() ->
                new ResourceNotFoundException("O Id = " + id + " nao foi encontrado"));
        return new CityDto(city);
    }

    @Transactional(readOnly = true)
    public CityDto findByIdWithTouristSpots(Long id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        City city = cityOptional.orElseThrow(()
                -> new ResourceNotFoundException("O Id = " + id + " nao foi encontrado"));
        return new CityDto(city, city.getTouristSpotList());
    }

    @Transactional
    public CityDto insert(CityDto cityDto) {
        City city = new City();
        copyDtoToEntity(cityDto, city);
        city = cityRepository.save(city);
        return new CityDto(city);
    }

    @Transactional
    public CityDto update(Long id, CityDto cityDto) {
        try {
            City city = cityRepository.getReferenceById(id);
            copyDtoToEntity(cityDto, city);
            city = cityRepository.save(city);
            return new CityDto(city);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("O Id = " + id + " nao foi encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("O Id = " + id + " nao foi encontrado");
        }
        try {
            cityRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(CityDto cityDto, City city) {
        city.setName(cityDto.getName());
        city.setState(cityDto.getState());
        city.setRegion(cityDto.getRegion());
        city.setImgUrl(cityDto.getImgUrl());
        city.getTouristSpotList().clear();
        for (TouristSpotDto touristSpotDto : cityDto.getTouristSpotDtoList()) {
            TouristSpot touristSpot = touristSpotRepository.getReferenceById(touristSpotDto.getId());
            city.getTouristSpotList().add(touristSpot);
        }
    }
}
