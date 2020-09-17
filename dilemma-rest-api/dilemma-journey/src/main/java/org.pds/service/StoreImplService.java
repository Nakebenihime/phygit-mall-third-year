package org.pds.service;

import org.pds.model.Store;
import org.pds.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StoreImplService implements ImplService<Store> {

    private StoreRepository storeRepository;

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Store save(Store store) {
        return this.storeRepository.save(store);
    }

    @Override
    public List<Store> findAll() {
        return this.storeRepository.findAll();
    }

    @Override
    public Optional<Store> findById(String id) {
        return this.storeRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        this.storeRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        storeRepository.deleteById(id);
    }
}
