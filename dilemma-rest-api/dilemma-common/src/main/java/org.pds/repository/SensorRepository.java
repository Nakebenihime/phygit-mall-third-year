package org.pds.repository;


import org.pds.model.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {
    Optional<Sensor> findByUUID(String UUID);
}
