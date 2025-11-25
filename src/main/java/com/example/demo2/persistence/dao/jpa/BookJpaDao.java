package com.example.demo2.persistence.dao.jpa;

import com.example.demo2.persistence.dao.entity.BookJpaEntity;

import java.util.Optional;

public interface BookJpaDao extends GenericJpaDao<BookJpaEntity> {
    Optional<BookJpaEntity> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
