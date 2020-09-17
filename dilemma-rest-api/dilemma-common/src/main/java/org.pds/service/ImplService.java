package org.pds.service;

import java.util.List;
import java.util.Optional;

public interface ImplService<T> {

    T save(T t);

    List<T> findAll();

    Optional<T> findById(String id);

    void deleteAll();

    void deleteById(String id);
}
