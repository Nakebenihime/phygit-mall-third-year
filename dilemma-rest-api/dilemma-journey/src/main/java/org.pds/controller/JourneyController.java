package org.pds.controller;

import lombok.extern.slf4j.Slf4j;
import org.pds.model.Journey;
import org.pds.model.Store;
import org.pds.service.JourneyImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(JourneyController.PATH)
public class JourneyController {
    public static final String PATH = "/api/v1/journeys";

    JourneyImplService journeyService;

    @Autowired
    public void setJourneyService(JourneyImplService journeyService) {
        this.journeyService = journeyService;
    }

    @GetMapping("/")
    public Journey initAJourney() {
        return journeyService.initAJourney();
    }

    @GetMapping("/{journeyId}")
    public List<Store> getJourney(@PathVariable String journeyId) {
        return this.journeyService.getJourney(journeyId);
    }

    @GetMapping("/journeys")
    public List<Journey> getAllJourneys() {
        journeyService.insertData();
        return journeyService.findAll();
    }
}


