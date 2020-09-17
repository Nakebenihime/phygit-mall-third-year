package org.pds.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ConsumerController.PATH)
public class ConsumerController {
    public static final String PATH = "/api/v1/events";

    @Value("${proxy.subscriber.uri}")
    private String URI;

    @PostMapping("/recognized")
    public ResponseEntity<Void> recognized() {
        log.info("Event was triggered, redirect to subscriber server " + URI);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION, URI);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .headers(responseHeaders)
                .build();
    }
}