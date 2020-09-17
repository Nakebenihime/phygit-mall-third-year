package org.pds.service;

import org.pds.model.Location;
import org.pds.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationImplService implements ImplService<Location> {

    private LocationRepository locationRepository;

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location save(Location location) {
        return this.locationRepository.save(location);
    }

    @Override
    public List<Location> findAll() {
        return this.locationRepository.findAll();
    }

    @Override
    public Optional<Location> findById(String id) {
        return this.locationRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        this.locationRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        this.locationRepository.deleteById(id);
    }

    public Location findLast() {
        List<Location> locationList = this.locationRepository.findAll();
        return locationList.get(locationList.size() - 1);
    }
}
