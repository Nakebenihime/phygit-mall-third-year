package org.pds.controller;


import lombok.extern.slf4j.Slf4j;
import org.pds.model.Sensor;
import org.pds.service.SensorImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(SensorController.PATH)
public class SensorController {
    public static final String PATH = "/api/v1/sensors";
    private SensorImplService sensorService;

    @Autowired
    public void setSensorService(SensorImplService sensorService) {
        this.sensorService = sensorService;
    }


    @GetMapping("/")
    public List<Sensor> findAll() {
        return this.sensorService.findAll();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Sensor> save(@RequestBody Sensor sensor, UriComponentsBuilder uriComponentsBuilder) {
        this.sensorService.save(sensor);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/sensor/{id}").buildAndExpand(sensor.getSensorId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uriComponents.toUri())
                .body(sensor);
    }

    @DeleteMapping("/{sensorId}")
    public void delete(@PathVariable("sensorId") String sensorId) {
        this.sensorService.deleteById(sensorId);
    }


    @GetMapping("/uuid/{uuid}")
    public String associatedStore(@PathVariable("uuid") String uuid) {
        return this.sensorService.associatedStore(uuid);
    }


}
