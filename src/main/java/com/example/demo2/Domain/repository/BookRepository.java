package com.example.demo2.Domain.repository;

import com.example.demo2.Domain.model.Page;
import com.example.demo2.Domain.repository.entity.BookEntity;

import java.util.Optional;

public interface BookRepository {
    Page<BookEntity> findAll(int page, int size);
    Optional<BookEntity> findByIsbn(String isbn);
    BookEntity save(BookEntity bookEntity);
    Optional<BookEntity> findById(Long id);
    void deleteByIsbn(String isbn);


}
