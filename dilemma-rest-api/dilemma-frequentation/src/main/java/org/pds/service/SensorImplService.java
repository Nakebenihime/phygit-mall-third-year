package org.pds.service;

import org.pds.model.Sensor;
import org.pds.repository.SensorRepository;
import org.pds.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorImplService implements ImplService<Sensor> {

    private SensorRepository sensorRepository;
    private StoreRepository storeRepository;

    @Autowired
    public void setSensorRepository(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Sensor save(Sensor sensor) {
        return this.sensorRepository.save(sensor);
    }

    @Override
    public List<Sensor> findAll() {
        return this.sensorRepository.findAll();
    }

    @Override
    public Optional<Sensor> findById(String id) {
        return this.sensorRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        this.sensorRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        this.sensorRepository.deleteById(id);
    }

    /**
     * Return name associated to  the store
     *
     * @param UUID
     * @return
     */
    public String associatedStore(String UUID) {
        String storeId = this.sensorRepository.findByUUID(UUID).get().getStoreId();
        return storeRepository.findById(storeId).get().getName();

    }

}
