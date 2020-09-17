package org.pds.controller;


import lombok.extern.slf4j.Slf4j;
import org.pds.model.Store;
import org.pds.service.StoreImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(StoreController.PATH)
public class StoreController {
    public static final String PATH = "/api/v1/stores";

    StoreImplService storeService;

    @Autowired
    public void setStoreService(StoreImplService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Store> save(@RequestBody Store store, UriComponentsBuilder uriComponentsBuilder) {
        this.storeService.save(store);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/stores/{id}").buildAndExpand(store.getStoreId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uriComponents.toUri())
                .body(store);
    }

    @GetMapping("/")
    public List<Store> findAll() {
        List<Store> stores = this.storeService.findAll();
        return stores;
    }

    @GetMapping("/{storeId}")
    public Optional<Store> findById(@PathVariable("storeId") String storeId) {
        Optional<Store> store = this.storeService.findById(storeId);
        return store;
    }

    @PutMapping
    public void update(@RequestBody Store store) {
        this.storeService.save(store);
    }

    @DeleteMapping
    public void deleteAll() {
        this.storeService.deleteAll();
    }

    @DeleteMapping("/{storeId}")
    public void delete(@PathVariable("storeId") String storeId) {
        this.storeService.deleteById(storeId);
    }


}
