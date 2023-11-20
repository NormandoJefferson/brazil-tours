package com.tiger.braziltours.services;

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
public class TouristSpotService {

    @Autowired
    private TouristSpotRepository touristSpotRepository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional(readOnly = true)
    public Page<TouristSpotDto> findAllPaged(Pageable pageable) {
        Page<TouristSpot> touristSpotPage = touristSpotRepository.findAll(pageable);
        return touristSpotPage.map(touristSpot -> new TouristSpotDto(touristSpot));
    }

    @Transactional(readOnly = true)
    public TouristSpotDto findById(Long id) {
        Optional<TouristSpot> touristSpotOptional = touristSpotRepository.findById(id);
        TouristSpot touristSpot = touristSpotOptional.orElseThrow(() ->
                new ResourceNotFoundException("O Id = " + id + " nao foi encontrado"));
        return new TouristSpotDto(touristSpot);
    }

    @Transactional
    public TouristSpotDto insert(TouristSpotDto touristSpotDto) {
        try {
            TouristSpot touristSpot = new TouristSpot();
            copyDtoToEntity(touristSpotDto, touristSpot);
            touristSpot = touristSpotRepository.save(touristSpot);
            return new TouristSpotDto(touristSpot);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    @Transactional
    public TouristSpotDto update(Long id, TouristSpotDto touristSpotDto) {
        try {
            TouristSpot touristSpot = touristSpotRepository.getReferenceById(id);
            copyDtoToEntity(touristSpotDto, touristSpot);
            Long cityId = touristSpotDto.getCityId();
            if (!cityRepository.existsById(cityId)) {
                throw new DatabaseException("Falha de integridade referencial : Não existe Cidade de id = " +cityId);
            }
            touristSpot = touristSpotRepository.save(touristSpot);
            return new TouristSpotDto(touristSpot);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("O Id = " + id + " nao foi encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!touristSpotRepository.existsById(id)) {
            throw new ResourceNotFoundException("O Id = " + id + " nao foi encontrado");
        }
        try {
            touristSpotRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(TouristSpotDto touristSpotDto, TouristSpot touristSpot) {
        touristSpot.setName(touristSpotDto.getName());
        touristSpot.setDescription(touristSpotDto.getDescription());
        touristSpot.setImgUrl(touristSpotDto.getImgUrl());
        touristSpot.setCity(new City(touristSpotDto.getCityId(), null, null, null, null));
    }
}
