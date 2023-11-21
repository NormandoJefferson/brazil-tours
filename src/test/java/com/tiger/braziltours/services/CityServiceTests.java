package com.tiger.braziltours.services;

import com.tiger.braziltours.dtos.CityDto;
import com.tiger.braziltours.entities.City;
import com.tiger.braziltours.repositories.CityRepository;
import com.tiger.braziltours.services.exceptions.ResourceNotFoundException;
import com.tiger.braziltours.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CityServiceTests {

    @InjectMocks
    private CityService service;

    @Mock
    private CityRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private City city;
    private CityDto cityDto;
    private PageImpl<City> cityPage;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        city = Factory.createCity();
        cityDto = Factory.createCityDto();
        cityPage = new PageImpl<>(List.of(city));
    }

    @Test
    public void findAllPagedShouldReturnCityDtoPage() {
        // Arrange
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(cityPage);
        Pageable pageable = PageRequest.of(0, 10);
        // Act
        Page<CityDto> cityDtoPage = service.findAllPaged(pageable);
        // Assert
        Assertions.assertNotNull(cityDtoPage);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnCityDtoWhenIdExists() {
        // Arrange
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(city));
        // Act
        CityDto cityDto = service.findById(existingId);
        // Assert
        Assertions.assertNotNull(cityDto);
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdWithTouristSpotsShouldReturnCityDtoWhenIdExists() {
        // Arrange
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(city));
        // Act
        CityDto cityDto = service.findByIdWithTouristSpots(existingId);
        // Assert
        Assertions.assertNotNull(cityDto);
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        // Act, assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            CityDto cityDtoResult = service.findById(nonExistingId);
        });
        Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    public void insertShouldReturnCityDto() {
        // Arrange
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(city);
        // Act
        CityDto cityDtoResult = service.insert(cityDto);
        // Assert
        Assertions.assertNotNull(cityDtoResult);
        Assertions.assertInstanceOf(CityDto.class , cityDtoResult);
    }

    @Test
    public void updateShouldReturnCityDtoWhenIdExists() {
        // Arrange
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(city);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(city);
        // Act
        CityDto cityDtoResult = service.update(existingId, cityDto);
        // Assert
        Assertions.assertNotNull(cityDtoResult);
        Assertions.assertInstanceOf(CityDto.class , cityDtoResult);
        Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
        Mockito.verify(repository, Mockito.times(1)).save(city);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrage
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        // Act, assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            CityDto cityDtoResult = service.update(nonExistingId, cityDto);
        });
        Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        // Arrange
        Mockito.doNothing().when(repository).deleteById(existingId);
        // Act, assert
        Assertions.assertDoesNotThrow(() -> {
            repository.deleteById(existingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).existsById(nonExistingId);
        // Act, assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
        Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {
        // Arrange
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).existsById(dependentId);
        // Act, assert
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            service.delete(dependentId);
        });
        Mockito.verify(repository, Mockito.times(1)).existsById(dependentId);
    }
}
