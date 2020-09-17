package org.pds.service;

import org.pds.model.Frequentation;
import org.pds.repository.FrequentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FrequentationImplService {
    private FrequentationRepository frequentationRepository;

    @Autowired
    public void setFrequentationRepository(FrequentationRepository frequentationRepository) {
        this.frequentationRepository = frequentationRepository;
    }

    public Frequentation save(Frequentation frequentation) {
        return frequentationRepository.save(frequentation);
    }

    public Optional<Frequentation> findById(String id){
        return frequentationRepository.findById(id);
    }

    public void deleteAll(){ frequentationRepository.deleteAll();}

    public Iterable<Frequentation> findAll(){ return frequentationRepository.findAll();}

    public long count(){ return frequentationRepository.count();}
}
