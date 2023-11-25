package com.tiger.braziltours.controllers;

import com.tiger.braziltours.dtos.TouristSpotDto;
import com.tiger.braziltours.services.TouristSpotService;
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
@RequestMapping(value = "/touristspots")
public class TouristSpotController {

    @Autowired
    private TouristSpotService touristSpotService;

    @GetMapping
    public ResponseEntity<Page<TouristSpotDto>> findAllPaged(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        Page<TouristSpotDto> touristSpotDtoPage = touristSpotService.findAllPaged(pageable);
        return ResponseEntity.ok().body(touristSpotDtoPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TouristSpotDto> findById(@PathVariable Long id) {
        TouristSpotDto touristSpotDto = touristSpotService.findById(id);
        return ResponseEntity.ok().body(touristSpotDto);
    }

    @PostMapping
    public ResponseEntity<TouristSpotDto> insert(@Valid @RequestBody TouristSpotDto dto) {
        TouristSpotDto touristSpotDto = touristSpotService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(touristSpotDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(touristSpotDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TouristSpotDto> update(@Valid @PathVariable Long id, @RequestBody TouristSpotDto dto) {
        TouristSpotDto touristSpotDto = touristSpotService.update(id, dto);
        return ResponseEntity.ok().body(touristSpotDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        touristSpotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
