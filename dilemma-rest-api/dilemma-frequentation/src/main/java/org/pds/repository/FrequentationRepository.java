package org.pds.repository;

import org.pds.model.Frequentation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FrequentationRepository extends ElasticsearchRepository<Frequentation, String> {
}
