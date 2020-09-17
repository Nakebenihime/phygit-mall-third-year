package org.pds.controller;

import lombok.extern.slf4j.Slf4j;
import org.pds.mock.MockFrequentation;
import org.pds.model.Frequentation;
import org.pds.service.FrequentationImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping(FrequentationController.PATH)
public class FrequentationController {

    public static final String PATH = "/api/v1/frequentations";
    private FrequentationImplService frequentationService;

    @Autowired
    public void setFrequentationService(FrequentationImplService frequentationService) {
        this.frequentationService = frequentationService;
    }

    @PostMapping
    @ResponseBody
    public Frequentation save(@RequestBody Frequentation frequentation) {
        frequentation.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        this.frequentationService.save(frequentation);
        return frequentation;
    }

    @GetMapping("/{id}")
    public Frequentation getFrequentation(@PathVariable("id") String id) {
        return this.frequentationService.findById(id).get();
    }

    @PostMapping("/mockInsertFrequentation")
    public void addFrequentations() {
        MockFrequentation mockFrequentation = new MockFrequentation();
        Frequentation frequentation;
        int i = 0;
        while (i < 1000) {
            frequentation = mockFrequentation.insertFrequentations();
            this.frequentationService.save(frequentation);
            i++;
        }
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllFrequentations() {
        this.frequentationService.deleteAll();
    }

    @GetMapping("/findAll")
    public Iterable<Frequentation> findAll() {
        return this.frequentationService.findAll();
    }


}
