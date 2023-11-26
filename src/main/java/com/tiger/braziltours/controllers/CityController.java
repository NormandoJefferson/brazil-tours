package com.tiger.braziltours.controllers;

import com.tiger.braziltours.dtos.CityDto;
import com.tiger.braziltours.dtos.CityInsertDto;
import com.tiger.braziltours.dtos.CityUpdateDto;
import com.tiger.braziltours.services.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<Page<CityDto>> findAllPaged(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CityDto> cityDtoPage = cityService.findAllPaged(pageable);
        return ResponseEntity.ok().body(cityDtoPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDto> findById(@PathVariable Long id) {
        CityDto cityDto = cityService.findById(id);
        return ResponseEntity.ok().body(cityDto);
    }

    @GetMapping(value = "/touristspots/{id}")
    public ResponseEntity<CityDto> findByIdWithTouristSpots(@PathVariable Long id) {
        CityDto cityDto = cityService.findByIdWithTouristSpots(id);
        return ResponseEntity.ok().body(cityDto);
    }

    @PostMapping
    public ResponseEntity<CityDto> insert(@Valid @RequestBody CityInsertDto cityInsertDto) {
        CityDto cityDto = cityService.insert(cityInsertDto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cityDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(cityDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CityDto> update(@PathVariable Long id, @Valid @RequestBody CityUpdateDto cityUpdateDto) {
        CityDto cityDto = cityService.update(id, cityUpdateDto);
        return ResponseEntity.ok().body(cityDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
