package org.pds.repository;

import org.pds.model.Journey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends MongoRepository<Journey, String> {

}
