package org.pds.repository;

import org.pds.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {
}
