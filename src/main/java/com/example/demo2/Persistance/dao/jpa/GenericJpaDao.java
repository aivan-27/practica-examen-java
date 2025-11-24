package com.example.demo2.Persistance.dao.jpa;

import java.util.List;
import java.util.Optional;

public interface GenericJpaDao<T> {
    List<T> findAll(int page, int size);
    Optional<T> findById(Long id);
    T insert(T jpaEntity);
    T update(T jpaEntity);
    void deleteById(Long id);
    long count();

}
