package org.pds.controller;

import lombok.extern.slf4j.Slf4j;
import org.pds.model.Location;
import org.pds.service.LocationImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(LocationController.PATH)
public class LocationController {
    public static final String PATH = "/api/v1/locations";

    private LocationImplService locationService;

    @Autowired
    public void setLocationService(LocationImplService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public List<Location> findAll() {
        return this.locationService.findAll();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Location> save(@RequestBody Location location, UriComponentsBuilder uriComponentsBuilder) {
        this.locationService.save(location);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/locations/{id}").buildAndExpand(location.getLocationId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uriComponents.toUri())
                .body(location);
    }

    @GetMapping("/last")
    public Location getLocation() {
        return this.locationService.findLast();
    }

    @DeleteMapping("/{locationId}")
    public void delete(@PathVariable("locationId") String locationId) {
        this.locationService.deleteById(locationId);
    }
}

